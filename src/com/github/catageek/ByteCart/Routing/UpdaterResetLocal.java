package com.github.catageek.ByteCart.Routing;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Event.UpdaterClearStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterClearSubnetEvent;
import com.github.catageek.ByteCart.Signs.BCSign;

public final class UpdaterResetLocal extends UpdaterLocal implements Updater {

	protected UpdaterResetLocal(BCSign bc) {
		super(bc);
	}

	@Override
	public void doAction(BlockFace to) {
		return;
	}

	@Override
	public void doAction(Side to) {
		Address address = this.getSignAddress();
		if (address.isValid()) {
			if (this.getNetmask() == 4) {
				UpdaterClearStationEvent event = new UpdaterClearStationEvent(this, address);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
			else {
				UpdaterClearSubnetEvent event = new UpdaterClearSubnetEvent(this, address, 16 >> this.getNetmask());
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}
		address.remove();
	}

	@Override
	public Side giveSimpleDirection() {
		if (this.getNetmask() < 4)
			return Side.RIGHT;
		return Side.LEFT;
	}

	@Override
	public BlockFace giveRouterDirection() {
		return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
	}
}
