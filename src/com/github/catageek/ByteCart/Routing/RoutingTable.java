/**
 * 
 */
package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.block.BlockFace;
import com.github.catageek.ByteCart.Util.DirectionRegistry;


// A route in routing table
public interface RoutingTable {
	public DirectionRegistry getDirection(int entry);
	public int getMetric(int entry, DirectionRegistry direction);
	public Metric getMinMetric(int entry);
	public void setEntry(int entry, DirectionRegistry direction, Metric metric);
	public void removeEntry(int entry, DirectionRegistry from);
	public void Update(UpdaterContent neighbour, DirectionRegistry from);
	public boolean isEmpty(int entry);
	public void clear(boolean fullreset);
	public boolean isDirectlyConnected(int ring, DirectionRegistry direction);
	public int getDirectlyConnected(DirectionRegistry direction);
	public BlockFace getFirstUnknown();
	
	/**
	 * Return an iterator of Route in incrementing order
	 *
	 *
	 * @return the set
	 */
	public Iterator<RouteNumber> getOrderedRouteNumbers();
	
	public Set<Integer> getDirectlyConnectedList(DirectionRegistry from);
	void serialize() throws IOException;
}
