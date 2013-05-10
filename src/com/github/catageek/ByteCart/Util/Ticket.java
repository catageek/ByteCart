package com.github.catageek.ByteCart.Util;

import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;

public final class Ticket {

	private final Inventory Inventory;
	private final int Slot;
	private final BookMeta BookMeta;

	public final Inventory getInventory() {
		return Inventory;
	}

	public final int getSlot() {
		return Slot;
	}

	public final BookMeta getBookMeta() {
		return BookMeta;
	}

	/**
	 * Get a slot containing a ticket
	 *
	 *
	 * @param inv The inventory to search in
	 * @return a slot number, or -1
	 */
	public static int getTicketslot(Inventory inv) {
		if (inv.contains(Material.WRITTEN_BOOK)) {

			// priority given to book in hand
			if (inv.getHolder() instanceof Player) {
				Player player = (Player) inv.getHolder();
				if (isTicket(player.getItemInHand())) {
					return player.getInventory().getHeldItemSlot();
				}
			}

			ListIterator<? extends ItemStack> it = inv.iterator();

			while (it.hasNext()) {
				if (isTicket(it.next()))
					return it.previousIndex();
			}
		}
		return -1;
	}

	private final static boolean isTicket(ItemStack stack) {
		if (stack != null && stack.getTypeId() == Material.WRITTEN_BOOK.getId() && stack.hasItemMeta()) {
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
	public static int getEmptyOrBookAndQuillSlot(Inventory inv) {

		ItemStack stack;

		if (inv.contains(Material.BOOK_AND_QUILL)) {

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

		// no book found, return empty slot
		int slot = inv.firstEmpty();

		if (slot != -1) {
			return slot;
		}
		return -1;
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

	public Ticket(Inventory inv, int slot) {
		this.Inventory = inv;
		this.Slot = slot;
		ItemStack stack = this.getItemStack();
		this.BookMeta = (BookMeta) (stack.hasItemMeta() ? stack.getItemMeta() : Bukkit.getServer().getItemFactory().getItemMeta(Material.BOOK_AND_QUILL));
	}

	public ItemStack getItemStack() {
		return this.getInventory().getItem(getSlot());
	}

	public InventoryHolder getTicketHolder() {
		return this.getInventory().getHolder();
	}

	public void appendTitle(String name, String s) {
		StringBuilder build = new StringBuilder(ByteCart.myPlugin.getConfig().getString("title"));
		if (name != null)
			build.append(" ").append(name);
		build.append(" (").append(s).append(")");
		this.BookMeta.setTitle(build.toString());
		this.getItemStack().setItemMeta(BookMeta);
	}
}

