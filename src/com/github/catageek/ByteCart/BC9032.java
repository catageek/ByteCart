package com.github.catageek.ByteCart;

public class BC9032 extends BC9001 implements TriggeredIC {

	public BC9032(org.bukkit.block.Block block,
			org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 1;
		this.Name = "BC9032";
		this.FriendlyName = "32-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
