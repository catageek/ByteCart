package com.github.catageek.ByteCart.IO;

import java.io.IOException;
import org.bukkit.inventory.ItemStack;

import com.github.catageek.ByteCart.ByteCart;


final class InventoryItemStackWriter extends java.io.Writer {

	private org.bukkit.inventory.Inventory Inventory;
	private int Index;
	private ItemStackWriter writer;

	public InventoryItemStackWriter(org.bukkit.inventory.Inventory inventory, int index, ItemStackWriter writer) {
		super();
		Inventory = inventory;
		Index = index;
		this.writer = writer;
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
		Inventory.setItem(Index, this.getItemStack());
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : InventoryItemStackWriter before");
		writer.write(arg0, arg1, arg2);
		writer.flush();
		Inventory.setItem(Index, this.getItemStack());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : InventoryItemStackWriter after, index = " + Index);
	}

	private ItemStack getItemStack() {
		return writer.getItemStack();
	}


}
