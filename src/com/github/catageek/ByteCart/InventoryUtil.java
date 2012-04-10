package com.github.catageek.ByteCart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.inventory.ItemStack;

public final class InventoryUtil {

	
	/*
	 * Put all items together to fill each stack at maximum
	 */
	public static ItemStack[] compress(ItemStack[] stacks) {

		if(stacks == null)
			return null;

		List<ItemStack> ret = new ArrayList<ItemStack>();

		List<ItemStack> arg = Arrays.asList(stacks);


		for(ListIterator<ItemStack> it = arg.listIterator(); it.hasNext(); ) {

			ItemStack src = it.next();


			if (src != null) {
				ListIterator<ItemStack> jt = ret.listIterator();
				
				while( src.getAmount() > 0 && jt.hasNext()) {


					ItemStack dest = jt.next();

					if (dest.getTypeId() == src.getTypeId() && dest.getAmount() <= dest.getMaxStackSize()) {
						int exchange = Math.min(dest.getMaxStackSize() - dest.getAmount(), src.getAmount());
						dest.setAmount(dest.getAmount() + exchange);
						src.setAmount(src.getAmount() - exchange);
					}


				}

				// we add the remainder
				if(src.getAmount() > 0)
					ret.add(src);
			}

		}


		return ret.toArray(new ItemStack[0]);

	}


}
