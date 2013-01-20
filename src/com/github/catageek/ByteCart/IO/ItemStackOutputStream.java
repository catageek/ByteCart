package com.github.catageek.ByteCart.IO;

import java.io.OutputStream;
import org.bukkit.inventory.ItemStack;

public abstract class ItemStackOutputStream extends OutputStream {
	
	public ItemStackOutputStream(org.bukkit.inventory.ItemStack itemStack) {
		super();
		ItemStack = itemStack;
	}

	private final ItemStack ItemStack;

	final ItemStack getItemStack() {
		return ItemStack;
	}
}
