package com.github.catageek.ByteCart.HAL;

/**
 * A registry that can be used as output
 */
public interface RegistryOutput extends Registry {
	/**
	 * Set a specific bit in this registry to the given value.
	 *
	 * @param index the position, starting from most significant bit
	 * @param value
	 */
	public void setBit(int index, boolean value);

	/**
	 * Set the value of this registry to the given amount.
	 *
	 * @param amount
	 */
	public void setAmount(int amount);
}
