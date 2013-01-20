package com.github.catageek.ByteCart.IO;

import java.io.IOException;
import java.io.OutputStream;
import org.bukkit.inventory.ItemStack;

import com.github.catageek.ByteCart.ByteCart;

public final class InventoryItemStackOutputStream extends OutputStream {
	
	public InventoryItemStackOutputStream(org.bukkit.inventory.Inventory inventory, int index, ItemStackOutputStream outputstream) {
		super();
		Inventory = inventory;
		Index = index;
		OutputStream = outputstream;
	}

	private final int Index;
	private final org.bukkit.inventory.Inventory Inventory;
	private final ItemStackOutputStream OutputStream;

	@Override
	public void write(byte[] cbuf, int off, int len) throws IOException {
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : InventoryItemStackWriter before");
		OutputStream.write(cbuf, off, len);
		OutputStream.flush();
		Inventory.setItem(Index, this.getItemStack());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : InventoryItemStackWriter after, index = " + Index);
	}

	@Override
	public void flush() throws IOException {
		OutputStream.flush();
		Inventory.setItem(Index, this.getItemStack());
	}

	@Override
	public void close() throws IOException {
	}
	
	private ItemStack getItemStack() {
		return OutputStream.getItemStack();
	}

	@Override
	public void write(int b) throws IOException {
		OutputStream.write(b);
	}

}
