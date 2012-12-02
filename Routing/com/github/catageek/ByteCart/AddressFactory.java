package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

public final class AddressFactory {
	
	@SuppressWarnings("unchecked")
	public static <T extends Address> T getAddress(Inventory inv){
		return (T) new AddressInventory(inv);
	}
	
	public static Address getAddress(Block b, int line){
		return new AddressSign(b, line);
	}
	public static Address getAddress(String s){
		return new AddressString(s);
	}

}
