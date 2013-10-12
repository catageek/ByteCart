package com.github.catageek.ByteCart.Routing;

import java.io.IOException;

import com.github.catageek.ByteCart.Updaters.UpdaterContent;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Wanderer.RoutingTable;


/**
 * A routing table
 */
public interface RoutingTableWritable extends RoutingTable {
	
	/**
	 * Store a line in the routing table
	 * 
	 * @param entry the track number
	 * @param direction the direction to associate
	 * @param metric the metric to associate
	 */
	public void setEntry(int entry, DirectionRegistry direction, Metric metric);
	
	/**
	 * Remove a line from the routing table
	 * 
	 * @param entry the track number
	 * @param from the direction to remove
	 */
	public void removeEntry(int entry, DirectionRegistry from);
	
	/**
	 * Performs the IGP protocol to update the routing table
	 * 
	 * @param neighbour the IGP packet received
	 * @param from the direction from where we received it
	 */
	public void Update(UpdaterContent neighbour, DirectionRegistry from);
	
	/**
	 * Clear the routing table
	 * 
	 * @param fullreset if set to false, route to entry 0 is kept.
	 */
	public void clear(boolean fullreset);
	
	
	/**
	 * Serialize the routing table
	 * 
	 * @throws IOException
	 */
	void serialize() throws IOException;
}
