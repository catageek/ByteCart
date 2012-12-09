/**
 * 
 */
package com.github.catageek.ByteCart;


// A route in routing table
public interface RoutingTable {
	public DirectionRegistry getDirection(int entry);
	public int getDistance(int entry);
	public void setEntry(int entry, int direction, int distance);
	public void Update(RoutingTableExchange neighbour, DirectionRegistry from);
	public boolean isEmpty(int entry);
	public int getSize();
}
