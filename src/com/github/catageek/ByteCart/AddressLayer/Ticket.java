package com.github.catageek.ByteCart.AddressLayer;

import java.io.IOException;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.FileStorage.BookProperties;
import com.github.catageek.ByteCart.FileStorage.BookProperties.Conf;

/**
 * Implement a ticket
 */
final class Ticket {

	/**
	 * Internal storage of the ticket
	 */
	private final BookProperties properties;

	/**
	 * Create a ticket using a book at a specific page
	 * 
	 * @param bookfile the book FS to use
	 * @param network the name of the page
	 */
	Ticket(BookFile bookfile, Conf network) {
		properties = new BookProperties(bookfile, network);
	}

	/**
	 * Get a slot containing a ticket
	 *
	 *
	 * @param inv The inventory to search in
	 * @return a slot number, or -1
	 */
	static int getTicketslot(Inventory inv) {
		if (inv.contains(Material.WRITTEN_BOOK)) {

			// priority given to book in hand
			if (inv.getHolder() instanceof Player) {
				Player player = (Player) inv.getHolder();
				if (isTicket(player.getItemInHand())) {
					return player.getInventory().getHeldItemSlot();
				}
			}

			ListIterator<ItemStack> it = inv.iterator();

			while (it.hasNext()) {
				if (isTicket(it.next()))
					return it.previousIndex();
			}
		}
		return -1;
	}

	/**
	 * Tell if a stack is a ticket
	 *
	 * @param stack the stack to check
	 * @return true if it is a ticket
	 */
	private final static boolean isTicket(ItemStack stack) {
		return BookFile.isBookFile(stack, "ticket");
	}

	/**
	 * Get a slot containing an empty book or nothing
	 *
	 *
	 * @param inv The inventory to search in
	 * @return a slot number, or -1
	 */
	static int getEmptyOrBookAndQuillSlot(Inventory inv) {

		ItemStack stack;

		if (ByteCart.myPlugin.getConfig().getBoolean("mustProvideBooks")
				&& inv.contains(Material.BOOK_AND_QUILL)) {

			// priority given to book in hand
			if (inv.getHolder() instanceof Player) {
				Player player = (Player) inv.getHolder();
				if (isEmptyBook(stack = player.getItemInHand())) {
					return player.getInventory().getHeldItemSlot();
				}
			}



			ListIterator<? extends ItemStack> it = inv.iterator();

			while (it.hasNext()) {
				stack = it.next();

				if (isEmptyBook(stack)) {
					int slot = it.previousIndex();
					// found a book
					return slot;
				}
			}
		}

		// no book found or user must provide one, return empty slot
		int slot = inv.firstEmpty();

		if (slot != -1) {
			return slot;
		}
		return -1;
	}
	
	/**
	 * Get a slot containing an empty book or nothing
	 *
	 *
	 * @param player The player having the inventory to search in
	 * @return a slot number, or -1
	 */
	static int getEmptyOrBookAndQuillSlot(Player player) {
		int slot;
		if ((slot = getEmptyOrBookAndQuillSlot(player.getInventory())) == -1) {
			String msg = "Error: No space in inventory.";
			player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + msg);
		}
		return slot;
	}
	
	/**
	 * Get a slot containing an empty book or nothing
	 *
	 *
	 * @param inv The inventory to search in
	 * @return a slot number, or -1
	 */
	static int searchSlot(Inventory inv) {
		int slot;
		// get a slot containing an emtpy book (or nothing)
		slot = Ticket.getEmptyOrBookAndQuillSlot(inv);

		if (slot != -1) {
			if (inv.getItem(slot) == null
					&& ByteCart.myPlugin.getConfig().getBoolean("mustProvideBooks")
					&& ByteCart.myPlugin.getConfig().getBoolean("usebooks")) {
				return -1;
			}
			return slot;
		}
		return -1;
	}

	/**
	 * Initialize a ticket in a inventory
	 *
	 * @param inv the inventory where to put the ticket
	 * @param slot the slot number where to put the ticket
	 */
	static void createTicket(Inventory inv, int slot) {
		
		if (slot == -1)
			return;
		
		// swap with an existing book if needed
		int existingticket = Ticket.getTicketslot(inv);
		if (existingticket != -1 && existingticket != slot) {
			inv.setItem(slot, inv.getItem(existingticket));
			slot = existingticket;
		}


		try {
			BookFile bookfile = BookFile.create(inv, slot, false, "ticket");
			bookfile.setDescription(ByteCart.myPlugin.getConfig().getString("title"));
			bookfile.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tell if a book_and_quill is empty
	 *
	 * @param stack the ItemStack to check
	 * @return true if it is an empty book_and_quill
	 */
	private static boolean isEmptyBook(ItemStack stack) {
		BookMeta book;

		if (stack != null && stack.getType().equals(Material.BOOK_AND_QUILL)) {

			if (stack.hasItemMeta()) {

				if ((book = (BookMeta)stack.getItemMeta()).hasPages()
						&& (book.getPage(1).isEmpty()))
					return true;
			}
			else {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get the holder of the ticket
	 *
	 * @return the holder
	 */
	InventoryHolder getTicketHolder() {
		return properties.getFile().getContainer().getHolder();
	}
	
	/**
	 * Set a parameter value in the ticket
	 * 
	 * <p>{@link Ticket#close()} must be called to actually perform the operation</p>
	 *
	 * @param parameter parameter to set
	 * @param value value to set
	 * @return true
	 */
	final boolean setEntry(Parameter parameter, String value) {
		try {
			properties.setProperty(parameter.getName(), value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Return the value of a parameter or a default value
	 *
	 * @param parameter the parameter to return
	 * @param defaultvalue the default value
	 * @return a string containing the parameter value, or the default value
	 */
	final String getString(Parameter parameter, String defaultvalue) {
		return properties.getString(parameter.getName(), defaultvalue);
	}

	/**
	 * Return the value of a parameter
	 *
	 * @param parameter the parameter to return
	 * @return a string containing the parameter value
	 */
	final String getString(Parameter parameter) {
		return properties.getString(parameter.getName());
	}

	/**
	 * Append a name and a string to the title
	 * 
	 * <p>{@link Ticket#close()} must be called to actually perform the operation</p>
	 * 
	 * <p>The appended value is " name (string)"</p>
	 *
	 * @param name
	 * @param s
	 */
	void appendTitle(String name, String s) {
		StringBuilder build = new StringBuilder(ByteCart.myPlugin.getConfig().getString("title"));
		if (name != null)
			build.append(" ").append(name);
		build.append(" ").append(s);
		try {
			properties.getFile().setDescription(build.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a parameter value as an integer or a default value
	 *
	 * @param parameter the parameter to get
	 * @param defaultvalue the default value
	 * @return the parameter value
	 */
	final int getInt(Parameter parameter, int defaultvalue) {
		return properties.getInt(parameter.getName(), defaultvalue);
	}
	
	/**
	 * Reset the parameter to default value
	 * 
	 * <p>{@link Ticket#close()} must be called to actually perform the operation</p>
	 *
	 * @param parameter the parameter to set
	 * @param defaultvalue the value to set
	 */
	void resetValue(Parameter parameter, String defaultvalue) {
		try {
			properties.setProperty(parameter.getName(), defaultvalue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove a parameter
	 * 
	 * <p>{@link Ticket#close()} must be called to actually perform the operation</p>
	 *
	 * @param parameter parameter to remove
	 */
	void remove(Parameter parameter) {
		try {
			properties.clearProperty(parameter.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Write the parameters and close the ticket
	 *
	 */
	void close() {
		try {
			properties.flush();
			properties.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

