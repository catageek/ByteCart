package com.github.catageek.ByteCart;

public final class RoutingTableInventory implements RoutingTable {
	
	private final org.bukkit.inventory.Inventory Inventory;
	
	public RoutingTableInventory(org.bukkit.inventory.Inventory inv) {
		this.Inventory = inv;
	}

	@Override
	public Registry getDirection(int entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Registry getDistance(int entry) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the inventory
	 */
	public org.bukkit.inventory.Inventory getInventory() {
		return Inventory;
	}

}
