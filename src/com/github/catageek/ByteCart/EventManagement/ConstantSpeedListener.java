package com.github.catageek.ByteCart.EventManagement;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public final class ConstantSpeedListener implements Listener {

	private final Map<Integer, Double> speedmap = new HashMap<Integer, Double>();
	private Location location = new Location(null, 0, 0, 0);

	@EventHandler(ignoreCancelled = true)
	public void onVehicleMove(VehicleMoveEvent event) {
		Vehicle v = event.getVehicle();

		if (! (v instanceof Minecart))
			return;

		Minecart m = (Minecart) v;
		double speed = getSpeed(m);
		int id = m.getEntityId();
		
		MaterialData data = m.getLocation(location).getBlock().getState().getData();
		
		if (speed != 0 && (data instanceof org.bukkit.material.Rails)) {
			Double storedspeed;
			if (! speedmap.containsKey(id))
				speedmap.put(id, speed);
			else
				if ((storedspeed = speedmap.get(id)) > speed)
					setSpeed(m, storedspeed);
				else
					speedmap.put(id, speed);
		} else
			speedmap.remove(id);
	}

	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		speedmap.remove(event.getVehicle().getEntityId());
	}

	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
		speedmap.remove(event.getVehicle().getEntityId());
	}

	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
		speedmap.remove(event.getVehicle().getEntityId());
	}

	private static double getSpeed(final Minecart minecart) {

		final Vector velocity = minecart.getVelocity();

		if (velocity.getX() > 0) {
			return velocity.getX();
		} else if (velocity.getX() < 0) {
			return -velocity.getX();
		} else if (velocity.getZ() > 0) {
			return velocity.getZ();
		} else if (velocity.getZ() < 0) {
			return -velocity.getZ();
		} else {
			return 0;
		}
	}

	private static void setSpeed(final Minecart minecart, final double speed) {

		final Vector velocity = minecart.getVelocity();

		if (velocity.getX() > 0) {
			velocity.setX(speed);
		} else if (velocity.getX() < 0) {
			velocity.setX(-speed);
		} else if (velocity.getZ() > 0) {
			velocity.setZ(speed);
		} else if (velocity.getZ() < 0) {
			velocity.setZ(-speed);
		}

		minecart.setVelocity(velocity);
	}


}
