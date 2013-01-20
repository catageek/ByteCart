package com.github.catageek.ByteCart.Routing;

import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class AddressFactory {

	@SuppressWarnings("unchecked")
	public static <T extends Address> T getAddress(Inventory inv){
		if (inv.contains(Material.BOOK_AND_QUILL)) {
			ListIterator<? extends ItemStack> it = inv.iterator();

			while (it.hasNext()) {
				ItemStack stack = it.next();
				try {

					if (stack.getTypeId() == Material.BOOK_AND_QUILL.getId())
						return (T) new AddressBook(inv, it.previousIndex());
				} catch (NullPointerException e) {
				}
			}
		}
		return (T) new AddressInventory(inv);
	}

	public static Address getAddress(Block b, int line){
		return new AddressSign(b, line);
	}
	public static Address getAddress(String s){
		return new AddressString(s);
	}

}
