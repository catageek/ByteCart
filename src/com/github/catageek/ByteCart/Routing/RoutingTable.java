package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;


/**
 * A routing table
 */
public interface RoutingTable {
	/**
	 * Return the best direction matching the entry
	 * 
	 * @param entry the track number
	 * @return the direction
	 */
	public DirectionRegistry getDirection(int entry);
	/**
	 * Get the metric associated with this entry and this direction
	 * 
	 * @param entry the track number
	 * @param direction the direction
	 * @return the metric
	 */
	public int getMetric(int entry, DirectionRegistry direction);
	/**
	 * Get the minimum metric for a specific entry
	 * 
	 * @param entry the track number
	 * @return the minimum metric recorded
	 */
	public Metric getMinMetric(int entry);
	
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
	 * Tells if there is no record for an entry
	 * 
	 * @param entry the track number
	 * @return true if there is no record
	 */
	public boolean isEmpty(int entry);
	
	/**
	 * Clear the routing table
	 * 
	 * @param fullreset if set to false, route to entry 0 is kept.
	 */
	public void clear(boolean fullreset);
	
	/**
	 * Tells if a track is directly connected to a router at a specific direction
	 * 
	 * @param ring the track number
	 * @param direction the direction
	 * @return true if the track is directly connected at this direction
	 */
	public boolean isDirectlyConnected(int ring, DirectionRegistry direction);
	
	/**
	 * Get the track number at the specific direction
	 * 
	 * @param direction the direction
	 * @return the track number
	 */
	public int getDirectlyConnected(DirectionRegistry direction);
	
	/**
	 * Get a direction that has not been configured, or null if all directions are configured
	 * 
	 * @return the direction
	 */
	public BlockFace getFirstUnknown();
	
	/**
	 * Get the number of entries in the routing table
	 * 
	 * @return the size
	 */
	public int size();
	
	/**
	 * Return an iterator of Route in incrementing order
	 *
	 *
	 * @return the set
	 */
	public Iterator<RouteNumber> getOrderedRouteNumbers();
	
	/**
	 * Get a list of tracks that have records with a metric 0 and at the specific direction
	 * 
	 * @param from the direction
	 * @return a list of track numbers
	 */
	public Set<Integer> getDirectlyConnectedList(DirectionRegistry from);
	
	/**
	 * Serialize the routing table
	 * 
	 * @throws IOException
	 */
	void serialize() throws IOException;
}
