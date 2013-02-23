package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.Util.Book;

public class AddressFactory {

	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Book.getTicketslot(inv)) != -1)
			return (T) new AddressBook(inv, slot);
		return (T) new AddressInventory(inv);
	}

	public final static Address getAddress(Block b, int line){
		return new AddressSign(b, line);
	}
	public final static Address getAddress(String s){
		return new AddressString(s);
	}
}
