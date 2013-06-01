package com.github.catageek.ByteCart.Signs;

final class BC9128  extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9128(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 1;
	}

	@Override
	public final String getName() {
		return "BC9128";
	}

	@Override
	public final String getFriendlyName() {
		return "128-station subnet";
	}
}