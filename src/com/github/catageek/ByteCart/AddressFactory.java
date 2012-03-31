package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public final class AddressFactory {
	
	public static Address getAddress(Inventory inv){
		return new AddressInventory(inv);
	}

	public static Address getAddress(Block b, int line){
		return new AddressSign(b, line);
	}
	public static Address getAddress(String s){
		return new AddressString(s);
	}

}
