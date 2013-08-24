package com.github.catageek.ByteCart.Signs;

/**
 * A 32-station subnet bound
 */
final class BC9032 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9032(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 3;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.AbstractSimpleCrossroad#getName()
	 */
	@Override
	public final String getName() {
		return "BC9032";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "32-station subnet";
	}
}

