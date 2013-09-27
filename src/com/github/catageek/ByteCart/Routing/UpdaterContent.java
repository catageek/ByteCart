package com.github.catageek.ByteCart.Routing;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.Routing.Updater;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

/**
 * A class to store data in books used by updater
 */
public class UpdaterContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9068486630910859194L;

	private Map<Integer, Metric> tablemap = new HashMap<Integer, Metric>();

	private transient Inventory inventory = null;
	private String player;

	private Counter counter;

	private long creationtime = Calendar.getInstance().getTimeInMillis();
	private long expirationtime = ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60) * 60000 + creationtime;
	private long lastrouterseen;
	private int lastrouterid;

	/**
	 * Set the counter instance
	 * 
	 * @param counter the counter instance to set
	 */
	final void setCounter(Counter counter) {
		this.counter = counter;
	}

	public UpdaterContent() {
	}

	private Updater.Level Level;

	private int Region;

	/**
	 * Set the level of the updater
	 * 
	 * @param level the level to store
	 */
	final void setLevel(Updater.Level level) {
		Level = level;
	}

	/**
	 * Set the region of the updater
	 * 
	 * @param region the region to set
	 */
	final void setRegion(int region) {
		Region = region;
	}

	//internal variable used by updaters
	private int Current = -2;

	private boolean fullreset = false;

	private boolean isnew = false;

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

	public UpdaterContent(Inventory inv, Updater.Level level, int region, Player player
			, boolean isfullreset) {
		this(inv, level, region, player, isfullreset, false);
	}
	public UpdaterContent(Inventory inv, Updater.Level level, int region, Player player
			, boolean isfullreset, boolean isnew) {
		this.Region = region;
		this.Level = level;
		this.inventory = inv;
		this.player = player.getName();
		counter = new Counter();
		this.fullreset = isfullreset;
		this.isnew = isnew;
	}

	/**
	 * Get the level of the updater
	 * 
	 * @return the level
	 */
	public Updater.Level getLevel() {
		return Level;
	}

	/**
	 * Get the region of the updater
	 * 
	 * @return the region
	 */
	int getRegion() {
		return Region;
	}

	/**
	 * Get the ring id where the updater thinks it is in
	 * 
	 * @return the ring id
	 */
	int getCurrent() {
		return Current;
	}

	/**
	 * Set the ring id where the updater thinks it is in
	 * 
	 * @param current the ring id
	 */
	void setCurrent(int current) {
		Current = current;
	}

	/**
	 * @return the counter
	 */
	public Counter getCounter() {
		return counter;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
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
	 * Set the timestamp field to now
	 */
	void seenTimestamp() {
		this.lastrouterseen = Calendar.getInstance().getTimeInMillis();
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

	/**
	 * @return the creationtime
	 */
	long getCreationtime() {
		return creationtime;
	}

	/**
	 * @param creationtime the creationtime to set
	 */
	@SuppressWarnings("unused")
	private void setCreationtime(long creationtime) {
		this.creationtime = creationtime;
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
	 * @return the player
	 */
	Player getPlayer() {
		return Bukkit.getPlayer(player);
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
	 * Get the id previously stored
	 * 
	 * @return the id
	 */
	final int getLastrouterid() {
		return lastrouterid;
	}

	/**
	 * Store an id in the updater book
	 * 
	 * @param lastrouterid the id to store
	 */
	final void setLastrouterid(int lastrouterid) {
		this.lastrouterid = lastrouterid;
	}
}
