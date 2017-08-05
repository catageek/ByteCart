package com.github.catageek.ByteCart.plugins;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import com.github.catageek.ByteCart.Util.LogUtil;
import com.github.catageek.ByteCartAPI.Event.UpdaterCreateEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterMoveEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterRemoveEvent;

public final class BCWandererTracker implements Listener,CommandExecutor {
	private Map<Integer,Location> locations = new HashMap<>();
	
	@EventHandler
	public void onUpdaterCreate(UpdaterCreateEvent event) {
		locations.put(event.getVehicleId(), event.getLocation());
	}

	@EventHandler
	public void onUpdaterMove(UpdaterMoveEvent event) {
		VehicleMoveEvent e = event.getEvent();
		locations.put(e.getVehicle().getEntityId(), e.getTo());
	}

	@EventHandler
	public void onUpdaterRemove(UpdaterRemoveEvent event) {
		locations.remove(event.getVehicleId());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (locations.isEmpty()) {
			LogUtil.sendSuccess(sender, "No updaters found");
			return true;
		}
		sender.sendMessage("List of locations of updaters:");
		for (Location loc : locations.values()) {
			StringBuilder s = new StringBuilder();
			s.append("World: ").append(loc.getWorld().getName()).append(" ");
			s.append("X: ").append(loc.getBlockX()).append(" ");
			s.append("Y: ").append(loc.getBlockY()).append(" ");
			s.append("Z: ").append(loc.getBlockZ());
			LogUtil.sendSuccess(sender, s.toString());
		}
		return true;
	}
}
