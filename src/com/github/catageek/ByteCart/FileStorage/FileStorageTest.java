package com.github.catageek.ByteCart.FileStorage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;

public final class FileStorageTest {
	private final Inventory inventory;
	private final static Random rng = new Random();
	private final static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public FileStorageTest(Inventory inv) {
		this.inventory = inv;
	}
	
	static String generateString(int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	public void runTest() {
		run(1);
		run(512);
		run(32767);
		run(128000);
		int len = inventory.getSize() * BookFile.MAXSIZE;
		run(len);
		run(len + 1);
	}
	
	public void run(int len) {
		inventory.clear();
		ByteCart.log.info("isInventoryFile() ? empty inventory : " + (InventoryFile.isInventoryFile(inventory, "test") ? "NOK" : "OK"));
		InventoryFile file1 = new InventoryFile(inventory, false, "test");
		ByteCart.log.info("isEmpty() ? empty inventory : " + (file1.isEmpty() ? "OK" : "NOK"));
		OutputStream outputstream = null;
		String string1 = generateString(len);
		try {
			outputstream = file1.getOutputStream();
			outputstream.write(string1.getBytes());
			outputstream.flush();
			outputstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		ByteCart.log.info("isEmpty() ? test inventory : " + (file1.isEmpty() ? "NOK" : "OK"));
		ByteCart.log.info("isInventoryFile() ? test inventory : " + (InventoryFile.isInventoryFile(inventory, "test") ? "OK" : "NOK"));
		ByteCart.log.info("isInventoryFile() ? wrong name : " + (InventoryFile.isInventoryFile(inventory, "othertest") ? "NOK" : "OK"));
		InventoryFile file2 = new InventoryFile(inventory, false, "test");
		ByteCart.log.info("isEmpty() ? test2 inventory : " + (file2.isEmpty() ? "NOK" : "OK"));
		String string2 = file2.getPages();
		for (int i = 0; i < len; i += BookFile.MAXSIZE) {
			ByteCart.log.info("Test of string of " + len + " bytes : " + i + " " + (string2.regionMatches(i, string1, i, Math.min(BookFile.MAXSIZE, len - i * BookFile.MAXSIZE)) ? "OK" : "NOK"));
		}
	}
}
