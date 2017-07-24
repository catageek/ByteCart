package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


/**
 * Factory for routing tables
 */
public final class RoutingTableFactory {

	/**
	 * Get a routing table
	 * 
	 * @param inv the inventory to open
	 * @return the RoutingTableWritable object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static public RoutingTableWritable getRoutingTable(Inventory inv, int slot) throws ClassNotFoundException, IOException, JsonSyntaxException {
		BookFile file = BookFile.getFrom(inv, slot, true, ".BookFile");
		if (file == null) {
			file = BookFile.getFrom(inv, slot, true, "RoutingTableBinary");
		}
		RoutingTableBook rt = null;
		if (file != null && ! file.isEmpty()) {
			ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
			rt = (RoutingTableBook) ois.readObject();
			rt.setInventory(inv, slot);
			return rt;
		}
	
		file = BookFile.getFrom(inv, slot, false, "RoutingTable");
		if (file == null || file.isEmpty()) {
			if (inv.getItem(slot) == null) {
				return new RoutingTableBookJSON(inv, slot);
			}
			throw new IOException("Slot " + slot + " is not empty.");
		}
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		RoutingTableBookJSON rtj = gson.fromJson(file.getPages(), RoutingTableBookJSON.class);
		rtj.setInventory(inv, slot);
		return rtj;
	}
	
	static public Boolean isRoutingTable(Inventory inv, int slot) {
		return BookFile.isBookFile(inv, slot, "RoutingTable");
	}
}
