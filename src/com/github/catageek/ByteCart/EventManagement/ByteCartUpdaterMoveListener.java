package com.github.catageek.ByteCart.EventManagement;

import java.io.IOException;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.Updaters.UpdaterContentFactory;
import com.github.catageek.ByteCartAPI.Event.UpdaterMoveEvent;

/**
 * Launch an event when an updater moves
 * This listener unregisters itself automatically if there is no updater
 */
public class ByteCartUpdaterMoveListener implements Listener {

	// flag for singleton
	private static boolean exist = false;

	// A map with ephemeral elements and timers
	private static UpdaterSet updaterset = new UpdaterSet();

	@EventHandler(ignoreCancelled = true)
	public void onVehicleMoveEvent(VehicleMoveEvent event) {

		Location loc = event.getFrom();
		Integer from_x = loc.getBlockX();
		Integer from_z = loc.getBlockZ();
		loc = event.getTo();
		int to_x = loc.getBlockX();
		int to_z = loc.getBlockZ();


		// Check if the vehicle crosses a cube boundary
		if(from_x == to_x && from_z == to_z)
			return;	// no boundary crossed, resumed

		Vehicle v;
		if (updaterset.isUpdater((v = event.getVehicle()).getEntityId())) {
			Bukkit.getServer().getPluginManager().callEvent((Event) new UpdaterMoveEvent(event));
			// reset the timer
			if (v instanceof InventoryHolder) {
				Inventory inv = ((InventoryHolder) v).getInventory();
				try {
					long duration = UpdaterContentFactory.getUpdaterContent(inv).getExpirationTime()
							- Calendar.getInstance().getTimeInMillis();
					if (duration < 1000)
						updaterset.getMap().reset(duration/50, v.getEntityId());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return;
		}

		if (updaterset.getMap().isEmpty()) {
			HandlerList.unregisterAll(this);
			updaterset = null;
			setExist(false);
		}
	}

	/**
	 * @return the exist
	 */
	public static boolean isExist() {
		return exist;
	}

	/**
	 * @param exist the exist to set
	 */
	public static void setExist(boolean exist) {
		if (! isExist() && exist)
			updaterset = new UpdaterSet();
		ByteCartUpdaterMoveListener.exist = exist;
	}

	/**
	 * Add a vehicle id in the updater map
	 *
	 * @param id the vehicle id
	 */
	public static final void addUpdater(int id) {
		updaterset.getMap().add(id);
	}
}
