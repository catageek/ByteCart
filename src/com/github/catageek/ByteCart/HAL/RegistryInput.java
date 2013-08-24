package com.github.catageek.ByteCart.HAL;

/**
 * A registry that can be used as input and read
 */
public interface RegistryInput extends Registry{
	/**
	 * Get the value of the bit at position index
	 *
	 * @param index the position, 0 for most significant bit
	 * @return true if the bit is set to 1, false otherwise
	 */
	public boolean getBit(int index);
}
