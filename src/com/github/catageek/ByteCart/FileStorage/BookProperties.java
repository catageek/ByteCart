package com.github.catageek.ByteCart.FileStorage;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import com.github.catageek.ByteCart.ByteCart;

/**
 * A property file with a book as container
 */
public final class BookProperties implements Closeable, Flushable {

	private final Properties Properties = new Properties();
	private final Conf PageNumber;
	private final BCFile file;
	private boolean isClosed = false;
	private OutputStream os = null;

	/**
	 * The page names
	 */
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
	
	/**
	 * @param file the file
	 * @param page the page
	 */
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

	/**
	 * Set a property
	 *
	 * @param key the key
	 * @param value the value
	 * @throws IOException
	 */
	public void setProperty(String key, String value) throws IOException {
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: BookProperties : setting key " + key + " to " + value);
		Properties.setProperty(key, value);
	}

	/**
	 * Save the content of the property buffer to the book
	 *
	 * @throws IOException
	 */
	private void save() throws IOException {
		file.clear();
		os = file.getOutputStream();
		Properties.store(os, PageNumber.name);
	}

	/**
	 * Removes a property
	 *
	 * @param key the key to remove
	 * @throws IOException
	 */
	public void clearProperty(String key) throws IOException {
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: BookProperties : clearing key " + key);
		Properties.remove(key);
	}

	/**
	 * Get the property value
	 *
	 * @param key the property key
	 * @return the value
	 */
	public String getString(String key) {
		return Properties.getProperty(key);
	}

	/**
	 * Get the property value or a default value
	 *
	 * @param key the property key
	 * @param defaultvalue the default value
	 * @return the value, or the default value
	 */
	public String getString(String key, String defaultvalue) {
		return Properties.getProperty(key, defaultvalue);
	}

	

	/**
	 * Get a property value as an integer or a default value
	 *
	 * @param key the property key
	 * @param defaultvalue the default value
	 * @return the value
	 */
	public int getInt(String key, int defaultvalue) {
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: property string : "+ Properties.getProperty(key, ""+defaultvalue));
		return Integer.parseInt(Properties.getProperty(key, ""+defaultvalue));

	}

	/* (non-Javadoc)
	 * @see java.io.Flushable#flush()
	 */
	@Override
	public void flush() throws IOException {
		if(isClosed)
			throw new IOException("Property file has been already closed");
		save();
	}

	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if(isClosed)
			throw new IOException("Property file has been already closed");
		isClosed = true;
	}

	/**
	 * Get the container that contains this property file
	 *
	 * @return the container file
	 */
	public final BCFile getFile() {
		return file;
	}


}
