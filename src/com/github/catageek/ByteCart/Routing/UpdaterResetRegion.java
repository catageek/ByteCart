package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.BlockFace;
import com.github.catageek.ByteCart.Signs.BCSign;

public final class UpdaterResetRegion extends UpdaterRegion implements Updater {

	public UpdaterResetRegion(BCSign bc) {
		super(bc);
	}

	@Override
	public void doAction(BlockFace to) {
		reset();
	}

	@Override
	protected BlockFace selectDirection() {
		if (this.getLevel().number != com.github.catageek.ByteCart.Routing.Updater.Level.REGION.number)
		{
			// if we are at the border of the region
			// going back

			return getFrom().getBlockFace();
		}
		return DefaultRouterWanderer.getRandomBlockFace(getRoutingTable(), getFrom().getBlockFace());
	}

}
