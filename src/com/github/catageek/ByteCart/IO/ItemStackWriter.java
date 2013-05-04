package com.github.catageek.ByteCart.IO;

import java.io.Writer;

import org.bukkit.inventory.ItemStack;

abstract class ItemStackWriter extends Writer {

	public ItemStackWriter(org.bukkit.inventory.ItemStack itemStack) {
		super();
		ItemStack = itemStack;
	}

	private final ItemStack ItemStack;

	final ItemStack getItemStack() {
		return ItemStack;
	}
}
