package com.github.catageek.ByteCart.FileStorage;

import java.io.IOException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;

/**
 * An outputstream for a book in an ItemStack. Write operations in the book update the ItemStack object 
 */
final class ItemStackMetaOutputStream extends ItemStackOutputStream {
	
	private final BookOutputStream OutputStream;
	private boolean isClosed = false;


	/**
	 * @param stack the stack containing the book
	 * @param outputstream an output stream for the book
	 */
	ItemStackMetaOutputStream(ItemStack stack, BookOutputStream outputstream) {
		super(stack);
		OutputStream = outputstream;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] cbuf, int off, int len) throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		OutputStream.write(cbuf, off, len);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		OutputStream.flush();
		getItemStack().setItemMeta(OutputStream.getBook());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Flushing meta to itemstack");
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Closing itemstack");
		OutputStream.close();
		isClosed  = true;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		if (isClosed)
			throw new IOException("ItemStack has been already closed");
		OutputStream.write(b);		
	}

	/**
	 * Get the current buffer
	 *
	 * @return the buffer
	 */
	final byte[] getBuffer() {
		return OutputStream.getBuffer();
	}
	
	/**
	 * Get the book
	 *
	 * @return the book
	 */
	final BookMeta getBook() {
		return OutputStream.getBook();
	}
}
