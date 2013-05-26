package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.ByteCart;

public final class TicketFactory {

	@SuppressWarnings("deprecation")
	public static final void getOrCreateTicket(Player player, boolean forcereuse) {
		int slot;
		Inventory inv = player.getInventory();

		if (forcereuse || ByteCart.myPlugin.getConfig().getBoolean("reusetickets", true)) {
			// if storage cart or we must reuse a existing ticket
			// check if a ticket exists and return
			// otherwise continue
			slot = Ticket.getTicketslot(inv);
			if (slot != -1)
				return;
		}

		// get a slot containing an emtpy book (or nothing)
		if ((slot = Ticket.getEmptyOrBookAndQuillSlot(player)) == -1)
			return;

		if (inv.getItem(slot) == null
				&& ByteCart.myPlugin.getConfig().getBoolean("mustProvideBooks")) {
			String msg = "No empty book in your inventory, you must provide one.";
			player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + msg);
			return;
		}

		Ticket.createTicket(inv, slot);
		player.updateInventory();
	}

	public static final void getOrCreateTicket(Inventory inv) {
		int slot;

		// if storage cart or we must reuse a existing ticket
		// check if a ticket exists and return
		// otherwise continue
		slot = Ticket.getTicketslot(inv);
		if (slot != -1)
			return;

		Ticket.createTicket(inv, Ticket.searchSlot(inv));

	}

}
