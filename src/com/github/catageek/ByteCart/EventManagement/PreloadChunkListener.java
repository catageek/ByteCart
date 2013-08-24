package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.util.Vector;

import com.github.catageek.ByteCart.Util.MathUtil;

/**
 * Listener to load chunks around moving carts
 */
public final class PreloadChunkListener implements Listener {

	private final Vector NullVector = new Vector(0,0,0);

	@EventHandler(ignoreCancelled = true)
	public void onVehicleMove(VehicleMoveEvent event) {

		Location loc = event.getTo();
		int to_x = loc.getBlockX() >> 4;
		int to_z = loc.getBlockZ() >> 4;

		if(event.getVehicle() instanceof Minecart) // we care only of minecart
		{
			// preload chunks
			MathUtil.loadChunkAround(loc.getWorld(), to_x, to_z, 2);				
		}
	}
	
	/**
	 * We cancel this event if a cart is moving in the chunk or around
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onChunkUnload(ChunkUnloadEvent event) {


		int n, j, i = event.getChunk().getX()-2, k = i+4, l = event.getChunk().getZ()+2;
		World world = event.getWorld();

		Entity[] entities;

		for (; i<=k; ++i) {
			for (j=l-4;  j<=l ; ++j) {

				if (world.isChunkLoaded(i, j)) {
					entities = world.getChunkAt(i, j).getEntities();


					for (n = entities.length -1; n >=0; --n) {
						if (entities[n] instanceof Minecart && !((Minecart)entities[n]).getVelocity().equals(NullVector)) {

							event.setCancelled(true);

							return;
						}
					}
				}
			}
		}

	}


}
