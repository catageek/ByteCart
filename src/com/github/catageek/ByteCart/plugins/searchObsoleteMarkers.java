package com.github.catageek.ByteCart.plugins;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.dynmap.markers.Marker;

import com.github.catageek.ByteCart.HAL.AbstractIC;
	
/**
 * Synchronous task to remove markers
 */
public final class searchObsoleteMarkers implements Runnable {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Iterator<Marker> it = BCDynmapPlugin.markerset.getMarkers().iterator();
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

