package com.github.catageek.ByteCart.Routing;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Wanderer.WandererContent;
import com.github.catageek.ByteCartAPI.Routing.Updater;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

/**
 * A class to store data in books used by updater
 */
public class UpdaterContent extends WandererContent implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 848098890652934583L;

	private Map<Integer, Metric> tablemap = new HashMap<Integer, Metric>();
	private boolean fullreset = false;
	private boolean isnew = false;
	private long lastrouterseen;
	private long expirationtime;


	public UpdaterContent(Inventory inv, Updater.Level level, int region, Player player
			, boolean isfullreset) {
		this(inv, level, region, player, isfullreset, false);
	}
	
	public UpdaterContent(Inventory inv, Updater.Level level, int region, Player player
			, boolean isfullreset, boolean isnew) {
		super(inv, level, region, player);
		this.fullreset = isfullreset;
		this.isnew = isnew;
		expirationtime = ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60) * 60000 + getCreationtime();
	}

	/**
	 * Get the metric value of a ring of the IGP exchange packet
	 * 
	 * @param entry the ring id
	 * @return the metric
	 */
	int getMetric(int entry) {
		return tablemap.get(entry).value();
	}

	/**
	 * Tells if the IGP packet has data on a ring
	 * 
	 * @param ring the ring id
	 * @return true if there is data on this ring
	 */
	boolean hasRouteTo(int ring) {
		return tablemap.containsKey(ring);
	}

	/**
	 * Insert an entry in the IGP packet
	 * 
	 * @param number the ring id
	 * @param metric the metric value
	 */
	void setRoute(int number, Metric metric) {
		tablemap.put(number, metric);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : setting metric of ring " + number + " to " + metric.value());
	}

	/**
	 * Get the ring that has the minimum metric in the IGP packet
	 * 
	 * @param routes the routing table
	 * @param from the direction to exclude from the search
	 * @return the ring id, or -1
	 */
	int getMinDistanceRing(RoutingTable routes, DirectionRegistry from) {
		Iterator<RouteNumber> it = routes.getOrderedRouteNumbers();

		if (! it.hasNext())
			return -1;

		//skip ring 0
		it.next();

		int route;
		int min = 10000, ret = -1; // big value

		while (it.hasNext()) {
			route = it.next().value();
			if (routes.getDirection(route).getAmount() != from.getAmount()) {
				if (! this.hasRouteTo(route)) {
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : found ring " + route + " was never visited");
					return route;
				}

				else {
					if (getMetric(route) < min) {
						min = getMetric(route);
						ret = route;
					}
				}
			}
		}
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : minimum found ring " + ret + " with " + min);
		return ret;
	}

	/**
	 * Get a set of the entries of the IGP packet
	 * 
	 * @return the set
	 */
	Set<Entry<Integer,Metric>> getEntrySet() {
		return tablemap.entrySet();
	}

	/**
	 * Build the IGP exchange packet
	 * 
	 * @param table the routing table
	 * @param direction the direction to exclude
	 */
	void putRoutes(RoutingTable table, DirectionRegistry direction) {
		tablemap.clear();
		Iterator<RouteNumber> it = table.getOrderedRouteNumbers();
		while (it.hasNext()) {
			int i = it.next().value();
			if( table.getDirection(i) != null && table.getDirection(i).getAmount() != direction.getAmount()) {
				tablemap.put(i, table.getMinMetric(i));
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : Route exchange : give ring " + i + " with metric " + table.getMinMetric(i).value() + " to " + table.getDirection(i).getBlockFace());
			}

		}
	}

	/**
	 * Set the timestamp field to now
	 */
	void seenTimestamp() {
		this.lastrouterseen = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Get the time difference between now and the last time we called seenTimestamp()
	 * 
	 * @return the time difference, or -1 if seenTimestamp() was never called
	 */
	public int getInterfaceDelay() {
		if (lastrouterseen != 0)
			return (int) ((Calendar.getInstance().getTimeInMillis() - lastrouterseen) / 1000);
		return -1;
	}

	/**
	 * @return the fullreset
	 */
	boolean isFullreset() {
		return fullreset;
	}

	/**
	 * @return the isnew
	 */
	boolean isNew() {
		return isnew;
	}
	
	/**
	 * Update the expiration time to have twice the spent time left
	 */
	void updateTimestamp() {
		long initial;
		long expiration;
		if ((initial = this.getCreationtime()) == (expiration = this.getExpirationTime()))
			return;
		long last = Calendar.getInstance().getTimeInMillis();
		long update = last + ((last - initial) << 1);
		if (update > expiration)
			setExpirationTime(update);
	}
	
	/**
	 * Return the expiration time
	 * 
	 * @return the expiration time
	 */
	long getExpirationTime() {
		return expirationtime;
	}

	/**
	 * Set the expiration time
	 * 
	 * @param lastupdate the lastupdate to set
	 */
	private void setExpirationTime(long lastupdate) {
		this.expirationtime = lastupdate;
	}
}
