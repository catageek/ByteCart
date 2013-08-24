package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.ByteCart;

/**
 * Factory to create or get a ticket
 */
public final class TicketFactory {

	/**
	 * Put a ticket in a player's inventory if necessary
	 *
	 * @param player the player
	 * @param forcereuse must be true to force the reuse of existing ticket
	 */
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

	/**
	 * Put a ticket in an inventory, if necessary. The inventory is not updated.
	 *
	 * @param inv the inventory where to put the ticket
	 */
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
	
	/**
	 * Remove a ticket from an inventory
	 *
	 * @param inv
	 */
	@SuppressWarnings("deprecation")
	public static final void removeTickets(Inventory inv) {
		int slot;
		while ((slot = Ticket.getTicketslot(inv)) != -1) {
			inv.clear(slot);
		}
		InventoryHolder holder;
		if ((holder = inv.getHolder()) instanceof Player)
			((Player) holder).updateInventory();			
	}
}
