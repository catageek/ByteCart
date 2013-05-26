package com.github.catageek.ByteCart.Routing;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

public abstract class AbstractRoutingTable {


	public void Update(UpdaterContent neighbour, DirectionRegistry from) {
		// Djikstra algorithm
		// search for better routes in the received ones
		int interfacedelay = neighbour.getInterfaceDelay();
		
		for (Map.Entry<Integer, Metric> entry : neighbour.getEntrySet()) {

			int ring = entry.getKey();
			Metric metric = entry.getValue();
			int computedmetric = metric.value();
			if (interfacedelay > 0)
				computedmetric += interfacedelay;			
			int routermetric = this.getMetric(ring, from);
			Metric m;
			boolean directlyconnected = (m = this.getMinMetric(ring)) != null && m.value() == 0;
			
			if ( ! directlyconnected && (routermetric > computedmetric || routermetric < 0)) {
				this.setEntry(ring, from, new Metric(computedmetric));
				if(ByteCart.debug) {
					ByteCart.log.info("ByteCart : Update : ring = " + ring + ", metric = " + computedmetric + ", direction " + from.ToString());
				}
				neighbour.updateTimestamp();
			}
		}
		// search for routes that are no more announced and not directly connected
		// to remove them
		Iterator<Integer> it = this.getNotDirectlyConnectedList(from).iterator();
		while (it.hasNext()) {
			Integer route;
			if(!neighbour.hasRouteTo(route = it.next())) {
				this.removeEntry(route, from);
				if(ByteCart.debug) {
					ByteCart.log.info("ByteCart : Remove : ring = " + route + " from " + from.ToString());
				}
				neighbour.updateTimestamp();
			}
		}
	}

	public final boolean isDirectlyConnected(int ring, DirectionRegistry direction) {
		if (this.getDirection(ring) != null)
			return this.getMetric(ring, direction) == 0;
		return false;
	}


	public final int getDirectlyConnected(DirectionRegistry direction) {
		Set<Integer> rings = getDirectlyConnectedList(direction);
		return rings.size() == 1 ? rings.iterator().next() : -1 ;
	}

	public final BlockFace getFirstUnknown() {
		for (BlockFace face : BlockFace.values()) {
			switch(face) {
			case NORTH:
			case EAST:
			case SOUTH:
			case WEST:

				if (this.getDirectlyConnectedList(new DirectionRegistry(face)).isEmpty())
					return face;
			default:
				break;
			}
		}
		return null;
	}

	abstract public Set<Integer> getDirectlyConnectedList(DirectionRegistry direction);
	abstract public int getMetric(int entry, DirectionRegistry direction);
	abstract public Metric getMinMetric(int entry);
	abstract public void setEntry(int entry, DirectionRegistry direction, Metric metric);
	abstract public boolean isEmpty(int entry);
	abstract protected Iterator<RouteNumber> getOrderedRouteNumbers();
	abstract protected Set<Integer> getNotDirectlyConnectedList(DirectionRegistry direction);
	abstract public void removeEntry(int entry, DirectionRegistry from);
	abstract public DirectionRegistry getDirection(int entry);
}
