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
		getRoutingTable().clear();
	}


}
