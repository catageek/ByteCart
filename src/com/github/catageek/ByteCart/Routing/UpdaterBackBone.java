package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.BlockFace;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Signs.BCSign;

public class UpdaterBackBone extends AbstractRegionUpdater implements Updater {

	protected UpdaterBackBone(BCSign bc) {
		super(bc);
	}


	protected BlockFace selectDirection() {
		if ((isAtBorder()))
			return getFrom().getBlockFace();

		return DefaultRouterWanderer.getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
	}

	@Override
	public void Update(BlockFace To) {

		// current: track number we are on
		int current = getCurrent();

		if (getRoutes() != null) {

			if (getSignAddress().isValid()) {
				// there is an address on the sign
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : track number is " + getTrackNumber());
				setCurrent(getTrackNumber());

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : current is " + current);
			}
			else
				// no address on sign, and is not provider
				// assumes it's 0 if first sign met
				if (current == -2)
					setCurrent(0);

			routeUpdates(To);

		}
	}

	@Override
	public final int getTrackNumber() {
		int ret = getSignAddress().getRegion().getAmount();
		return ret == 0 ? -1 : ret;
	}

}
