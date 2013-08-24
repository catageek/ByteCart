package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.Routing.RoutingTable;

/**
 * An IC that have a routing table should implement this
 */
public interface HasRoutingTable {
	/**
	 * Get the routing table
	 *
	 * @return the routing table
	 */
	public RoutingTable getRoutingTable();
}
