package com.github.catageek.ByteCart;

public class Subnet8 extends Station implements TriggeredIC {

	public Subnet8(org.bukkit.block.Block block, org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 3;
	}

}
