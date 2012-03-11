package com.github.catageek.ByteCart;

public class Subnet32 extends Station implements TriggeredIC {

	public Subnet32(org.bukkit.block.Block block,
			org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 1;
	}

}
