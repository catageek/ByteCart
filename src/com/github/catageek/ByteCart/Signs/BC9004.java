package com.github.catageek.ByteCart.Signs;


public class BC9004 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	public BC9004(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 2;
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
