package com.github.catageek.ByteCart.Routing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import com.github.catageek.ByteCart.HAL.Registry;
import com.github.catageek.ByteCart.HAL.SubRegistry;
import com.github.catageek.ByteCart.IO.InventorySlot;
import com.github.catageek.ByteCart.Util.DirectionRegistry;


public final class RoutingTableInventory extends AbstractRoutingTable implements RoutingTable {

	private final org.bukkit.inventory.Inventory Inventory;

	public RoutingTableInventory(org.bukkit.inventory.Inventory inv) {
		this.Inventory = inv;
	}

	@Override
	public DirectionRegistry getDirection(int entry) {
		if (! this.isEmpty(entry)) {
			Registry reg = new InventorySlot(this.Inventory, entry);
			return new DirectionRegistry(1 << (reg.getAmount() >> 4));
		}
		return null;
	}

	@Override
	public int getDistance(int entry) {
		Registry reg = new InventorySlot(this.Inventory, entry);
		return new SubRegistry(reg, 4, 2).getAmount();
	}

	/**
	 * @return the inventory
	 */
	public org.bukkit.inventory.Inventory getInventory() {
		return Inventory;
	}

	@Override
	public int getSize() {
		return Inventory.getSize();
	}

	@Override
	public void setEntry(int entry, int direction, int distance) {
		Registry reg = new InventorySlot(this.Inventory, entry);
		reg.setAmount(direction+distance);
	}

	@Override
	public boolean isEmpty(int entry) {
		try {
			return (Inventory.getItem(entry) == null);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return true;
	}

	public Set<Entry<Integer,Integer>> getRoutesTo(DirectionRegistry direction) {
		Map<Integer, Integer> tablemap = new HashMap<Integer, Integer>();

		for(int i = 0; i< this.getSize(); i++) {
			if( this.getDirection(i) != null && this.getDirection(i).getAmount() == direction.getAmount())
				tablemap.put(i, this.getDistance(i));
		}
		return tablemap.entrySet();
	}

	@Override
	public void clear() {
		ItemStack zero = this.Inventory.getItem(0);
		this.Inventory.clear();
		this.Inventory.setItem(0, zero);
	}

	@Override
	public void removeEntry(int entry) {
		this.Inventory.clear(entry);

	}
	
	@Override
	public Set<Entry<Integer,Integer>> getEntrySet() {
		Map<Integer, Integer> tablemap = new HashMap<Integer, Integer>();

		for(int i = 0; i< this.getSize(); i++) {
			if(this.getDirection(i) != null)
				tablemap.put(i, this.getDistance(i));
		}
		return tablemap.entrySet();
	}

}
