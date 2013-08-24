package com.github.catageek.ByteCart.FileStorage;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.Util.Base64;

/**
 * An input stream to read from book
 */
class BookInputStream extends ByteArrayInputStream {

	/**
	 * @param book the book
	 * @param binary set binary mode
	 */
	BookInputStream(BookMeta book, boolean binary) {
		super(readPages(book, binary));
	}

	/**
	 * @param outputstream the output stream from where we read data
	 */
	BookInputStream(ItemStackMetaOutputStream outputstream) {
		super(outputstream.getBuffer());
	}

	/**
	 * Copy all pages of a book in a array of bytes
	 *
	 * @param book the book
	 * @param binary binary mode
	 * @return the array of bytes
	 */
	private static byte[] readPages(BookMeta book, boolean binary) {
		int len =  book.getPageCount() << BookFile.PAGELOG;
		StringBuilder sb = new StringBuilder(len);

		Iterator<String> it = book.getPages().iterator();

		while (it.hasNext())
			sb.append(it.next());

		sb.trimToSize();
		if (binary)
			return Base64.decodeFast(sb.toString());
		return sb.toString().getBytes();
	}
}