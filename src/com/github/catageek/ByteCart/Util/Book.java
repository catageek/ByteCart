package com.github.catageek.ByteCart.Util;

import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;

public final class Book {
	
	/**
	 * Get a slot containing a ticket
	 *
	 *
	 * @param inv The inventory to search in
	 * @return a slot number, or -1
	 */
	public static int getTicketslot(Inventory inv) {
		if (inv.contains(Material.WRITTEN_BOOK)) {
			ListIterator<? extends ItemStack> it = inv.iterator();

			while (it.hasNext()) {
				ItemStack stack = it.next();
				try {

					if (stack.getTypeId() == Material.WRITTEN_BOOK.getId() && stack.hasItemMeta()) {
						String bookauthor = ((BookMeta) stack.getItemMeta()).getAuthor();
						if (bookauthor.equals(ByteCart.myPlugin.getConfig().getString("author")))
							return it.previousIndex();
					}
				} catch (NullPointerException e) {
				}
			}
		}
		return -1;

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

		if (stack.getType().equals(Material.BOOK_AND_QUILL)) {

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
}

