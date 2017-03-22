package com.github.catageek.ByteCart.FileStorage;

import java.io.ByteArrayInputStream;

import org.bukkit.ChatColor;
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
		super(readPagesAsBytes(book, binary));
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
	public static String readPages(BookMeta book, boolean binary) {
		String sb = getRawPages(book);
		if (binary)
			return new String(Base64.decodeFast(sb));
		return sb;
	}

	/**
	 * Copy all pages of a book in a array of bytes
	 *
	 * @param book the book
	 * @param binary binary mode
	 * @return the array of bytes
	 */
	private static byte[] readPagesAsBytes(BookMeta book, boolean binary) {
		String sb = getRawPages(book);
		if (binary)
			return Base64.decodeFast(sb);
		return sb.getBytes();
	}

	private static String getRawPages(BookMeta book) {
		int len =  book.getPageCount() * BookFile.PAGESIZE;
		StringBuilder sb = new StringBuilder(len);

		for (int i = 1; i <= book.getPageCount(); ++i) {
			sb.append(ChatColor.stripColor(book.getPage(i)));
		}

		sb.trimToSize();
		return sb.toString();
	}
}