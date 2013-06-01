package com.github.catageek.ByteCart.FileStorage;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.Util.Base64;

class BookInputStream extends ByteArrayInputStream {

	BookInputStream(BookMeta book, boolean binary) {
		super(readPages(book, binary));
	}

	BookInputStream(ItemStackMetaOutputStream outputstream) {
		super(outputstream.getBuffer());
	}

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