package com.github.catageek.ByteCart.IO;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.bukkit.inventory.meta.BookMeta;


final class BookWriter extends Writer {

	public BookWriter(org.bukkit.inventory.meta.BookMeta bookMeta, int page) {
		super();
		BookMeta = bookMeta;
		Page = page;
	}
	private final BookMeta BookMeta;
	private StringWriter stringwriter = new StringWriter(256);
	private final int Page;
	
	@Override
	public void close() throws IOException {
	}
	
	@Override
	public void flush() throws IOException {
		stringwriter.flush();
		BookMeta.setPage(Page, this.stringwriter.toString());
	}
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		stringwriter = new StringWriter(256);
		stringwriter.write(cbuf, off, len);
		stringwriter.flush();
	}

	public final BookMeta getBookMeta() {
		try {
			flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BookMeta;
	}

}
