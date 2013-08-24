package com.github.catageek.ByteCart.AddressLayer;

import com.github.catageek.ByteCart.HAL.RegistryBoth;


/**
 * Represents an address that can be used as destination or return address
 */
public interface Address {
	/**
	 * Get the region number
	 *
	 *
	 * @return the region number
	 */
	public RegistryBoth getRegion();
	
	/**
	 * Get the ring number
	 *
	 *
	 * @return the ring number
	 */
	public RegistryBoth getTrack();
	
	/**
	 * Get the station number
	 *
	 *
	 * @return the station number
	 */
	public RegistryBoth getStation();
	
	/**
	 * Get the train flag
	 *
	 *
	 * @return true if the flag is set
	 */
	public boolean isTrain();
	
	/**
	 * Get the state of the return address
	 *
	 *
	 * @return true if this is a return address
	 */
	public boolean isReturnable();
	
	/**
	 * Set an address with a name
	 *
	 * <p>{@link Address#finalizeAddress()} should be called later to actually set the address</p>
	 *
	 * @param s the address to set in the format aa.bb.cc
	 * @param name the name to associate to this address
	 * @return true if the address was set
	 */
	public boolean setAddress(String s, String name);

	/**
	 * Set an address
	 *
	 * <p>{@link Address#finalizeAddress()} should be called later to actually set the address</p>
	 *
	 * @param s the address to set in the format aa.bb.cc
	 * @return true if the address was set
	 */
	public boolean setAddress(String s);

	/**
	 * Set an address with a name
	 *
	 * <p>{@link Address#finalizeAddress()} should be called later to actually set the address</p>
	 *
	 * @param a the address to set
	 * @param name the name to associate to this address
	 * @return true if the address was set
	 */
	public boolean setAddress(Address a, String name);
	
	/**
	 * Set the train flag
	 *
	 * <p>{@link Address#finalizeAddress()} should be called later to actually set the flag</p>
	 *
	 * @param istrain must be true to set the flag
	 * @return true if the flag was set
	 */
	public boolean setTrain(boolean istrain);
	
	/**
	 * Flush the address to its support
	 *
	 *
	 */
	public void finalizeAddress();
	
	/**
	 * Tell if an address can be considered as valid or should be discarded
	 * 
	 * <p>For example, an empty field is an invalid address even if "0.0.0" is returned</p>
	 *
	 *
	 * @return true if the address is valid
	 */
	public boolean isValid();
	
	/**
	 * remove the address
	 *
	 * <p>{@link Address#finalizeAddress()} should be called later to actually remove the address from the support</p>
	 */
	public void remove();
	
	/**
	 * Return a printable string for this address
	 *
	 *
	 * @return the address as string
	 */
	public String toString();
}
