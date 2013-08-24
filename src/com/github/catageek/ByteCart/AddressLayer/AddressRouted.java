package com.github.catageek.ByteCart.AddressLayer;


/**
 * Represents an address currently routed
 */
public interface AddressRouted extends Address {
	
	/**
	 * Get the TTL (time-to-live) associated with the address
	 *
	 *
	 * @return the TTL
	 */
	public int getTTL();
	
	/**
	 * Set the TTL
	 *
	 * {@link Address#finalizeAddress()} should be called later to actually set the TTL
	 *
	 * @param i the value to set
	 */
	public void updateTTL(int i);
	
	/**
	 * Initialize TTL to its default value
	 *
	 * {@link Address#finalizeAddress()} should be called later to actually initialize the TTL
	 *
	 */
	public void initializeTTL();
}
