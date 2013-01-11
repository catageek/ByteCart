package com.github.catageek.ByteCart.Routing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;

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
		// search for routes that are no more announced and not directly connected
		for(Map.Entry<Integer, Integer> entry2 : this.getRoutesTo(from)) {
			if(!neighbour.hasRouteTo(entry2.getKey()) && ! this.isEmpty(entry2.getKey()) && ! (this.getDistance(entry2.getKey()) == 0)) {
				this.removeEntry(entry2.getKey());
				if(ByteCart.debug) {
					ByteCart.log.info("ByteCart : Remove : ring = " + entry2.getKey() + " from " + from.ToString());

				}
			}
		}
	}

	public final boolean isDirectlyConnected(int ring, DirectionRegistry direction) {
		if (this.getDirection(ring) != null)
		return (this.getDirection(ring).getAmount() == direction.getAmount())
				&& this.getDistance(ring) == 0;
		return false;
	}

	public final List<Integer> getDirectlyConnected(DirectionRegistry direction) {
		List<Integer> list = new ArrayList<Integer>();
		Iterator<Entry<Integer,Integer>> iterator = this.getRoutesTo(direction).iterator();
		while(iterator.hasNext()) {
			Entry<Integer,Integer> entry = iterator.next();
			if (entry.getValue() == 0)
				list.add(entry.getKey());
		}
		return list;
	}

	public final BlockFace getFirstUnknown() {
		for (BlockFace face : BlockFace.values()) {
			switch(face) {
			case NORTH:
			case EAST:
			case SOUTH:
			case WEST:

				if (this.getDirectlyConnected(new DirectionRegistry(face)).isEmpty())
					return face;
			default:
				break;
			}
		}
		return null;
	}

	abstract public int getDistance(int entry);
	abstract public void setEntry(int entry, int i, int distance);
	abstract public boolean isEmpty(int entry);
	abstract public Set<Entry<Integer,Integer>> getRoutesTo(DirectionRegistry direction);
	abstract public void removeEntry(int entry);
	abstract public DirectionRegistry getDirection(int entry);

}
