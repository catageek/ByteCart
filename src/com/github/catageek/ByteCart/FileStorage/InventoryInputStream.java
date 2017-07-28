package com.github.catageek.ByteCart.FileStorage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.Base64;

final class InventoryInputStream extends ByteArrayInputStream {
	/**
	 * @param book the book
	 * @param binary set binary mode
	 */
	InventoryInputStream(Inventory inv, boolean binary, String name) {
		super(readPagesAsBytes(inv, name, binary));
	}

	public InventoryInputStream(InventoryOutputStream outputstream) {
		super(outputstream.toByteArray());
	}

	public static String readPages(Inventory inv, String name, boolean binary) {
		String sb = getRawContent(inv, name);
		if (binary)
			return new String(Base64.decodeFast(sb));
		return sb;
	}
	
	static boolean isInventoryInputStream(Inventory inv, String name) {
		for (int i = 0; i < inv.getSize(); ++i) {
			if (BookFile.isBookFile(inv, i, name)) {
				return true;
			}
		}
		return false;
	};

	/**
	 * Get the underlying inputstream
	 * 
	 * @param slot the slot where the book is
	 * @return the book inputstream
	 * @throws IOException
	 */
	private static BookInputStream getBookInputStream(Inventory inventory, int slot, String name) {
		BookFile book = BookFile.getFrom(inventory, slot, false, name);
		if (book == null) {
			return null;
		}
		BookInputStream ret = null;
		try {
			ret  = book.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static byte[] readPagesAsBytes(Inventory inv, String name, boolean binary) {
		String sb = getRawContent(inv, name);
		if (binary)
			return Base64.decodeFast(sb);
		return sb.getBytes();
	}

	private static String getRawContent(Inventory inv, String name) {
		int bookcount = inv.all(Material.WRITTEN_BOOK).size();
		int len =  bookcount * BookFile.MAXSIZE;
		StringBuilder sb = new StringBuilder(len);

		for (int i = 0; i < bookcount; ++i) {
			final BookInputStream bookInputStream = getBookInputStream(inv, i, name);
			if (bookInputStream != null) {
				sb.append(bookInputStream.getBuffer());
			}
		}

		sb.trimToSize();
		return sb.toString();
	}

}
