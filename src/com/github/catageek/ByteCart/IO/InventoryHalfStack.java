package com.github.catageek.ByteCart.IO;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.catageek.ByteCart.ByteCart;

public class InventoryHalfStack implements Inventory {
	
	private final static int materialid = ByteCart.myPlugin.getConfig().getInt("material.id");


	public InventoryHalfStack(org.bukkit.inventory.Inventory inventory) {
		super();
		Inventory = inventory;
	}

	private final Inventory Inventory;

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... arg0){
		return Inventory.addItem(arg0);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(int arg0) {
		return Inventory.all(arg0);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material arg0) {
		return Inventory.all(arg0);
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack arg0) {
		return Inventory.all(arg0);
	}

	@Override
	public void clear() {
		Inventory.clear();

	}

	@Override
	public void clear(int arg0) {
		ItemStack item = this.Inventory.getItem(arg0 >> 1);
		if ((arg0 & 1) == 0)
			this.Inventory.setItem(arg0 >> 1,InventoryHalfStack.setLower(item, 0));
		else
			this.Inventory.setItem(arg0 >> 1, InventoryHalfStack.setHigher(item, 0));
		if (this.Inventory.getItem(arg0 >> 1).getAmount() ==0)
			this.Inventory.clear(arg0 >> 1);
	}

	@Override
	public boolean contains(int arg0) {
		return Inventory.contains(arg0);
	}

	@Override
	public boolean contains(Material arg0) {
		return Inventory.contains(arg0);
	}

	@Override
	public boolean contains(ItemStack arg0) {
		return Inventory.contains(arg0);
	}

	@Override
	public boolean contains(int arg0, int arg1) {
		return Inventory.contains(arg0, arg1);
	}

	@Override
	public boolean contains(Material arg0, int arg1) {
		return Inventory.contains(arg0, arg1);
	}

	@Override
	public boolean contains(ItemStack arg0, int arg1) {
		return Inventory.contains(arg0, arg1);
	}

	@Override
	public int first(int arg0) {
		return Inventory.first(arg0);
	}

	@Override
	public int first(Material arg0) {
		return Inventory.first(arg0);
	}

	@Override
	public int first(ItemStack arg0) {
		return Inventory.first(arg0);
	}

	@Override
	public int firstEmpty() {
		ItemStack[] item = Inventory.getContents();
		for (int i = 0; i< item.length; i++) {
			if (item[i] == null || (item[i].getAmount() & 7) == 0)
				return 2*i;
			if ((item[i].getAmount() >> 3) == 0)
				return 2*i+1;
		}
		return -1;
	}

	@Override
	public ItemStack[] getContents() {
		ItemStack[] old = Inventory.getContents();
		int size = old.length;
		ItemStack[] nouveau = new ItemStack[size << 1];
		for (int i =0; i < size; i++) {
			if (old[i] == null)
				continue;
			nouveau[i << 1] = InventoryHalfStack.getLower(old[i]);
			nouveau[(i << 1) +1] = InventoryHalfStack.getHigher(old[i]);
		}
		return nouveau;
	}

	@Override
	public InventoryHolder getHolder() {
		return Inventory.getHolder();
	}

	@Override
	public ItemStack getItem(int arg0) {
		return ((arg0 & 1) == 0 ? InventoryHalfStack.getLower(Inventory.getItem(arg0 >> 1)): InventoryHalfStack.getHigher(Inventory.getItem(arg0 >> 1)));
	}

	@Override
	public int getMaxStackSize() {
		return 7;
	}

	@Override
	public String getName() {
		return Inventory.getName();
	}

	@Override
	public int getSize() {
		return Inventory.getSize()*2;
	}

	@Override
	public String getTitle() {
		return Inventory.getTitle();
	}

	@Override
	public InventoryType getType() {
		return Inventory.getType();
	}

	@Override
	public List<HumanEntity> getViewers() {
		return Inventory.getViewers();
	}

	@Override
	public ListIterator<ItemStack> iterator() {
		return new InventoryHalfStackIterator(Inventory);
	}

	@Override
	public ListIterator<ItemStack> iterator(int arg0) {
		return Inventory.iterator(arg0);
	}

	@Override
	public void remove(int arg0) {
		Inventory.remove(arg0);

	}

	@Override
	public void remove(Material arg0) {
		Inventory.remove(arg0);

	}

	@Override
	public void remove(ItemStack arg0) {
		Inventory.remove(arg0);

	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... arg0) {
		return Inventory.removeItem(arg0);
	}

	@Override
	public void setContents(ItemStack[] arg0) {
		Inventory.setContents(arg0);

	}

	@Override
	public void setItem(int arg0, ItemStack arg1) {
		if ((arg0 & 1) == 0)
			Inventory.setItem(arg0 >> 1, InventoryHalfStack.setLower(Inventory.getItem(arg0 >> 1), (arg1.getAmount() > getMaxStackSize() ? getMaxStackSize() : arg1.getAmount())));
		else
			Inventory.setItem(arg0 >> 1, InventoryHalfStack.setHigher(Inventory.getItem(arg0 >> 1), (arg1.getAmount() > getMaxStackSize() ? getMaxStackSize() : arg1.getAmount())));
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : InventoryDouble: Inventory content:\n" + this.toString());

	}

	@Override
	public void setMaxStackSize(int arg0) {
		Inventory.setMaxStackSize(arg0);
	}

	private final static ItemStack getLower(ItemStack item) {
		if (item != null) {
			ItemStack ret = new ItemStack(item.getTypeId(), item.getAmount() & 7);
			return (ret.getAmount() == 0 ? null : ret);
		}
		return null;
	}

	private final static ItemStack getHigher(ItemStack item) {
		if (item != null) {
			ItemStack ret = new ItemStack(item.getTypeId(), item.getAmount() >> 3);
			return (ret.getAmount() == 0 ? null : ret);
		}
		return null;
	}

	private final static ItemStack setLower(ItemStack item, int amount) {
		int oldamount = (item != null ? item.getAmount(): 0);
		int oldtype = (item != null ? item.getTypeId(): InventoryHalfStack.materialid);
		return new ItemStack(oldtype, (oldamount & 56) | (amount & 7));
	}

	private final static ItemStack setHigher(ItemStack item, int amount) {
		int oldamount = (item != null ? item.getAmount(): 0);
		int oldtype = (item != null ? item.getTypeId(): InventoryHalfStack.materialid);
		return new ItemStack(oldtype, (oldamount & 7) | (amount << 3));
	}

	@Override
	public final String toString() {
		ItemStack[] item = Inventory.getContents();
		String s = "";
		for (int i = 0; i< item.length; i++) {
			if (item[i] != null)
				s += "slot[" + i + "] = " + item[i].getAmount() + "\n";
		}
		return s;
	}

	@Override
	public boolean containsAtLeast(ItemStack arg0, int arg1) {
		return Inventory.containsAtLeast(arg0, arg1);
	}

	private final class InventoryHalfStackIterator implements ListIterator<ItemStack> {

		protected InventoryHalfStackIterator(Inventory inv) {
			this.it = inv.iterator();
		}

		private final ListIterator<ItemStack> it;
		private boolean odd = false;
		private ItemStack item;

		@Override
		public boolean hasNext() {
			if (odd)
				return true;
			return it.hasNext();
		}

		@Override
		public ItemStack next() {
			odd ^= true;
			if (! odd)
				return InventoryHalfStack.getHigher(item);
			item = it.next();
			return InventoryHalfStack.getLower(item);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasPrevious() {
			throw new UnsupportedOperationException();
		}

		@Override
		public ItemStack previous() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int nextIndex() {
			if (odd)
				return it.nextIndex()*2-1;
			return it.nextIndex()*2;
		}

		@Override
		public int previousIndex() {
			if(! odd) {
				if (it.previousIndex() >=0 )
					return (it.previousIndex() << 1) + 1;
				else
					return -1;
			}
			if (it.previousIndex() >= 0)
				return (it.previousIndex() << 1);
			return 0;
		}

		@Override
		public void set(ItemStack e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(ItemStack e) {
			throw new UnsupportedOperationException();
		}
	}



}
