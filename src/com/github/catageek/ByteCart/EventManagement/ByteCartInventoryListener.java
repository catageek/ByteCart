package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.ModifiableRunnable;
import com.github.catageek.ByteCart.Event.UpdaterCreateEvent;

public class ByteCartInventoryListener implements Listener {

	private final Player Player;
	private final ModifiableRunnable<Inventory> Execute;

	public ByteCartInventoryListener(ByteCart plugin, Player player, ModifiableRunnable<Inventory> execute) {
		this.Player = player;
		this.Execute = execute;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		
		if (! event.getPlayer().equals(Player) || !(event.getInventory().getHolder() instanceof Vehicle)) {
			PlayerInteractEvent.getHandlerList().unregister(this);
			return;
		}

		this.Execute.SetParam(event.getInventory());
		this.Execute.run();

		event.setCancelled(true);
		
		Vehicle v = (Vehicle) event.getInventory().getHolder();
		
		UpdaterCreateEvent e = new UpdaterCreateEvent(v.getEntityId(), v.getLocation());
		ByteCart.myPlugin.getServer().getPluginManager().callEvent(e);

		InventoryOpenEvent.getHandlerList().unregister(this);
	}
}


