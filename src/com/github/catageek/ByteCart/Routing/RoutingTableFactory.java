package com.github.catageek.ByteCart.Routing;

import org.bukkit.inventory.Inventory;


public final class RoutingTableFactory {
	
	static public RoutingTable getRoutingTable(Inventory inv) {
		return new RoutingTableInventory(inv);
	}

}
