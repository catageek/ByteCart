package com.github.catageek.ByteCart.AddressLayer;


public interface AddressRouted extends Address {
	public int getTTL();
	public void updateTTL(int i);
	public void initializeTTL();
}
