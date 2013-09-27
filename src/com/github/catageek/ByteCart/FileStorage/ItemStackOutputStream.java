package com.github.catageek.ByteCart.FileStorage;

import java.io.OutputStream;

import org.bukkit.inventory.ItemStack;

/**
 * An output stream associated with an ItemStack
 */
abstract class ItemStackOutputStream extends OutputStream {
	
	ItemStackOutputStream(org.bukkit.inventory.ItemStack itemStack) {
		super();
		ItemStack = itemStack;
	}

	private final ItemStack ItemStack;

	final ItemStack getItemStack() {
		return ItemStack;
	}
}
