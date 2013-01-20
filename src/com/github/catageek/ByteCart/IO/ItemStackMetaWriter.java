package com.github.catageek.ByteCart.IO;

import java.io.IOException;
import org.bukkit.inventory.ItemStack;
import com.github.catageek.ByteCart.ByteCart;

public final class ItemStackMetaWriter extends ItemStackOutputStream {
	
	public ItemStackMetaWriter(ItemStack stack, BookOutputStream outputstream) {
		super(stack);
		OutputStream = outputstream;
	}

	private final BookOutputStream OutputStream;
	

	@Override
	public void write(byte[] cbuf, int off, int len) throws IOException {
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : ItemStackMetaWriter before");
		OutputStream.write(cbuf, off, len);
		getItemStack().setItemMeta(OutputStream.getBookMeta());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : ItemStackMetaWriter after");
	}

	@Override
	public void flush() throws IOException {
		OutputStream.flush();
		getItemStack().setItemMeta(OutputStream.getBookMeta());
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void write(int b) throws IOException {
		OutputStream.write(b);		
	}
}
