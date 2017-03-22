package com.github.catageek.ByteCart.Wanderer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.BCCounter;
import com.github.catageek.ByteCart.Routing.Metric;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Wanderer.InventoryContent;
import com.github.catageek.ByteCartAPI.Wanderer.RoutingTable;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;

public class WandererContent  implements InventoryContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9068486630910859194L;
	
	private transient Inventory inventory = null;
	private String player;

	private BCCounter counter;

	private long creationtime = Calendar.getInstance().getTimeInMillis();
	private int lastrouterid;
	private Stack<Integer> Start;
	private Stack<Integer> End;

	//internal variable used by updaters
	private int Current = -2;

	private long expirationtime;

	protected Map<Integer, Metric> tablemap = new HashMap<Integer, Metric>();


	public WandererContent(Inventory inv, Wanderer.Level level, int region, Player player) {
		this.Region = region;
		this.Level = level;
		this.inventory = inv;
		this.player = player.getName();
		counter = new BCCounter();
		setStart(new Stack<Integer>());
		setEnd(new Stack<Integer>());
	}
	/**
	 * Set the counter instance
	 * 
	 * @param counter the counter instance to set
	 */
	final void setCounter(BCCounter counter) {
		this.counter = counter;
	}

	private Wanderer.Level Level;

	private int Region;

	/**
	 * Set the level of the updater
	 * 
	 * @param level the level to store
	 */
	final void setLevel(Wanderer.Level level) {
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

	/**
	 * Get the level of the updater
	 * 
	 * @return the level
	 */
	@Override
	public Wanderer.Level getLevel() {
		return Level;
	}

	/**
	 * Get the region of the updater
	 * 
	 * @return the region
	 */
	@Override
	public int getRegion() {
		return Region;
	}

	/**
	 * Get the ring id where the updater thinks it is in
	 * 
	 * @return the ring id
	 */
	@Override
	public int getCurrent() {
		return Current;
	}

	/**
	 * Set the ring id where the updater thinks it is in
	 * 
	 * @param current the ring id
	 */
	@Override
	public void setCurrent(int current) {
		Current = current;
	}

	/**
	 * @return the counter
	 */
	@Override
	public BCCounter getCounter() {
		return counter;
	}

	/**
	 * @return the inventory
	 */
	@Override
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public long getCreationtime() {
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
	 * @return the player
	 */
	@Override
	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}


	/**
	 * Get the id previously stored
	 * 
	 * @return the id
	 */
	public final int getLastrouterid() {
		return lastrouterid;
	}

	/**
	 * Store an id in the updater book
	 * 
	 * @param lastrouterid the id to store
	 */
	public final void setLastrouterid(int lastrouterid) {
		this.lastrouterid = lastrouterid;
	}

	/**
	 * @return the start
	 */
	@Override
	public Stack<Integer> getStart() {
		return Start;
	}
	
	/**
	 * @return the end
	 */
	@Override
	public Stack<Integer> getEnd() {
		return End;
	}
	
	/**
	 * @param start the start to set
	 */
	void setStart(Stack<Integer> start) {
		Start = start;
	}


	/**
	 * @param end the end to set
	 */
	void setEnd(Stack<Integer> end) {
		End = end;
	}
	/**
	 * Update the expiration time to have twice the spent time left
	 */
	public void updateTimestamp() {
		long initial;
		long expiration;
		if ((initial = this.getCreationtime()) == (expiration = this.getExpirationTime()))
			return;
		long last = Calendar.getInstance().getTimeInMillis();
		long update = last + ((last - initial) << 1);
		if (update > expiration)
			setExpirationTime(update);
	}

	public long getExpirationTime() {
		return expirationtime;
	}
	/**
	 * Set the expiration time
	 * 
	 * @param lastupdate the lastupdate to set
	 */
	protected void setExpirationTime(long lastupdate) {
		this.expirationtime = lastupdate;
	}
	/**
	 * Insert an entry in the IGP packet
	 * 
	 * @param number the ring id
	 * @param metric the metric value
	 */
	@Override
	public void setRoute(int number, int metric) {
		tablemap.put(number, new Metric(metric));
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : setting metric of ring " + number + " to " + metric);
	}
	/**
	 * Get the metric value of a ring of the IGP exchange packet
	 * 
	 * @param entry the ring id
	 * @return the metric
	 */
	@Override
	public int getMetric(int entry) {
		return tablemap.get(entry).value();
	}
	/**
	 * Get the ring that has the minimum metric in the IGP packet
	 * 
	 * @param routingTable the routing table
	 * @param from the direction to exclude from the search
	 * @return the ring id, or -1
	 */
	@Override
	public int getMinDistanceRing(RoutingTable routingTable, DirectionRegistry from) {
		Iterator<Integer> it = routingTable.getOrderedRouteNumbers();
	
		if (! it.hasNext())
			return -1;
	
		//skip ring 0
		it.next();
	
		int route;
		int min = 10000, ret = -1; // big value
	
		while (it.hasNext()) {
			route = it.next();
			if (routingTable.getDirection(route) != from.getBlockFace()) {
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
	 * Tells if the IGP packet has data on a ring
	 * 
	 * @param ring the ring id
	 * @return true if there is data on this ring
	 */
	@Override
	public boolean hasRouteTo(int ring) {
		return tablemap.containsKey(ring);
	}
}
