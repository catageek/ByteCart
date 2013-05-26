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
import com.github.catageek.ByteCart.Event.UpdaterCreateEvent;

public class ByteCartInventoryListener implements Listener {

	private final Player Player;
	private final ModifiableRunnable<Inventory> Execute;
	private final boolean isUpdater;

	public ByteCartInventoryListener(ByteCart plugin, Player player, ModifiableRunnable<Inventory> execute,
			boolean isupdater) {
		this.Player = player;
		this.Execute = execute;
		this.isUpdater = isupdater;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler (ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Entity entity;
		Inventory inv;
		if (event.getPlayer().equals(Player) && ((entity = event.getRightClicked()) instanceof InventoryHolder)) {
			this.Execute.SetParam(inv = ((InventoryHolder) entity).getInventory());
			this.Execute.run();
			event.setCancelled(true);

			if (isUpdater) {
				StorageMinecart v = (StorageMinecart) inv.getHolder();
				UpdaterCreateEvent e = new UpdaterCreateEvent(v.getEntityId(), v.getLocation());
				ByteCart.myPlugin.getServer().getPluginManager().callEvent(e);
			}
		}
		PlayerInteractEntityEvent.getHandlerList().unregister(this);
	}
}


