package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCartAPI.Signs.HasNetmask;
import com.github.catageek.ByteCartAPI.Signs.Subnet;


/**
 * A 4-station subnet bound
 */
final class BC9004 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9004(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 6;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.AbstractSimpleCrossroad#getName()
	 */
	@Override
	public final String getName() {
		return "BC9004";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "4-station subnet";
	}
}
