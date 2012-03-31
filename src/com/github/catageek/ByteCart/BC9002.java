package com.github.catageek.ByteCart;

public class BC9002 extends BC9001 implements TriggeredIC {

	public BC9002(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 3;
		this.Name = "BC9002";
		this.FriendlyName = "2-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
