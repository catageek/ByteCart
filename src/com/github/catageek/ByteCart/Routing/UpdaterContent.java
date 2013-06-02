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
import com.github.catageek.ByteCart.Util.DirectionRegistry;

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

	final void setCounter(Counter counter) {
		this.counter = counter;
	}

	public UpdaterContent() {
	}

	private Updater.Level Level;

	private int Region;

	final void setLevel(Updater.Level level) {
		Level = level;
	}

	final void setRegion(int region) {
		Region = region;
	}

	//internal variable used by updaters
	private int Current = -2;

	private boolean fullreset = false;

	private boolean isnew = false;

	int getMetric(int entry) {
		return tablemap.get(entry).value();
	}

	boolean hasRouteTo(int ring) {
		return tablemap.containsKey(ring);
	}

	void setRoute(int number, Metric metric) {
		tablemap.put(number, metric);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : setting metric of ring " + number + " to " + metric.value());
	}

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

	Set<Entry<Integer,Metric>> getEntrySet() {
		return tablemap.entrySet();
	}

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

	public Updater.Level getLevel() {
		return Level;
	}

	int getRegion() {
		return Region;
	}

	int getCurrent() {
		return Current;
	}

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
	
	void seenTimestamp() {
		this.lastrouterseen = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * @return the lastupdate
	 */
	long getExpirationTime() {
		return expirationtime;
	}

	/**
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

	final int getLastrouterid() {
		return lastrouterid;
	}

	final void setLastrouterid(int lastrouterid) {
		this.lastrouterid = lastrouterid;
	}
}
