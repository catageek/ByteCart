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

	/*
	 * Map storing (type, amount) for each type contained in inventory
	 */
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
		Write(value, pos, true);
	}
	
	public void Write(int value, int pos, boolean strict) {

		// duplicate the inventory
		Inventory Inventory = Bukkit.createInventory(null, this.getInventory().getSize());
		Inventory.setContents(this.getInventory().getContents());

		// convert in array
		ItemStack[] Stacks = Inventory.getContents().clone();

		Set<ItemStack> stackset;

		/*
		 * Map storing (type, Set<ItemStack>) ordered
		 * by ascending distance to Value
		 */
		Map<Integer, Set<ItemStack>> MapStackSet = new TreeMap<Integer, Set<ItemStack>>(new InventoryWriterComparator<Integer>(value, typemap));

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

			// if the type of material was never seen in this loop
			if(stack != null && ! typemap.containsKey(stack.getTypeId()) && ! this.Written(i)) {
				// we create a set
				stackset = new TreeSet<ItemStack>(new ItemStackComparator<ItemStack>());
				// we add current stack
				stackset.add(stack);
				// we record the entry (type, qty) in map
				typemap.put(stack.getTypeId(), stack.getAmount());

				/*				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: map put = " + stack.getTypeId() + ":" + typemap.get(stack.getTypeId()));
				 */
				// we look in remaining stacks for same type
				for(int j = i + 1; j < Stacks.length; j++) {
					ItemStack otherstack = Stacks[j];
					if (otherstack != null && stack.getTypeId() == otherstack.getTypeId() && !this.Written(j)) {
						// we add matchin one to set
						stackset.add(otherstack);
						/*						if(ByteCart.debug)
							ByteCart.log.info("ByteCart: map put = " + stack.getTypeId() + ":" + (typemap.get(stack.getTypeId()) + otherstack.getAmount()));

						 */						// we update the entry (type, new qty) in map
						typemap.put(stack.getTypeId(),typemap.get(stack.getTypeId()) + otherstack.getAmount());
					}
				}
				if(! strict || this.getTotal(stackset) >= value) {
					// 	we record the set in the map
					MapStackSet.put(stack.getTypeId(), stackset);
				}
				
			}

		}

		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: map length = " + Map.size());
		 */
		/*
		 * now we have built Map and typemap
		*/
		
		int target = -1;

		for (Map.Entry<Integer, Set<ItemStack>> entry : MapStackSet.entrySet()) {
			target = this.reorganize(Inventory, entry.getValue(), value, strict);
			if (target != -1)
				break;
		}
		
		typemap.clear();

		if (target == -1) {
			this.setSuccess(false);
			return;
		}

		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: stack build at pos " + target);
		 */
		// we now put the value in the target slot
		swap(Inventory, pos, target);

		// we save our work
		this.setOriginalInventory(Inventory);

		// we declare the target slot read-only
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

	public boolean setUnwritten(int station) {
		
		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 	Declaring as Written slot #" +  pos);
		 */
		return this.Written.remove(station);
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

	private boolean isEligible(Set<ItemStack> set, int value, Inventory inv) {
		int sum = this.getTotal(set);
		int maxsize = this.getMaxSize(set);
		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 	isEligible Set = " + this.getType(set) + ":" + this.getTotal(set) + " : " + (sum >= value && this.getMaxSize(set) >= value));
		 */		
		return (maxsize >= value && ((sum - value) <= (set.size()-1)*maxsize || inv.firstEmpty() != -1));
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

	private int reorganize(Inventory inv, Set<ItemStack> set, int value, boolean strict) {
/*
				if(ByteCart.debug)
			ByteCart.log.info("ByteCart: reorganize Set = " + this.getType(set) + ":" + this.getTotal(set));
	*/	 

		// we test if this type can hold the value to write
		if ( ! this.isEligible(set, value, inv))
			return -1;
		else {
			// if eligible, we try to set the value
			int sum = this.getTotal(set);
			int type = this.getType(set);
			int maxsize = this.getMaxSize(set);


			// we clear the inventory for the type
			this.remove(inv, set);
/*
						if(ByteCart.debug)
				ByteCart.log.info("ByteCart: 	RemoveItem = " + type + ":" + sum );
*/			 


			int retour = inv.firstEmpty();

			// we put the value in first free slot
			inv.setItem(retour,new ItemStack(type,Math.min(sum,value)));
			/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: 	SetItem = " + type + ":" + value );
			 */			
			// then we put the remainder in next free slots
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
			// we return the slot where we wrote the value
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
		this.success &= success;
	}
}
