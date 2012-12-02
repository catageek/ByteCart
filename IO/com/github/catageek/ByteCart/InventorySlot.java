package com.github.catageek.ByteCart;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySlot implements Registry {
	
	private Inventory Inventory;
	private int Index;
	
	public InventorySlot(Inventory inv, int index) {
		this.Inventory = inv;
		this.Index = index;
		
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Inventory slot #" + index);
*/
	}

	@Override
	public int length() {
		return 6;
	}

	@Override
	public boolean getBit(int index) {
		int temp = this.getAmount() >> (length() - index - 1);
		
		if ((temp & 1) == 0)
			return false;
		return true;
	}

	@Override
	public int getAmount() {
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Inventoryslot.getAmount() : Inventory " + this.Inventory.toString());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Inventoryslot.getAmount() : Index " + this.Index);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Inventoryslot.getAmount() : item " + this.Inventory.getItem(this.Index));
*/		
		ItemStack stack = this.Inventory.getItem(this.Index);
		
		if (stack != null) // if slot is not not empty
			// we get value modulo 64, so stack of 64 equals 0
			return stack.getAmount() % ( 1 << this.length());
		else
			return 0;
	}

	@Override
	public void setBit(int index, boolean value) {
		ItemStack stack = this.Inventory.getItem(this.Index);
		int dest = stack.getAmount();
		
		// if stack is empty, use cobble
		if(dest == 0)
			stack.setType(Material.COBBLESTONE);
		
		// compute final value
		if(value)
			dest |= 1 << (length() - index - 1);
		else
			dest &= ~(1 << (length() - index - 1));
		
		// copy value to stack object
		stack.setAmount(dest);
		
		// update inventory
		this.Inventory.setItem(this.Index, stack);
				
	}

	@Override
	public void setAmount(int amount) {

		ItemStack stack = this.Inventory.getItem(this.Index);
		int dest = stack.getAmount();
		
		// if stack is empty, use cobble
		if(dest == 0)
			stack.setType(Material.COBBLESTONE);
		
		// copy value to stack object. empty stack not permitted
		if (amount % (1 << this.length()) == 0)  // if amount modulo 64 = 0
			stack.setAmount(1 << this.length()); // we put 64 items
		else
			stack.setAmount(amount % ( 1 << this.length())); // or amount modulo 64
		
		// update inventory
		this.Inventory.setItem(this.Index, stack);
				

	}

}
