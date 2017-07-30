package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.FileStorage.InventoryFile;
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
		InventoryFile file = null;
		if (InventoryFile.isInventoryFile(inv, ".BookFile")) {
			file = new InventoryFile(inv, true, ".BookFile");
		}
		else if (InventoryFile.isInventoryFile(inv, "RoutingTableBinary")) {
			file = new InventoryFile(inv, true, "RoutingTableBinary");
		}
		RoutingTableBook rt = null;
		if (file != null && ! file.isEmpty()) {
			ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
			rt = (RoutingTableBook) ois.readObject();
			rt.setInventory(inv);
			return rt;
		}
	
		if (InventoryFile.isInventoryFile(inv, "RoutingTable")) {
			file = new InventoryFile(inv, false, "RoutingTable");
		}
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

}
