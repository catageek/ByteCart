package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.FileStorage.BookProperties.Conf;

/**
 * Factory to create address using various supports
 */
public class AddressFactory {

	/**
	 * Creates an address with a ticket as support
	 *
	 * @param inv the inventory containing the ticket
	 * @return the address
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Ticket.getTicketslot(inv)) != -1)
			return (T) new AddressBook(new Ticket(new BookFile(inv, slot, false, "ticket"), Conf.NETWORK), Parameter.DESTINATION);
		return null;
	}

	/**
	 * Creates an address with a sign as support
	 *
	 * @param b the sign block
	 * @param line the line number
	 * @return the address
	 */
	public final static Address getAddress(Block b, int line){
		return new AddressSign(b, line);
	}
	
	/**
	 * Creates an address with a string as internal support
	 *
	 * @param s the address in the form aa.bb.cc
	 * @return the address
	 */
	public final static Address getAddress(String s){
		return new AddressString(s);
	}
}
