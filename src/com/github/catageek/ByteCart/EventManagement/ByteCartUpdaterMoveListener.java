package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Event.UpdaterMoveEvent;

public class ByteCartUpdaterMoveListener implements Listener {
	
	private static boolean exist = false;

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

		if (ByteCart.myPlugin.getUm().isUpdater(event.getVehicle().getEntityId())) {
			Bukkit.getServer().getPluginManager().callEvent((Event) new UpdaterMoveEvent(event));
			return;
		}
		
		if (ByteCart.myPlugin.getUm().getMapRoutes().isEmpty()) {
			HandlerList.unregisterAll(this);
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
		ByteCartUpdaterMoveListener.exist = exist;
	}

}
