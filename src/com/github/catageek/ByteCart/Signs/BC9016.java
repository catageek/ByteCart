package com.github.catageek.ByteCart.Signs;




public class BC9016 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	public BC9016(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 0;
	}

	@Override
	public final String getName() {
		return "BC9016";
	}

	@Override
	public final String getFriendlyName() {
		return "16-station subnet";
	}
}
