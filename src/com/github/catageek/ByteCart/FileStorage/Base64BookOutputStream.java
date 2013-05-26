package com.github.catageek.ByteCart.FileStorage;

import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.Util.Base64;

final class Base64BookOutputStream extends BookOutputStream {

	public Base64BookOutputStream(BookMeta book) {
		super(book);
	}

	@Override
	protected String getEncodedString() {
		return Base64.encodeToString(buf, false);
	}
	
	@Override
	protected byte[] getBuffer() {
		return Base64.encodeToByte(buf, false);
	}
}
