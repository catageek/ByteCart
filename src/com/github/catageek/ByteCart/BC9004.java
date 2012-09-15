package com.github.catageek.ByteCart;

public class BC9004 extends BC9016 implements TriggeredIC {

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
