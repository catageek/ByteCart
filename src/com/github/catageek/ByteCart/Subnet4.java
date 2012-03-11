package com.github.catageek.ByteCart;

public class Subnet4 extends Station implements TriggeredIC {

	public Subnet4(org.bukkit.block.Block block,
			org.bukkit.inventory.Inventory inv) {
		super(block, inv);
		this.netmask = 4;
	}

}
