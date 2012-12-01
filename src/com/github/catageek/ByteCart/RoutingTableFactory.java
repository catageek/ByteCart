package com.github.catageek.ByteCart;

import org.bukkit.inventory.Inventory;


public final class RoutingTableFactory {
	
	static public RoutingTable getRoutingTable(Inventory inv) {
		return new RoutingTableInventory(inv);
	}

}
