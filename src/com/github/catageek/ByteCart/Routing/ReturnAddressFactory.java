package com.github.catageek.ByteCart.Routing;

import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.Util.Ticket;

public final class ReturnAddressFactory {

	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Ticket.getTicketslot(inv)) != -1)
			return (T) new ReturnAddressBook(new Ticket(inv, slot));
		return (T) new ReturnAddressInventory(inv);
	}
}
