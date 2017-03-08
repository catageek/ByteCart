package com.github.catageek.ByteCart.Updaters;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.BCCounter;
import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Level;

abstract class AbstractRegionUpdater extends DefaultRouterWanderer {

	AbstractRegionUpdater(BCSign bc, UpdaterContent rte) {
		super(bc, rte.getRegion());
		Routes = rte;
		counter = rte.getCounter();

		if (bc instanceof BC8010) {
			BC8010 ic = (BC8010) bc;
			IsTrackNumberProvider = ic.isTrackNumberProvider();
		}
		else
			IsTrackNumberProvider = false;
	}


	abstract protected void Update(BlockFace to);
	abstract protected int getTrackNumber();

	protected UpdaterContent Routes;
	private final boolean IsTrackNumberProvider;
	private BCCounter counter;
	abstract protected BlockFace selectDirection();

	/**
	 * Perform the IGP routing protocol update
	 * 
	 * @param To the direction where we are going to
	 */
	protected final void routeUpdates(BlockFace To) {
		if(isRouteConsumer()) {
			Set<Integer> connected = getRoutingTable().getDirectlyConnectedList(getFrom().getBlockFace());
			int current = getCurrent();

			current = (current == -2 ? 0 : current); 

			// if the track we come from is not recorded
			// or others track are wrongly recorded, we correct this
			if (current >=0 && ( ! connected.contains(current) || connected.size() != 1)) {

				Iterator<Integer> it = connected.iterator();
				while (it.hasNext()) {
					getRoutingTable().removeEntry(it.next(), getFrom().getBlockFace());
				}

				// Storing the route from where we arrive
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : Wanderer : storing ring " + current + " direction " + getFrom().ToString());

				getRoutingTable().setEntry(current, getFrom().getBlockFace(), 0);
				Routes.updateTimestamp();

			}

			// loading received routes in router if coming from another router
			if (this.getRoutes().getLastrouterid() != this.getCenter().hashCode())
				getRoutingTable().Update(getRoutes(), getFrom().getBlockFace());


			// preparing the routes to send
			Routes.putRoutes(getRoutingTable(), new DirectionRegistry(To));


			// storing the route in the map
			//			ByteCart.myPlugin.getUm().getMapRoutes().put(getVehicle().getEntityId(), getRoutes(), false);

			setCurrent(current);
			this.getRoutes().setLastrouterid(this.getCenter().hashCode());

			try {
				getRoutingTable().serialize();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.DefaultRouterWanderer#doAction(org.bukkit.block.BlockFace)
	 */
	@Override
	public void doAction(BlockFace To) {

		this.Update(To);

		int current = getCurrent();
		current = (current == -2 ? 0 : current); 
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : doAction() : current is " + current);

		// If we are turning back, keep current track otherwise discard
		if (!isSameTrack(To))
			getRoutes().setCurrent(-1);

		this.getRoutes().seenTimestamp();

		try {
			UpdaterContentFactory.saveContent(Routes);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.DefaultRouterWanderer#giveRouterDirection()
	 */
	@Override
	public final BlockFace giveRouterDirection() {
		return this.selectDirection();
	}

	/**
	 * Get the type of updater
	 * 
	 * @return the type
	 */
	public final Level getLevel() {
		return getRoutes().getLevel();
	}

	protected final UpdaterContent getRoutes() {
		return Routes;
	}

	protected final BCCounter getCounter() {
		return counter;
	}

	protected final int getCurrent() {
		if (getRoutes() != null)
			// current: track number we are on
			return getRoutes().getCurrent();
		return -1;
	}

	protected final void setCurrent(int current) {
		if (getRoutes() != null)
			getRoutes().setCurrent(current);
	}

	/**
	 * @return true if the IC can receive routes
	 */
	protected final boolean isRouteConsumer() {
		return getRoutes().getLevel().equals(this.getSignLevel());
	}

	/**
	 * Clear the routing table, keeping ring 0
	 */
	protected void reset() {
		boolean fullreset = this.getRoutes().isFullreset();
		if (fullreset)
			this.getSignAddress().remove();
		// clear routes except route to ring 0
		getRoutingTable().clear(fullreset);
		try {
			getRoutingTable().serialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Tells if this updater must provide track numbers for this IC
	 * 
	 * @return true if this updater must provide track numbers
	 */
	protected final boolean isTrackNumberProvider() {
		return IsTrackNumberProvider;
	}
}
