package com.github.catageek.ByteCart;

public class Subnet16 extends Station implements TriggeredIC {


	public Subnet16(org.bukkit.block.Block block, org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 2;
	}

}
