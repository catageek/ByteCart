package com.github.catageek.ByteCart.Signs;


public class BC9008 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	public BC9008(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 1;
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
