package com.github.catageek.ByteCart.Routing;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Event.UpdaterSetRingEvent;
import com.github.catageek.ByteCart.Signs.BCSign;

public class UpdaterRegion extends AbstractRegionUpdater implements Updater {

	protected UpdaterRegion(BCSign bc) {
		super(bc);
	}

	private final String getAddress(int ring) {
		return "" + getRegion() + "." + ring + ".0";
	}

	private int setSign(int current) {
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


	private int getOrSetCurrent(int current) {
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

	private boolean isSignNeedUpdate(int current) {
		int track = getTrackNumber();
		return (! getSignAddress().isValid() && current != -2) 
				|| (getSignAddress().isValid() && getCounter().getCount(track) == 0 && current != -2)
				|| (current >= 0 && current != track)
				|| (track != -1 && ! this.getRoutingTable().isDirectlyConnected(track, getFrom()));
	}

	protected BlockFace selectDirection() {
		BlockFace face;
		if ((isAtBorder()))
			return getFrom().getBlockFace();

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
		return DefaultRouterWanderer.getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
	}

	@Override
	public void Update(BlockFace To) {

		// current: track number we are on
		int current = getCurrent();

		if (getRoutes() != null) {

			UpdaterSetRingEvent event = null;

			if(isTrackNumberProvider() && ! getSignAddress().isValid()) {
				// if there is no address on the sign
				// we provide one
				current = setSign(current);
				event = new UpdaterSetRingEvent(this, -1, current);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}

			if (getSignAddress().isValid()) {
				// there is an address on the sign
				int old = getTrackNumber();
				current = getOrSetCurrent(current);
				event = new UpdaterSetRingEvent(this, old, current);
				if (old != current)
					Bukkit.getServer().getPluginManager().callEvent(event);
			}


			if (isSameTrack(To))
				setCurrent(current);
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : current is " + current);

			routeUpdates(To);
		}
	}


	@Override
	public final int getTrackNumber() {
		int ret = getSignAddress().getTrack().getAmount();
		return ret == 0 ? -1 : ret;
	}
}
