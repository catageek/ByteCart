package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.FileStorage.BookProperties.Conf;

public final class ReturnAddressFactory {

	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Ticket.getTicketslot(inv)) != -1)
			return (T) new ReturnAddressBook(new Ticket(new BookFile(inv, slot, false), Conf.NETWORK), Parameter.RETURN);
		return null;
	}
}
