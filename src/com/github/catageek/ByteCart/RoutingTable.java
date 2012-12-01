/**
 * 
 */
package com.github.catageek.ByteCart;


// A route in routing table
public interface RoutingTable {
	public DirectionRegistry getDirection(int entry);
	public Registry getDistance(int entry);
}
