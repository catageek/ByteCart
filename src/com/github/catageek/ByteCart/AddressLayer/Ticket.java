package com.github.catageek.ByteCart.AddressLayer;

import java.io.IOException;
import java.util.ListIterator;

import org.bukkit.Bukkit;
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

final class Ticket {

	private final BookProperties properties;

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

	private final static boolean isTicket(ItemStack stack) {
		if (stack != null && stack.getType().equals(Material.WRITTEN_BOOK) && stack.hasItemMeta()) {
			String bookauthor = ((BookMeta) stack.getItemMeta()).getAuthor();
			if (bookauthor.equals(ByteCart.myPlugin.getConfig().getString("author")))
				return true;
		}
		return false;
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
	
	static int getEmptyOrBookAndQuillSlot(Player player) {
		int slot;
		if ((slot = getEmptyOrBookAndQuillSlot(player.getInventory())) == -1) {
			String msg = "Error: No space in inventory.";
			player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + msg);
		}
		return slot;
	}
	
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

	static void createTicket(Inventory inv, int slot) {
		
		if (slot == -1)
			return;
		
		ItemStack stack = getBookStack(ByteCart.myPlugin.getConfig().getString("author"),
				ByteCart.myPlugin.getConfig().getString("title"));

		// swap with an existing book if needed
		int existingticket = Ticket.getTicketslot(inv);
		if (existingticket != -1 && existingticket != slot) {
			inv.setItem(slot, inv.getItem(existingticket));
			slot = existingticket;
		}

		inv.setItem(slot, stack);
	}

	private static ItemStack getBookStack(String author, String title) {
		ItemStack stack;
		/*
		 * Here we create a ticket in slot, replacing empty book if needed
		 */
		BookMeta book;

		book = (BookMeta) Bukkit.getServer().getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
		book.setAuthor(author);
		book.setTitle(title);
		stack = new ItemStack(Material.WRITTEN_BOOK);
		stack.setItemMeta(book);
		return stack;
	}


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

	InventoryHolder getTicketHolder() {
		return properties.getFile().getContainer().getHolder();
	}
	
	final boolean setEntry(Parameter parameter, String value) {
		try {
			properties.setProperty(parameter.getName(), value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	final String getString(Parameter parameter, String defaultvalue) {
		return properties.getString(parameter.getName(), defaultvalue);
	}

	final String getString(Parameter parameter) {
		return properties.getString(parameter.getName());
	}

	void appendTitle(String name, String s) {
		StringBuilder build = new StringBuilder(ByteCart.myPlugin.getConfig().getString("title"));
		if (name != null)
			build.append(" ").append(name);
		build.append(" (").append(s).append(")");
		try {
			properties.getFile().setDescription(build.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	final int getInt(Parameter parameter, int defaultvalue) {
		return properties.getInt(parameter.getName(), defaultvalue);
	}
	
	void resetValue(Parameter parameter, String defaultvalue) {
		try {
			properties.setProperty(parameter.getName(), defaultvalue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void remove(Parameter parameter) {
		try {
			properties.clearProperty(parameter.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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

