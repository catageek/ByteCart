package com.github.catageek.ByteCart.Signs;

/**
 * A 64-station subnet bound
 */
final class BC9064  extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9064(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 2;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.AbstractSimpleCrossroad#getName()
	 */
	@Override
	public final String getName() {
		return "BC9064";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "64-station subnet";
	}
}