package com.github.catageek.ByteCart.FileStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;


class BookOutputStream extends ByteArrayOutputStream {
	
	private final BookMeta book;

	private boolean isClosed = false;

	BookOutputStream(BookMeta book) {
		super(book.getPageCount() * BookFile.PAGESIZE);
		this.book = book;
	}

	@Override
	public void write (byte[] bytes, int off, int len) {
//		this.reset();
//		if(ByteCart.debug)
//			ByteCart.log.info("ByteCart : empty data cache buffer");

		super.write(bytes, off, len);
	}

	@Override
	public void write(byte[] bytes) throws IOException {
		if (isClosed)
			throw new IOException("Book has been already closed");
		this.write(bytes, 0, bytes.length);
	}
	
	protected byte[] getBuffer() {
		return this.toByteArray();
	}

	@Override
	public void flush() throws IOException {
		
		if (isClosed)
			throw new IOException("Book has been already closed");
		
		if (this.size() == 0)
			return;

		StringBuilder sb = new StringBuilder(getEncodedString());

		int len= sb.length();
		int i, j = 1;


		// number of pages to write less 1
		int count = len / BookFile.PAGESIZE;

		String[] strings = new String[count+1];

		// loop for full pages
		for (i = 0; i < count; i++) {
			strings[i] = sb.substring(i << BookFile.PAGELOG, j << BookFile.PAGELOG);
			j++;
		}

		// last page
		strings[count] = sb.substring(i << BookFile.PAGELOG);

		this.book.setPages(strings);
		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Flushing " + len + " bytes of data to meta");
	}

	protected String getEncodedString() {
		return this.toString();
	}
	
	@Override
	public void close() throws IOException {
		if (isClosed)
			throw new IOException("Book has been already closed");
		isClosed  = true;
	}
	
	final BookMeta getBook() {
		return book;
	}
}

