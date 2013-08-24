package com.github.catageek.ByteCart.Storage;

/**
 * Represents an integer that can be stored by its decomposition of power of 2
 */
public interface Partitionable {
	/**
	 * Get the value
	 *
	 * @return the value to store
	 */
	int getAmount();
}
