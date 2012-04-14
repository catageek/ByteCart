package com.github.catageek.ByteCart;

public interface AddressRouted extends Address {
	public Registry getTTL();
	public void updateTTL(int i);
	public Address initializeTTL();
}
