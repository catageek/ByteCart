package com.github.catageek.ByteCart.IO;

import java.io.IOException;
import org.bukkit.inventory.ItemStack;

import com.github.catageek.ByteCart.ByteCart;


final class ItemStackMetaWriter extends ItemStackWriter {

	public ItemStackMetaWriter(ItemStack stack, BookWriter writer) {
		super(stack);
		this.writer = writer;
	}

	private final BookWriter writer;
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : ItemStackMetaWriter before");
		writer.write(cbuf, off, len);
		getItemStack().setItemMeta(writer.getBookMeta());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : ItemStackMetaWriter after");
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
		getItemStack().setItemMeta(writer.getBookMeta());
	}

	@Override
	public void close() throws IOException {
	}



}
