package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.Routing.RoutingTableWritable;

/**
 * An IC that have a routing table should implement this
 */
interface HasRoutingTable {
	/**
	 * Get the routing table
	 *
	 * @return the routing table
	 */
	public RoutingTableWritable getRoutingTable();
}
