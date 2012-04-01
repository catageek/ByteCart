package com.github.catageek.ByteCart;

public final class RoutingTableInventory implements RoutingTable {
	
	private final org.bukkit.inventory.Inventory Inventory;
	
	public RoutingTableInventory(org.bukkit.inventory.Inventory inv) {
		this.Inventory = inv;
	}

	@Override
	public Registry getDirection(int entry) {
		Registry reg = new InventorySlot(this.Inventory, entry);
		return new SubRegistry(reg, 2, 0);
	}

	@Override
	public Registry getDistance(int entry) {
		Registry reg = new InventorySlot(this.Inventory, entry);
		return new SubRegistry(reg, 4, 2);
	}

	/**
	 * @return the inventory
	 */
	public org.bukkit.inventory.Inventory getInventory() {
		return Inventory;
	}

}
