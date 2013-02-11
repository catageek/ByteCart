package com.github.catageek.ByteCart.Routing;

import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class ReturnAddressFactory {

	@SuppressWarnings("unchecked")
	public static <T extends Returnable> T getAddress(Inventory inv){
		if (inv.contains(Material.BOOK_AND_QUILL)) {
			ListIterator<? extends ItemStack> it = inv.iterator();

			while (it.hasNext()) {
				ItemStack stack = it.next();
				try {

					if (stack.getTypeId() == Material.BOOK_AND_QUILL.getId())
						return (T) new ReturnAddressBook(inv, it.previousIndex());
				} catch (NullPointerException e) {
				}
			}
		}
		return (T) new ReturnAddressInventory(inv);
	}
}
