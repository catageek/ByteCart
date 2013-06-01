package com.github.catageek.ByteCart.Signs;

final class BC9032 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9032(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 3;
	}

	@Override
	public final String getName() {
		return "BC9032";
	}

	@Override
	public final String getFriendlyName() {
		return "32-station subnet";
	}
}

