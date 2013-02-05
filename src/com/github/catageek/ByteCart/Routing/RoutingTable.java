/**
 * 
 */
package com.github.catageek.ByteCart.Routing;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.Util.DirectionRegistry;


// A route in routing table
public interface RoutingTable {
	public DirectionRegistry getDirection(int entry);
	public int getDistance(int entry);
	public void setEntry(int entry, int direction, int distance);
	public void removeEntry(int entry);
	public void Update(RoutingTableExchange neighbour, DirectionRegistry from);
	public boolean isEmpty(int entry);
	public int getSize();
	public Set<Entry<Integer,Integer>> getRoutesTo(DirectionRegistry direction);
	public void clear();
	public boolean isDirectlyConnected(int ring, DirectionRegistry direction);
	public int getDirectlyConnected(DirectionRegistry direction);
	public BlockFace getFirstUnknown();
	public Set<Entry<Integer, Integer>> getEntrySet();
	public List<Integer> getDirectlyConnectedList(DirectionRegistry from);
}
