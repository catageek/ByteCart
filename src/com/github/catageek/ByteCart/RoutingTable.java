/**
 * 
 */
package com.github.catageek.ByteCart;

// A route in routing table
public interface RoutingTable {
	public Registry getDirection(int entry);
	public Registry getDistance(int entry);
}
