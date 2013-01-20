package com.github.catageek.ByteCart.IO;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.bukkit.inventory.meta.BookMeta;

public final class BookReader extends Reader {

	public BookReader(BookMeta book, int page) {
		super();
		Book = book;
		this.Reader = new StringReader(Book.getPage(page));
		try {
			Reader.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final BookMeta Book;
	private final Reader Reader;

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return Reader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
	}
	
	@Override
	public void reset() throws IOException {
		Reader.reset();
	}

}
