package com.github.catageek.ByteCart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryWriter {

	private final Set<Integer> Written = new HashSet<Integer>();
	private Inventory OriginalInventory;
	private boolean success = true;

	private final Map<Integer, Integer> typemap = new HashMap<Integer, Integer>();

	public InventoryWriter(Inventory inv) {
		this.setOriginalInventory(inv);
	}

	private class ItemStackComparator<E extends ItemStack> implements Comparator<E> {

		@Override
		public int compare(E o1, E o2) {
			return ((Integer) o2.getAmount()).compareTo((Integer) o1.getAmount());
		}

	}

	public void Write(int value, int pos) {

		Inventory Inventory = Bukkit.createInventory(null, this.getInventory().getSize());
		Inventory.setContents(this.getInventory().getContents());

		ItemStack[] Stacks = Inventory.getContents().clone();

		Set<ItemStack> stackset;

		Map<Integer, Set<ItemStack>> Map = new TreeMap<Integer, Set<ItemStack>>(new InventoryWriterComparator<Integer>(value, typemap));

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Writing value " + value + " to slot #" + pos);

		for (int i = 0; i < Stacks.length; i++) {
			ItemStack stack = Stacks[i];

/*			if(ByteCart.debug && stack != null) {
				ByteCart.log.info("ByteCart: stack = " + stack + "********************");
				ByteCart.log.info("typemap contains key ? "+typemap.containsKey(stack.getTypeId()));
				ByteCart.log.info("Written ? "+this.Written(i));

			}
*/

			if(stack != null && ! typemap.containsKey(stack.getTypeId()) && ! this.Written(i)) {
				stackset = new TreeSet<ItemStack>(new ItemStackComparator<ItemStack>());
				stackset.add(stack);
				typemap.put(stack.getTypeId(), stack.getAmount());

/*				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: map put = " + stack.getTypeId() + ":" + typemap.get(stack.getTypeId()));
*/
				for(int j = i + 1; j < Stacks.length; j++) {
					ItemStack otherstack = Stacks[j];
					if (otherstack != null && stack.getTypeId() == otherstack.getTypeId() && !this.Written(j)) {
						stackset.add(otherstack);
/*						if(ByteCart.debug)
							ByteCart.log.info("ByteCart: map put = " + stack.getTypeId() + ":" + (typemap.get(stack.getTypeId()) + otherstack.getAmount()));
*/						typemap.put(stack.getTypeId(),typemap.get(stack.getTypeId()) + otherstack.getAmount());
					}
				}

				Map.put(stack.getTypeId(), stackset);
			}

		}

/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: map length = " + Map.size());
*/
		ArrayList<Set<ItemStack>> mylist = new ArrayList<Set<ItemStack>>(Map.values());

		int target = -1;

		for (ListIterator<Set<ItemStack>> it = mylist.listIterator(); (target == -1) && it.hasNext();) {
			/*			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: index = " + it.nextIndex());
			 */			
			target = this.reorganize(Inventory, it.next(), value);
		}

		typemap.clear();

		if (target == -1) {
			this.setSuccess(false);
			return;
		}

/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: stack build at pos " + target);
*/
		swap(Inventory, pos, target);

		this.setOriginalInventory(Inventory);

		this.Written.add(pos);

	}

	/**
	 * @return the originalInventory
	 */
	public Inventory getInventory() {
		return OriginalInventory;
	}

	/**
	 * @param originalInventory the originalInventory to set
	 */
	private void setOriginalInventory(Inventory originalInventory) {
		OriginalInventory = originalInventory;
	}

	public InventoryWriter setWritten(int pos) {
		this.Written.add(pos);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 	Declaring as Written slot #" +  pos);
*/		return this;
	}

	private final void swap(Inventory inv, int pos1, int pos2) {
		ItemStack temp = inv.getItem(pos1);
		inv.setItem(pos1, inv.getItem(pos2));
		inv.setItem(pos2, temp);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 	swapping slots #" +  pos1 + " <=>  #" + pos2);
*/
	}

	private int getTotal(Set<ItemStack> set) {
		int sum = this.typemap.get(this.getType(set));
		return sum;
	}

	private boolean isEligible(Set<ItemStack> set, int value) {
		int sum = this.getTotal(set);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 	isEligible Set = " + this.getType(set) + ":" + this.getTotal(set) + " : " + (sum >= value && this.getMaxSize(set) >= value));
*/		return (sum >= value && this.getMaxSize(set) >= value);
	}

	private void remove(Inventory inv, Set<ItemStack> set) {
		/*		for (ListIterator<ItemStack> jt = (new ArrayList<ItemStack>(set)).listIterator(); jt.hasNext();) {
			inv.remove(jt.next());
		}
		 */		
		int settype = this.getType(set);
		for (ListIterator<ItemStack> it = inv.iterator(); it.hasNext();) {
			int index = it.nextIndex();
			ItemStack stack = it.next();

/*			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: 	remove() #" + index + " ?");
*/
			if (! this.Written(index) && stack !=null && stack.getTypeId() == settype) {
				inv.clear(index);
			}
		}
	}

	private int getType(Set<ItemStack> set) {
		ListIterator<ItemStack> jt = (new ArrayList<ItemStack>(set)).listIterator();
		if (jt.hasNext())
			return jt.next().getTypeId();
		return 0;
	}

	private int reorganize(Inventory inv, Set<ItemStack> set, int value) {

		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: reorganize Set = " + this.getType(set) + ":" + this.getTotal(set));
		 */
		if ( ! this.isEligible(set, value ))
			return -1;
		else {
			int sum = this.getTotal(set);
			int type = this.getType(set);
			int maxsize = this.getMaxSize(set);
			this.remove(inv, set);

/*			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: 	RemoveItem = " + type + ":" + sum );
*/
			int retour = inv.firstEmpty();

			if (retour == -1)
				return -1;

			inv.setItem(retour,new ItemStack(type,value));
/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: 	SetItem = " + type + ":" + value );
*/			
			sum -= value;
			while (sum >0) {
				if (sum <= maxsize) {
					inv.setItem(inv.firstEmpty(),new ItemStack(type, sum));
/*					if(ByteCart.debug)
						ByteCart.log.info("ByteCart: 	SetItem = " + type + ":" + sum );
*/					sum = 0;
				}
				else {
					inv.setItem(inv.firstEmpty(),new ItemStack(type, maxsize));
/*					if(ByteCart.debug)
						ByteCart.log.info("ByteCart: 	SetItem = " + type + ":" + maxsize );
*/					sum -= maxsize;
				}
			}
			return retour;
		}
	}

	private int getMaxSize(Set<ItemStack> set) {
		ListIterator<ItemStack> jt = (new ArrayList<ItemStack>(set)).listIterator();
		if (jt.hasNext())
			return jt.next().getMaxStackSize();
		return 0;
	}

	private boolean Written(int slot) {
		return this.Written.contains(slot);
	}



	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	private void setSuccess(boolean success) {
		this.success = success;
	}
}
