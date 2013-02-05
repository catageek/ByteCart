package com.github.catageek.ByteCart.Routing;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.Event.UpdaterClearRingEvent;
import com.github.catageek.ByteCart.Signs.BCSign;

public final class UpdaterResetRegion extends UpdaterRegion implements Updater {

	public UpdaterResetRegion(BCSign bc) {
		super(bc);
	}

	@Override
	public void doAction(BlockFace to) {
		if (this.getSignAddress().isValid()) {
			UpdaterClearRingEvent event = new UpdaterClearRingEvent(this, this.getTrackNumber());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
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
