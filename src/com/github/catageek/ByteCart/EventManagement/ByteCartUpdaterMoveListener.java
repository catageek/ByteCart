package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import com.github.catageek.ByteCart.Event.UpdaterMoveEvent;

public class ByteCartUpdaterMoveListener implements Listener {
	
	private static boolean exist = false;
	
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

		int id;
		if (updaterset.isUpdater(id = event.getVehicle().getEntityId())) {
			Bukkit.getServer().getPluginManager().callEvent((Event) new UpdaterMoveEvent(event));
			updaterset.getMapRoutes().reset(id);
			return;
		}
		
		if (updaterset.getMapRoutes().isEmpty()) {
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

	public static final void addUpdater(int id) {
		updaterset.getMapRoutes().add(id);
	}
}
