package com.github.catageek.ByteCart;

import org.bukkit.inventory.Inventory;

/**
 * Represents a runnable that can updates its inventory variable
 * 
 * @param <T>
 */
public interface ModifiableRunnable<T> extends Runnable {

	/**
	 * Updates the inventory variable
	 *
	 * @param inventory
	 */
	void SetParam(Inventory inventory);

}
