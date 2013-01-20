package com.github.catageek.ByteCart.IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bukkit.inventory.meta.BookMeta;

public final class BookOutputStream extends OutputStream {

	private final BookMeta BookMeta;
	private final ByteArrayOutputStream Baos = new ByteArrayOutputStream(256);
	private final int Page;

	public BookOutputStream(BookMeta bookmeta, int page) {
		super();
		BookMeta = bookmeta;
		Page = page;
	}


	@Override
	public void write(byte[] cbuf, int off, int len) throws IOException {
		Baos.reset();
		Baos.write(cbuf, off, len);
		Baos.flush();
	}
	
	@Override
	public void flush() throws IOException {
		Baos.flush();
		BookMeta.setPage(Page, this.Baos.toString());
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

	@Override
	public void write(int b) throws IOException {
		Baos.write(b);
	}

}
