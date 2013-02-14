package com.github.catageek.ByteCart.Routing;

import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;

public final class ReturnAddressFactory {

	@SuppressWarnings("unchecked")
	public static <T extends Returnable> T getAddress(Inventory inv){
		if (inv.contains(Material.WRITTEN_BOOK)) {
			ListIterator<? extends ItemStack> it = inv.iterator();

			while (it.hasNext()) {
				ItemStack stack = it.next();
				try {

					if (stack.getTypeId() == Material.WRITTEN_BOOK.getId() && stack.hasItemMeta()) {
						String bookauthor = ((BookMeta) stack.getItemMeta()).getAuthor();
						if (bookauthor == ByteCart.myPlugin.getConfig().getString("author"))
							return (T) new ReturnAddressBook(inv, it.previousIndex());
					}
				} catch (NullPointerException e) {
				}
			}
		}
		return (T) new ReturnAddressInventory(inv);
	}
}
