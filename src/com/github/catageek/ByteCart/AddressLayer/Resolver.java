package com.github.catageek.ByteCart.AddressLayer;

/**
 * Represent a class that can get an address from a name
 */
public interface Resolver {
	/**
	 * Resolve the name
	 * 
	 * @param name the name to resolve
	 * @return the address corresponding to the name, or null
	 */
	String resolve(String name);
}
