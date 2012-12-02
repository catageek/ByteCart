package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class ByteCartInventoryListener implements Listener {

	private final Address Address;
	private final Player Player;

	ByteCartInventoryListener(ByteCart plugin, Player player, Address address) {
		this.Address = address;
		this.Player = player;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		
		if (! event.getPlayer().equals(Player) || !(event.getInventory().getHolder() instanceof StorageMinecart)) {
			PlayerInteractEvent.getHandlerList().unregister(this);
			return;
		}

		Inventory inventory = event.getInventory();
		
		if ((new BC7011(event.getPlayer().getLocation().getBlock(), ((StorageMinecart) inventory.getHolder()))).setAddress(Address)) {
			Player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress"));
			Player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.GetTTL") + AddressFactory.<AddressRouted>getAddress(inventory).getTTL().getAmount());
		}
		else
			Player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );


		event.setCancelled(true);

		InventoryOpenEvent.getHandlerList().unregister(this);
	}
}


