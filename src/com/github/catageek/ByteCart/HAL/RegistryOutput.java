package com.github.catageek.ByteCart.HAL;

public interface RegistryOutput extends Registry {
	/**
	 * Set a specific bit in this registry to the given value.
	 *
	 * @param index
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
