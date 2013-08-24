package com.github.catageek.ByteCart.Signs;


/**
 * A 8-station subnet bound
 */
final class BC9008 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9008(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 5;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.AbstractSimpleCrossroad#getName()
	 */
	@Override
	public final String getName() {
		return "BC9008";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "8-station subnet";
	}
}
