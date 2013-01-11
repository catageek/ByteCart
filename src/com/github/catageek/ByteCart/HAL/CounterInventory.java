package com.github.catageek.ByteCart.HAL;

import java.util.ListIterator;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.IO.InventorySlot;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;

/**
 * Provides counters in each slot of the inventory
 * 
 * @author catageek
 *
 */

public final class CounterInventory {
	private final Inventory Inventory;
	private final int CounterLength;
	private final int Size;

	public CounterInventory(Inventory inventory) {
		this(inventory, inventory.getSize());
	}

	public CounterInventory(Inventory inventory, int size) {
		Inventory = inventory;
		CounterLength = MathUtil.binlog(inventory.getMaxStackSize());
		Size = size;
	}

	private Inventory getInventory() {
		return Inventory;
	}

	public int getCount(int counter) {
		return (this.getSlot(counter)).getAmount();
	}

	public void incrementCount(int counter) {
		this.incrementCount(counter, 1);
	}

	public void incrementCount(int counter, int value) {
		InventorySlot slot = getSlot(counter);
		slot.setAmount(slot.getAmount()+value > this.getInventory().getMaxStackSize() ? this.getInventory().getMaxStackSize() : slot.getAmount() + value);
	}


	public void setCount(int counter, int amount) {
		getSlot(counter).setAmount(amount > this.getInventory().getMaxStackSize() ? this.getInventory().getMaxStackSize() : amount);
	}

	public int firstEmpty() {
		int ret = this.getInventory().firstEmpty();
		return ret >= Size ? -1 : ret;
	}

	public void reset(int counter) {
		this.getSlot(counter).empty();
	}

	public void resetAll() {
		for (int i = 0; i < Size; i++)
			this.getInventory().clear(i);
	}

	public boolean isAllFull(int start, int end) {
		ListIterator<ItemStack> items = this.getInventory().iterator(start);

		if (end >= Size)
			throw new IllegalArgumentException();

		boolean ret = true;
		int maxsize = this.getInventory().getMaxStackSize();
		while (items.hasNext() && items.nextIndex() <= end) {
			ItemStack item = items.next();
			ret &= (item == null || item.getAmount() == maxsize);
			if(!ret)
				break;
		}
		return ret;
	}

	public boolean isAllFull() {
		ListIterator<ItemStack> items = this.getInventory().iterator();

		//we jump the first one
		items.next();

		boolean ret = true;
		while (items.hasNext() && items.nextIndex() < Size) {
			ItemStack item = items.next();
			ret &= (item == null || item.getAmount() == this.getInventory().getMaxStackSize());
			if(!ret)
				break;
		}
		return ret;
	}

	public int getMinimum(RoutingTable routes, DirectionRegistry from) {
		ListIterator<ItemStack> items = this.getInventory().iterator();

		//we jump the first one
		items.next();

		int min = this.getInventory().getMaxStackSize();
		int index = -1;
		while (items.hasNext() && items.nextIndex() < Size) {
			ItemStack item = items.next();
			if ((item == null || item != null && item.getAmount() < min) 
					&& routes.getDirection(items.previousIndex()) != null
					&& routes.getDirection(items.previousIndex()).getAmount() != from.getAmount()) {
				min = (item != null ? item.getAmount() : 0);
				index = items.previousIndex();
			}
		}
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : minimum found ring " + index + " with " + min);
		return index;

	}

	private InventorySlot getSlot(int counter) {
		if (counter >= Size)
			throw new IllegalArgumentException();
		return new InventorySlot(this.getInventory(), counter, this.getCounterLength());
	}

	@Override
	public final String toString() {
		ItemStack[] item = Inventory.getContents();
		String s = "";
		for (int i = 0; i< item.length; i++) {
			if (item[i] != null)
				s += "slot[" + i + "] = " + item[i].getAmount() % (1 << this.getCounterLength()) + "\n";
		}
		return s;
	}

	public int getCounterLength() {
		return CounterLength;
	}

}