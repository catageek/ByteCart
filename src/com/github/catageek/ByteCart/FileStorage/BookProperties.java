package com.github.catageek.ByteCart.FileStorage;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import com.github.catageek.ByteCart.ByteCart;

public final class BookProperties implements Closeable, Flushable {

	private final Properties Properties = new Properties();
	private final Conf PageNumber;
	private final BCFile file;
	private boolean isClosed = false;
	private OutputStream os = null;

	public enum Conf {
		NETWORK("Network"),
		BILLING("Billing"),
		ACCESS("Access"),
		PROTECTION("Protection"),
		HISTORY("History");

		private final String name;

		Conf(String name) {
			this.name = name;
		}
	}
	
	public BookProperties(BCFile file, Conf page) {
		super();
		this.file = file;
		try {
			Properties.load(file.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PageNumber = page;
	}

	public void setProperty(String key, String value) throws IOException {
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: BookProperties : setting key " + key + " to " + value);
		Properties.setProperty(key, value);
	}

	private void save() throws IOException {
		file.clear();
		os = file.getOutputStream();
		Properties.store(os, PageNumber.name);
	}

	public void clearProperty(String key) throws IOException {
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: BookProperties : clearing key " + key);
		Properties.remove(key);
	}

	public String getString(String key) {
		return Properties.getProperty(key);
	}

	public String getString(String key, String defaultvalue) {
		return Properties.getProperty(key, defaultvalue);
	}

	public int getInt(String key) {
		return Integer.parseInt(Properties.getProperty(key));
	}

	public int getInt(String key, int defaultvalue) {
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: property string : "+ Properties.getProperty(key, ""+defaultvalue));
		return Integer.parseInt(Properties.getProperty(key, ""+defaultvalue));

	}

	@Override
	public void flush() throws IOException {
		if(isClosed)
			throw new IOException("Property file has been already closed");
		save();
	}

	@Override
	public void close() throws IOException {
		if(isClosed)
			throw new IOException("Property file has been already closed");
		file.close();
		isClosed = true;
	}

	public final BCFile getFile() {
		return file;
	}


}
