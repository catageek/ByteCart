package com.github.catageek.ByteCart.Signs;


final class BC9004 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	BC9004(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 6;
	}

	@Override
	public final String getName() {
		return "BC9004";
	}

	@Override
	public final String getFriendlyName() {
		return "4-station subnet";
	}
}
