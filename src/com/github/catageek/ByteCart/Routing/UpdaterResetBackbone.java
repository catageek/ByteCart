package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.BlockFace;
import com.github.catageek.ByteCart.Signs.BCSign;

class UpdaterResetBackbone extends UpdaterBackBone implements Updater {

	UpdaterResetBackbone(BCSign bc, UpdaterContent rte) {
		super(bc, rte);
	}


	@Override
	public void doAction(BlockFace to) {
		if (! this.isAtBorder())
			reset();
	}
	
	@Override
	protected BlockFace selectDirection() {
		BlockFace face;
		if ((face = manageBorder()) != null)
			return face;
		return DefaultRouterWanderer.getRandomBlockFace(getRoutingTable(), getFrom().getBlockFace());
	}


}
