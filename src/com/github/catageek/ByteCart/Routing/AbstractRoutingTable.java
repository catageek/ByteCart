package com.github.catageek.ByteCart.Routing;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

/**
 * An abstract class for routing tables 
 */
public abstract class AbstractRoutingTable {


	/**
	 * Performs the IGP protocol to update the routing table
	 * 
	 * @param neighbour the IGP packet received
	 * @param from the direction from where we received it
	 */
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

	/**
	 * Tells if a track is directly connected to a router at a specific direction
	 * 
	 * @param ring the track number
	 * @param direction the direction
	 * @return true if the track is directly connected at this direction
	 */
	public final boolean isDirectlyConnected(int ring, DirectionRegistry direction) {
		if (this.getDirection(ring) != null)
			return this.getMetric(ring, direction) == 0;
		return false;
	}


	/**
	 * Get the track number at the specific direction
	 * 
	 * @param direction the direction
	 * @return the track number
	 */
	public final int getDirectlyConnected(DirectionRegistry direction) {
		Set<Integer> rings = getDirectlyConnectedList(direction);
		return rings.size() == 1 ? rings.iterator().next() : -1 ;
	}

	/**
	 * Get a direction that has not been configured, or null if all directions are configured
	 * 
	 * @return the direction
	 */
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

	/**
	 * Get a list of tracks that have records with a metric 0 and at the specific direction
	 * 
	 * @param direction the direction
	 * @return a list of track numbers
	 */
	abstract public Set<Integer> getDirectlyConnectedList(DirectionRegistry direction);

	/**
	 * Get the metric associated with this entry and this direction
	 * 
	 * @param entry the track number
	 * @param direction the direction
	 * @return the metric
	 */
	abstract public int getMetric(int entry, DirectionRegistry direction);
	
	/**
	 * Get the minimum metric for a specific entry
	 * 
	 * @param entry the track number
	 * @return the minimum metric recorded
	 */
	abstract public Metric getMinMetric(int entry);

	/**
	 * Store a line in the routing table
	 * 
	 * @param entry the track number
	 * @param direction the direction to associate
	 * @param metric the metric to associate
	 */
	abstract public void setEntry(int entry, DirectionRegistry direction, Metric metric);

	/**
	 * Tells if there is no record for an entry
	 * 
	 * @param entry the track number
	 * @return true if there is no record
	 */
	abstract public boolean isEmpty(int entry);

	/**
	 * Get an iterator on the entries
	 * 
	 * @return an iterator
	 */
	abstract protected Iterator<RouteNumber> getOrderedRouteNumbers();

	/**
	 * Get a set of track numbers that are seen in a direction, but not directly connected
	 * 
	 * @param direction the direction
	 * @return a set of track numbers
	 */
	abstract protected Set<Integer> getNotDirectlyConnectedList(DirectionRegistry direction);

	/**
	 * Remove a line from the routing table
	 * 
	 * @param entry the track number
	 * @param from the direction to remove
	 */
	abstract public void removeEntry(int entry, DirectionRegistry from);

	/**
	 * Return the best direction matching the entry
	 * 
	 * @param entry the track number
	 * @return the direction
	 */
	abstract public DirectionRegistry getDirection(int entry);
}
