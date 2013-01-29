package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;


public class BC9008 extends AbstractBC9000 implements BCSign,HasNetmask, Triggable {

	public BC9008(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 1;
		this.Name = "BC9008";
		this.FriendlyName = "8-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
