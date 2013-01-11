package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;

public final class UpdaterBackBone extends AbstractUpdater implements Updater {

	public UpdaterBackBone(
			com.github.catageek.ByteCart.Routing.RoutingTable routingtable,
			org.bukkit.entity.Vehicle vehicle, Address trackaddress, BlockFace from, boolean isTrackNumberProvider, Level level) {
		super(routingtable, vehicle, trackaddress, from, isTrackNumberProvider, level);
	}

	@Override
	protected String getAddress(int var) {
		return "" + var + ".0.0";
	}

	@Override
	protected int setSign(int current) {
		return -1;
	}

	@Override
	protected int getCurrent(int current) {
		return getTrackNumber();
	}

	@Override
	protected BlockFace selectDirection() {
		BlockFace face;
		if ((face = super.selectDirection()) != null)
			return face;

		return AbstractUpdater.getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
	}
	
	@Override
	public void Update(BlockFace To) {
		if (getRoutes() != null) {

			// current: track number we are on
			int current = getRoutes().getCurrent();

			if (getSignAddress().isValid()) {
				// there is an address on the sign
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : track number is " + getTrackNumber());
				current = getCurrent(current);

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : current is " + current);
			}
			else
				// no address on sign, and is not provider
				// assumes it's 0 if first sign met
				if (current == -2)
					current = 0;

			routeUpdates(To, current);

		}
		super.Update(To);

	}



}
