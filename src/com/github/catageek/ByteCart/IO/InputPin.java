package com.github.catageek.ByteCart.IO;

/**
 * Represents a readable component, giving 1 bit
 */
public interface InputPin {
	/**
	 * Read the bit
	 *
	 * @return true if 1, false otherwise
	 */
	public boolean read();
}
