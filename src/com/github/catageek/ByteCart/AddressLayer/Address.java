package com.github.catageek.ByteCart.AddressLayer;

import com.github.catageek.ByteCart.HAL.RegistryBoth;


public interface Address {
	public RegistryBoth getRegion();
	public RegistryBoth getTrack();
	public RegistryBoth getStation();
	public boolean isTrain();
	public boolean isReturnable();
	public boolean setAddress(String s, String name);
	public boolean setAddress(String s);
	public boolean setAddress(Address a, String name);
	public boolean setTrain(boolean istrain);
	public void finalizeAddress();
	public boolean isValid();
	public void remove();
	public String toString();
}
