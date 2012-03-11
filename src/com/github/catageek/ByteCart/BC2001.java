package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;


// reads a slot by its index
public final class BC2001 extends AbstractIC implements TriggeredIC {

	final private Inventory Inventory;

	
	public BC2001(Block block, Inventory inv) {
		super(block);
		this.Inventory = inv;		
	}

	@Override
	public void trigger() {
		// copy input to output
		try {
			// Input[0] is the slot index to read
			InventorySlot slot = new InventorySlot(this.Inventory,this.getInput(0).getAmount());
			
			// Output[0] is the content of the slot
			this.getOutput(0).setAmount(slot.getAmount());
		}
		catch (NullPointerException e) {
			
		}

	}

}
