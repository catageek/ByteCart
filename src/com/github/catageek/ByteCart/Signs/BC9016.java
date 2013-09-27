package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCartAPI.Signs.HasNetmask;
import com.github.catageek.ByteCartAPI.Signs.Subnet;




/**
 * A 16-station subnet bound
 */
final class BC9016 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9016(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 4;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.AbstractSimpleCrossroad#getName()
	 */
	@Override
	public final String getName() {
		return "BC9016";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "16-station subnet";
	}
}
