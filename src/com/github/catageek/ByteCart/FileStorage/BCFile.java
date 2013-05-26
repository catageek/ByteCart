package com.github.catageek.ByteCart.FileStorage;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.inventory.Inventory;

public interface BCFile extends Closeable, Flushable {

	int getCapacity();

	void clear();

	boolean isEmpty();
	
	OutputStream getOutputStream() throws IOException;
	InputStream getInputStream() throws IOException;
	Inventory getContainer();
	void setDescription(String s) throws IOException;
	String getDescription() throws IOException;
}
