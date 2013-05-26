package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.FileStorage.BookProperties.Conf;

public class AddressFactory {

	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Ticket.getTicketslot(inv)) != -1)
			return (T) new AddressBook(new Ticket(new BookFile(inv, slot, false, "ticket"), Conf.NETWORK), Parameter.DESTINATION);
		return null;
	}

	public final static Address getAddress(Block b, int line){
		return new AddressSign(b, line);
	}
	public final static Address getAddress(String s){
		return new AddressString(s);
	}
}
