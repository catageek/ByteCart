package com.github.catageek.ByteCart.FileStorage;

import java.io.IOException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;

final class ItemStackMetaOutputStream extends ItemStackOutputStream {
	
	private final BookOutputStream OutputStream;
	private boolean isClosed = false;


	ItemStackMetaOutputStream(ItemStack stack, BookOutputStream outputstream) {
		super(stack);
		OutputStream = outputstream;
	}

	@Override
	public void write(byte[] cbuf, int off, int len) throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		OutputStream.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		OutputStream.flush();
		getItemStack().setItemMeta(OutputStream.getBook());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Flushing meta to itemstack");
	}

	@Override
	public void close() throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Closing itemstack");
		OutputStream.close();
		isClosed  = true;
	}

	@Override
	public void write(int b) throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		OutputStream.write(b);		
	}

	final byte[] getBuffer() {
		return OutputStream.getBuffer();
	}
	
	final BookMeta getBook() {
		return OutputStream.getBook();
	}
}
