package com.github.catageek.ByteCart;

public class BC9008 extends BC9001 implements TriggeredIC {

	public BC9008(org.bukkit.block.Block block, org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 1;
		this.Name = "BC9008";
		this.FriendlyName = "8-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
