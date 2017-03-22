package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.ModifiableRunnable;
import com.github.catageek.ByteCartAPI.Event.UpdaterCreateEvent;

/**
 * Class implementing a listener and waiting for a player to right-click an inventory holder
 * and running a Runnable
 */
public class ByteCartInventoryListener implements Listener {

	private final Player Player;
	// the Runnable to update
	private final ModifiableRunnable<Inventory> Execute;
	// flag set when we deal with an updater command
	private final boolean isUpdater;

	public ByteCartInventoryListener(ByteCart plugin, Player player, ModifiableRunnable<Inventory> execute,
			boolean isupdater) {
		this.Player = player;
		this.Execute = execute;
		this.isUpdater = isupdater;
		// self registering as Listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler (ignoreCancelled = true)
	@SuppressWarnings("ucd")
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Entity entity;
		Inventory inv;
		if (event.getPlayer().equals(Player) && ((entity = event.getRightClicked()) instanceof InventoryHolder)) {
			// we set the member and run the Runnable
			this.Execute.SetParam(inv = ((InventoryHolder) entity).getInventory());
			this.Execute.run();
			// we cancel the right-click
			event.setCancelled(true);

			if (isUpdater) {
				// we launch an UpdaterCreateEvent
				StorageMinecart v = (StorageMinecart) inv.getHolder();
				UpdaterCreateEvent e = new UpdaterCreateEvent(v.getEntityId(), v.getLocation());
				ByteCart.myPlugin.getServer().getPluginManager().callEvent(e);
			}
		}
		// Self unregistering
		PlayerInteractEntityEvent.getHandlerList().unregister(this);
	}
}


