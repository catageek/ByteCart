package com.github.catageek.ByteCart.Routing;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.Event.UpdaterClearRingEvent;
import com.github.catageek.ByteCart.Signs.BCSign;

final class UpdaterResetRegion extends UpdaterRegion implements Updater {

	UpdaterResetRegion(BCSign bc, UpdaterContent rte) {
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


	@Override
	protected final void reset() {
		// case of reset
		// erase address on sign if ring 0
		Address address = this.getSignAddress();
		boolean isValid = address.isValid();
		int track = this.getTrackNumber();

		if (!isValid || track == -1) {
			address.remove();
			if (isValid) {
				UpdaterClearRingEvent event = new UpdaterClearRingEvent(this, 0);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}
		// clear routes except route to ring 0
		super.reset();
	}
}
