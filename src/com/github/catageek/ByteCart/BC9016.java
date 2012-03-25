package com.github.catageek.ByteCart;

public class BC9016 extends BC9001 implements TriggeredIC {


	public BC9016(org.bukkit.block.Block block, org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 0;
		this.Name = "BC9016";
		this.FriendlyName = "16-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
