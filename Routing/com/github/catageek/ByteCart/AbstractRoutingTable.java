package com.github.catageek.ByteCart;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class AbstractRoutingTable {


	public final void Update(RoutingTableExchange neighbour, DirectionRegistry from) {
		// Djikstra algorithm
		// search for better routes in the received ones
		for (Map.Entry<Integer, Integer> entry : neighbour.getEntrySet()) {

			int ring = entry.getKey();
			int distance = entry.getValue() + 1;

			if (this.getDistance(ring) > distance || this.isEmpty(ring)) {
				this.setEntry(ring, MathUtil.binlog(from.getAmount()) << 4, distance);
				if(ByteCart.debug) {
					ByteCart.log.info("ByteCart : Update : ring = " + ring + ", distance = " + distance + ", direction " + from.ToString());
				}

			}
		}
		// search for routes that are no more announced
		for(Map.Entry<Integer, Integer> entry2 : this.getRoutesTo(from)) {
			if(!neighbour.hasRouteTo(entry2.getKey())) {
				this.setEntry(entry2.getKey(), 0, 0);
				if(ByteCart.debug) {
					ByteCart.log.info("ByteCart : Remove : ring = " + entry2.getKey() + " from " + from.ToString());

				}
			}
		}
	}

	abstract public int getDistance(int entry);
	abstract public void setEntry(int entry, int i, int distance);
	abstract public boolean isEmpty(int entry);
	abstract public Set<Entry<Integer,Integer>> getRoutesTo(DirectionRegistry direction);

}
