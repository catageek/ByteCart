package com.github.catageek.ByteCart;

import java.util.Map;

public abstract class AbstractRoutingTable {


	public void Update(RoutingTableExchange neighbour, DirectionRegistry from) {
		
		for (Map.Entry<Integer, Integer> entry : neighbour.getEntrySet()) {
		
			int ring = entry.getKey();
			int distance = entry.getValue() + 1;

			if (this.getDistance(ring) > distance || this.isEmpty(ring)) {
				this.setEntry(ring, MathUtil.binlog(from.getAmount()) << 4, distance);
				if(ByteCart.debug) {
					ByteCart.log.info("ByteCart : Update : ring = " + ring);
					ByteCart.log.info("ByteCart : Update : distance = " + distance);
					ByteCart.log.info("ByteCart : Update : set direction " + from.ToString());
				}

			}
		}
	}
	
	abstract public int getDistance(int entry);
	abstract public void setEntry(int entry, int i, int distance);
	abstract public boolean isEmpty(int entry);


}
