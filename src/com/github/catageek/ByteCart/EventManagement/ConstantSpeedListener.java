package com.github.catageek.ByteCart.EventManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * Listener to maintain cart speed
 */
public final class ConstantSpeedListener implements Listener {

	// We keep the speed of each cart in this map
	private final Map<Integer, Double> speedmap = new HashMap<Integer, Double>();
	
	// empty Location
	private Location location = new Location(null, 0, 0, 0);

	@EventHandler(ignoreCancelled = true)
	@SuppressWarnings("ucd")
	public void onVehicleMove(VehicleMoveEvent event) {
		final Vehicle v = event.getVehicle();

		if (! (v instanceof Minecart))
			return;

		final Minecart m = (Minecart) v;
		double speed = MathUtil.getSpeed(m);
		int id = m.getEntityId();
		
		final BlockData data = m.getLocation(location).getBlock().getState().getBlockData();
		
		if (speed != 0 && (data instanceof Rail)) {
			Double storedspeed;
			if (! speedmap.containsKey(id))
				speedmap.put(id, speed);
			else
				if ((storedspeed = speedmap.get(id)) > speed
						&& storedspeed <= m.getMaxSpeed())
					MathUtil.setSpeed(m, storedspeed);
				else
					speedmap.put(id, speed);
		} else
			speedmap.remove(id);
	}

	@EventHandler (ignoreCancelled = false, priority = EventPriority.MONITOR)
	@SuppressWarnings("ucd")
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		speedmap.remove(event.getVehicle().getEntityId());
	}

	@EventHandler (ignoreCancelled = false, priority = EventPriority.MONITOR)
	@SuppressWarnings("ucd")
	public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
		final List<Entity> passengers = event.getVehicle().getPassengers();
		for(Entity passenger : passengers) {
			if(passenger.getEntityId() == event.getEntity().getEntityId()) {
				return;
			}
		}
		speedmap.remove(event.getVehicle().getEntityId());
	}

	@EventHandler (ignoreCancelled = false, priority = EventPriority.MONITOR)
	@SuppressWarnings("ucd")
	public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
		speedmap.remove(event.getVehicle().getEntityId());
	}



}
