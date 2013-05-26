package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.FileStorage.BookFile;


public final class RoutingTableFactory {

	static public RoutingTable getRoutingTable(Inventory inv) throws IOException, ClassNotFoundException {
		RoutingTableBook rt;
		
		// If upgrading from ByteCart 1.x, cleaning routing table
		if (! inv.contains(Material.WRITTEN_BOOK))
			inv.clear();
		
		try (BookFile file = new BookFile(inv, 0, true, "RoutingTable")) {
			if (file.isEmpty())
				return new RoutingTableBook(inv);
			ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
			rt = (RoutingTableBook) ois.readObject();
		}
		rt.setInventory(inv);
		return rt;
	}

}
