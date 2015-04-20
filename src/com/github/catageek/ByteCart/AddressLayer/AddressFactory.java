package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.FileStorage.BookProperties.Conf;
import com.github.catageek.ByteCart.Signs.BC7010;
import com.github.catageek.ByteCart.Signs.BC7011;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;

/**
 * Factory to create address using various supports
 */
public class AddressFactory {

	/**
	 * Get an address with a ticket as support
	 *
	 * @param inv the inventory containing the ticket
	 * @return the address or null if there is no ticket
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getAddress(Inventory inv){
		int slot;
		if ((slot = Ticket.getTicketslot(inv)) != -1)
			return (T) new AddressBook(new Ticket(new BookFile(inv, slot, false, "ticket"), Conf.NETWORK), Parameter.DESTINATION);
		return null;
	}

	/**
	 * Creates a ticket with default address
	 *
	 * @param inv the inventory containing the ticket
	 * @return the address
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Address> T getDefaultTicket(Inventory inv){
		String destination;
		if (inv.getHolder() instanceof Player) {
			destination = ByteCart.myPlugin.getConfig().getString("PlayersNoTicketDefaultRoute", "0.0.0");
			if ((new BC7010(null,(Player)inv.getHolder())).setAddress(destination,"No ticket found !")) {
				return (T) new AddressBook(new Ticket(new BookFile(inv, Ticket.getTicketslot(inv), false, "ticket"), Conf.NETWORK), Parameter.DESTINATION);
			}
		}
		else if (inv.getHolder() instanceof Vehicle) {
			destination = ByteCart.myPlugin.getConfig().getString("EmptyCartsDefaultRoute", "0.0.0");
			if ((new BC7011(null,(Vehicle)inv.getHolder())).setAddress(destination,"No ticket found !")) {
				return (T) new AddressBook(new Ticket(new BookFile(inv, Ticket.getTicketslot(inv), false, "ticket"), Conf.NETWORK), Parameter.DESTINATION);
			}
		}
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
	 * The address is resolved.
	 *
	 * @param s the address in the form aa.bb.cc
	 * @return the address
	 */
	public final static Address getAddress(String s){
		return new AddressString(s, true);
	}

	/**
	 * Creates an address with a string as internal support
	 *
	 * The address is not resolved.
	 *
	 * @param s the address in the form aa.bb.cc
	 * @return the address
	 */
	public final static Address getUnresolvedAddress(String s){
		return new AddressString(s, false);
	}
}
