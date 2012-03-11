package com.github.catageek.ByteCart;

public class Subnet2 extends Station implements TriggeredIC {

	public Subnet2(org.bukkit.block.Block block,
			org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 5;
	}

}
