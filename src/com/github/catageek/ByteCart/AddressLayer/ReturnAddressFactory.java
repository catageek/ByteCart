package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.FileStorage.BookProperties.Conf;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;

/**
 * Factory class to create a return address from various supports
 */
public final class ReturnAddressFactory {

	/**
	 * Creates a return address from a ticket
	 *
	 * @param inv the inventory containing the ticket
	 * @return the return address
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Ticket.getTicketslot(inv)) != -1)
			return (T) new ReturnAddressBook(new Ticket(BookFile.getFrom(inv, slot, false, ByteCart.myPlugin.getConfig().getString("author")), Conf.NETWORK), Parameter.RETURN);
		return null;
	}
}
