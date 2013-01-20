package com.github.catageek.ByteCart.IO;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.catageek.ByteCart.ByteCart;

public final class BookProperties {

	private final Properties Properties = new Properties();
	private final int PageNumber = 1, Index;
	private Reader Reader = null;
	private OutputStream OutputStream = null;
	private final Inventory Inventory;

	public BookProperties(Inventory inventory, int index) {
		super();
		Inventory = inventory;
		Index = index;
		ItemStack stack = inventory.getItem(index);

		ItemMeta meta = stack.hasItemMeta() ? stack.getItemMeta() : null;

		try {
			BookOutputStream bookoutputstream = new BookOutputStream((BookMeta) meta, PageNumber);
			ItemStackOutputStream stackoutputstream = new ItemStackMetaWriter(stack, bookoutputstream);
			InventoryItemStackOutputStream inventoryoutputstream = new InventoryItemStackOutputStream(inventory, index, stackoutputstream);
			OutputStream = new BufferedOutputStream(inventoryoutputstream, 256);

		} catch (NullPointerException e) {
		}
	}

	public void setProperty(String key, String value) {
		try {
			readPrepare();
			Properties.setProperty(key, value);
			Properties.store(OutputStream, null);
			OutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearProperty(String key) {
		try {
			readPrepare();
			Properties.remove(key);
			Properties.store(OutputStream, null);
			OutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getString(String key) {
		try {
			readPrepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Properties.getProperty(key);
	}

	private void readPrepare() throws IOException {
		ItemStack stack = Inventory.getItem(Index);
		ItemMeta meta = stack.hasItemMeta() ? stack.getItemMeta() : null;

		try {
			Reader = new BookReader((BookMeta) meta, PageNumber);
		} catch (NullPointerException e) {
		}
		Properties.load(Reader);
	}

	public String getString(String key, String defaultvalue) {
		try {
			readPrepare();
		} catch (IOException e) {
			return defaultvalue;
		}
		return Properties.getProperty(key, defaultvalue);
	}

	public int getInt(String key) {
		try {
			readPrepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Integer.getInteger(Properties.getProperty(key));

	}
	
	public int getInt(String key, int defaultvalue) {
		try {
			readPrepare();
		} catch (IOException e) {
			return 0;
		}
		
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: property string : "+ Properties.getProperty(key, ""+defaultvalue));
		return Integer.parseInt(Properties.getProperty(key, ""+defaultvalue));

	}


}
