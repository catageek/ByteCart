package com.github.catageek.ByteCart.FileStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.Base64;

class InventoryOutputStream extends ByteArrayOutputStream {

	private final Inventory inventory;
	private final boolean binary;
	private final String name;
	private final String title;
	
	InventoryOutputStream(Inventory inventory, boolean binary, String title, String name) {
		this.inventory = inventory;
		this.binary = binary;
		this.name = name;
		this.title = title;
	}

	/**
	 * Get the content as a string
	 *
	 * @return the content of the book
	 */
	private String getEncodedString() {
		return binary ?  Base64.encodeToString(buf, false) : this.toString();
	}
	
	/**
	 * Get the underlying OutputStream
	 * 
	 * @param slot the slot where the book is
	 * @return the book outputstream
	 * @throws IOException
	 */
	private void writeData(byte[] data, int slot) throws IOException {
		final BookFile book = BookFile.create(inventory, slot, false, name);
		book.setDescription(this.title);
		OutputStream outputstream = book.getOutputStream();
		outputstream.write(data);
		outputstream.flush();
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		
		if (this.size() == 0)
			return;

		StringBuilder sb = new StringBuilder(getEncodedString());

		int len= sb.length();
		int i, j = 1;

		// number of books to write
		int count = 1 + (len - 1) / BookFile.MAXSIZE;

		// Throw if too many books are needed
		if (count > this.inventory.getSize()) {
			if (ByteCart.debug)
				ByteCart.log.info(count + " books are needed, maximum is " + this.inventory.getSize());
			throw new IOException();
		}

		String mystring;
		// loop for full books
		count -= 1;
		for (i = 0; i < count; i++) {
			mystring = sb.substring(i * BookFile.MAXSIZE, j * BookFile.MAXSIZE);
			writeData(mystring.getBytes(), i);
			j++;
		}

		// last page
		mystring = sb.substring(i * BookFile.MAXSIZE);
		writeData(mystring.getBytes(), i++);
		
		// Clear last slots
		for(; i < this.inventory.getSize(); i++) {
			this.inventory.clear(i);
		}
		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Flushing " + len + " bytes of data to books");
	}
}
