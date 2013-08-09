package com.github.catageek.ByteCart.plugins;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.dynmap.markers.Marker;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;

// Executed in another thread
final class searchObsoleteMarkers implements Runnable {

	@Override
	public void run() {
		Set<Marker> toRemove = new HashSet<Marker>();
		Iterator<Marker> it = BCDynmapPlugin.markerset.getMarkers().iterator();
		int x, y, z;
		while (it.hasNext()) {
			Marker m = it.next();
			x = Location.locToBlock(m.getX());
			y = Location.locToBlock(m.getY());
			z = Location.locToBlock(m.getZ());
			Block block = Bukkit.getServer().getWorld(m.getWorld()).getBlockAt(x, y, z);
			if (! AbstractIC.checkEligibility(block))
				toRemove.add(m);
		}
		if (! toRemove.isEmpty())
			Bukkit.getScheduler().runTask(ByteCart.myPlugin, new removeMarkers(toRemove));
	}
	
	// Executed in the main thread
	private static final class removeMarkers implements Runnable {

		private final Set<Marker> toRemove;

		public removeMarkers(Set<Marker> toRemove) {
			this.toRemove = toRemove;
		}

		@Override
		public void run() {
			Iterator<Marker> it = toRemove.iterator();
			int x, y, z;
			while (it.hasNext()) {
				Marker m = it.next();
				x = Location.locToBlock(m.getX());
				y = Location.locToBlock(m.getY());
				z = Location.locToBlock(m.getZ());
				Block block = Bukkit.getServer().getWorld(m.getWorld()).getBlockAt(x, y, z);
				if (! AbstractIC.checkEligibility(block))
					m.deleteMarker();
			}
		}
	}
}
