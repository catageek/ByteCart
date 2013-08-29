package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;
import com.github.catageek.ByteCart.Routing.UpdaterLocal;


final class BC9000 extends AbstractSimpleCrossroad implements Subnet, Triggable {

	private final int netmask;

	BC9000(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 0;
	}

	@Override
	protected void manageUpdater(SimpleCollisionAvoider intersection) {
		// it's an updater, so let it choosing direction
		super.manageUpdater(intersection);

		if (ByteCart.myPlugin.getConfig().getBoolean("oldBC900behaviour", true)) {
			UpdaterLocal updater;
			try {
				updater = (UpdaterLocal) UpdaterFactory.getUpdater(this, this.getInventory());

				// here we perform routes update
				updater.leaveSubnet();
				updater.save();

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return "BC9000";
	}

	@Override
	public String getFriendlyName() {
		return "Collision avoider";
	}

	/**
	 * @return the netmask
	 */
	public final int getNetmask() {
		return netmask;
	}
}
