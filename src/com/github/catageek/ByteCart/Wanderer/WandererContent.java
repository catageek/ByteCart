package com.github.catageek.ByteCart.Wanderer;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.Routing.Counter;
import com.github.catageek.ByteCartAPI.Wanderer.InventoryContent;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;

public class WandererContent  implements InventoryContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9068486630910859194L;
	
	private transient Inventory inventory = null;
	private String player;

	private Counter counter;

	private long creationtime = Calendar.getInstance().getTimeInMillis();
	private int lastrouterid;

	public WandererContent(Inventory inv, Wanderer.Level level, int region, Player player) {
		this.Region = region;
		this.Level = level;
		this.inventory = inv;
		this.player = player.getName();
		counter = new Counter();
	}
	/**
	 * Set the counter instance
	 * 
	 * @param counter the counter instance to set
	 */
	final void setCounter(Counter counter) {
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

	//internal variable used by updaters
	private int Current = -2;

	/**
	 * Get the level of the updater
	 * 
	 * @return the level
	 */
	public Wanderer.Level getLevel() {
		return Level;
	}

	/**
	 * Get the region of the updater
	 * 
	 * @return the region
	 */
	public int getRegion() {
		return Region;
	}

	/**
	 * Get the ring id where the updater thinks it is in
	 * 
	 * @return the ring id
	 */
	public int getCurrent() {
		return Current;
	}

	/**
	 * Set the ring id where the updater thinks it is in
	 * 
	 * @param current the ring id
	 */
	public void setCurrent(int current) {
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
	 * @return the creationtime
	 */
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

}
