package com.github.catageek.ByteCart.Routing;

import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.Util.Book;

public final class ReturnAddressFactory {

	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Book.getTicketslot(inv)) != -1)
			return (T) new ReturnAddressBook(inv, slot);
		return (T) new ReturnAddressInventory(inv);
	}
}
