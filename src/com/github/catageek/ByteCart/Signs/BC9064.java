package com.github.catageek.ByteCart.Signs;

final class BC9064  extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9064(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 2;
	}

	@Override
	public final String getName() {
		return "BC9064";
	}

	@Override
	public final String getFriendlyName() {
		return "64-station subnet";
	}
}