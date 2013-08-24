package com.github.catageek.ByteCart.Signs;

/**
 * A 128-station subnet bound
 */
final class BC9128  extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9128(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 1;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.AbstractSimpleCrossroad#getName()
	 */
	@Override
	public final String getName() {
		return "BC9128";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "128-station subnet";
	}
}