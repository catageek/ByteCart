package com.github.catageek.ByteCart.Signs;


final class BC9008 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9008(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 5;
	}

	@Override
	public final String getName() {
		return "BC9008";
	}

	@Override
	public final String getFriendlyName() {
		return "8-station subnet";
	}
}
