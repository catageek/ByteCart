package com.github.catageek.ByteCart.Routing;

import java.util.Iterator;
import java.util.List;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.minecart.StorageMinecart;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.CounterInventory;
import com.github.catageek.ByteCart.IO.InventoryHalfStack;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;

abstract class AbstractRegionUpdater extends DefaultRouterWanderer {

	protected AbstractRegionUpdater(BCSign bc) {
		super(bc);
		counter = new CounterInventory(new InventoryHalfStack(((StorageMinecart) this.getVehicle()).getInventory()));
		Routes = ByteCart.myPlugin.getUm().getMapRoutes().get(getVehicle().getEntityId());
	}

	
	abstract protected void Update(BlockFace to);
	abstract protected int getTrackNumber();

	protected RoutingTableExchange Routes;
	private CounterInventory counter;
	abstract protected BlockFace selectDirection();

	protected final void routeUpdates(BlockFace To) {
		if(isRouteConsumer()) {
			List<Integer> connected = getRoutingTable().getDirectlyConnectedList(getFrom());
			int current = getCurrent();
			
			current = (current == -2 ? 0 : current); 

			// if the track we come from is not recorded
			// or others track are wrongly recorded, we correct this
			if (current >=0 && ( ! connected.contains(current) || connected.size() != 1)) {

				Iterator<Integer> it = connected.iterator();
				while (it.hasNext()) {
					getRoutingTable().removeEntry(it.next());
				}

				// Storing the route from where we arrive
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : Updater : storing ring " + current + " direction " + getFrom().ToString());

				getRoutingTable().setEntry(current, MathUtil.binlog(getFrom().getAmount()) << 4, 0);

			}

			// loading received routes in router
			getRoutingTable().Update(getRoutes(), getFrom());


			// preparing the routes to send
			Routes = new RoutingTableExchange(getRoutingTable(), new DirectionRegistry(To), this.getLevel(), getRegion());


			// storing the route in the map
			ByteCart.myPlugin.getUm().getMapRoutes().put(getVehicle().getEntityId(), getRoutes(), false);
			
			setCurrent(current);
		}
	}
	
	@Override
	public void doAction(BlockFace To) {

		boolean isNew = (getCurrent() < 0);

		this.Update(To);

		int current = getCurrent();
		current = (current == -2 ? 0 : current); 
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : current is " + current);

		// update track counter if we have entered a new one
		if (current >= 0 && isNew) {
			this.getCounter().incrementCount(current);
			if (this.getCounter().isAllFull()) {
				int zero = this.getCounter().getCount(0);
				this.getCounter().resetAll();
				this.getCounter().setCount(0, ++zero);
			}
		}

		// If we are turning back, keep current track otherwise discard
		if (!isSameTrack(To))
			getRoutes().setCurrent(-1);
	}
	
	@Override
	public final BlockFace giveRouterDirection() {
		return this.selectDirection();
	}

	protected final RoutingTableExchange getRoutes() {
		return Routes;
	}

	protected final CounterInventory getCounter() {
		return counter;
	}

	public final int getCurrent() {
		if (getRoutes() != null)
			// current: track number we are on
			return getRoutes().getCurrent();
		return -1;
	}

	protected final void setCurrent(int current) {
		if (getRoutes() != null)
			getRoutes().setCurrent(current);
	}

	protected final boolean isRouteConsumer() {
		return getRoutes().getLevel().number == this.getLevel().number;
	}

	protected final void reset() {
		// case of reset
		// erase address on sign
		getSignAddress().remove();
		// clear routes except route to ring 0
		getRoutingTable().clear();
	}
}
