package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;
import com.github.catageek.ByteCart.ByteCart;

public final class UpdaterRegion extends AbstractUpdater implements Updater {

	private final int Region;

	public UpdaterRegion(RoutingTable routingtable, Vehicle vehicle,
			Address ringAddress, BlockFace from, boolean isTrackNumberProvider, Level level) {
		super(routingtable, vehicle, ringAddress, from, isTrackNumberProvider, level);
		Region = ByteCart.myPlugin.getUm().getMapRoutes().get(vehicle.getEntityId()).getRegion();

	}

	@Override
	protected String getAddress(int ring) {
		return "" + Region + "." + ring + ".0";
	}

	@Override
	protected int setSign(int current) {
		current = findFreeTrackNumber(current);
		// update sign with new number we found or current
		this.getSignAddress().setAddress(this.getAddress(current));
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : change address on sign to " + this.getAddress(current));
		return current;
	}

	private int findFreeTrackNumber(int current) {
		while(current < 0) {
			// find a free number for the track if needed
			current = this.getCounter().firstEmpty();
			// if there is already a route, find another number
			// except if the route connects to here
			if (! this.getRoutingTable().isEmpty(current)
					&& ! this.getRoutingTable().isDirectlyConnected(current, getFrom())) {
				this.getCounter().incrementCount(current);
				current = -1;
			}
		}
		return current;
	}


	protected int getCurrent(int current) {
		// check if the sign has not priority
		if (current >= 0  && current < getTrackNumber()) {
			// current < sign => reset counter, clear route and write sign
			this.getCounter().reset(getTrackNumber());
			this.getRoutingTable().removeEntry(getTrackNumber());
			this.getSignAddress().setAddress(this.getAddress(current));
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : change address on sign to " + this.getAddress(current));
			return current;
		}
		else {
			// sign seems to have priority
			// if the router knows that it is directly connected
			// we keep it, otherwise we find a new number (if possible)
			if (! this.getRoutingTable().isDirectlyConnected(getTrackNumber(), getFrom()) && this.isTrackNumberProvider())
				return setSign(current);
			return getTrackNumber();
		}
	}

	protected boolean isSignNeedUpdate(int current) {
		return (! getSignAddress().isValid() && current != -2) 
				|| (getSignAddress().isValid() && getCounter().getCount(getTrackNumber()) == 0 && current != -2)
				|| (current >= 0 && current != getTrackNumber())
				|| ! this.getRoutingTable().isDirectlyConnected(getTrackNumber(), getFrom());
	}

	@Override
	protected BlockFace selectDirection() {
		BlockFace face;
		if ((face = super.selectDirection()) != null)
			return face;

		if (this.getRoutes() != null) {
			// current: track number we are on
			int current = this.getRoutes().getCurrent();

			if (this.isSignNeedUpdate(current))
				return this.getFrom().getBlockFace();

			// if there is a side we don't have visited yet, let go there
			if (isTrackNumberProvider()) {
				if ((face = this.getRoutingTable().getFirstUnknown()) != null && ! this.isSameTrack(face))
					return face;

				int min;
				if((min = this.getCounter().getMinimum(this.getRoutingTable(), this.getFrom())) != -1)
					return this.getRoutingTable().getDirection(min).getBlockFace();
			}
		}
		return AbstractUpdater.getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
	}

	@Override
	public void Update(BlockFace To) {

		if (isResetCart()) {
			reset();
			return;
		}

		if (getRoutes() != null) {

			// current: track number we are on
			int current = getRoutes().getCurrent();
			boolean isNew = (current < 0);

			if(isTrackNumberProvider() && ! getSignAddress().isValid()) {
				// if there is no address on the sign
				// we provide one
				current = setSign(current);

			}

			if (getSignAddress().isValid()) {
				// there is an address on the sign
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : track number is " + getTrackNumber());
				current = getCurrent(current);

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : current is " + current);
			}

			// update track counter if we have entered a new one
			if (current >= 0 && isNew) {
				this.getCounter().incrementCount(current);
				if (this.getCounter().isAllFull()) {
					int zero = this.getCounter().getCount(0);
					this.getCounter().resetAll();
					this.getCounter().setCount(0, ++zero);
				}
			}

			routeUpdates(To, current);

		}

	}

}
