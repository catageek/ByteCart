package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;


public class BC9004 extends AbstractBC9000 implements Subnet,HasNetmask, Triggable {

	public BC9004(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 2;
		this.Name = "BC9004";
		this.FriendlyName = "4-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
