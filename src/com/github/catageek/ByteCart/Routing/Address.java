package com.github.catageek.ByteCart.Routing;

import com.github.catageek.ByteCart.HAL.RegistryBoth;


public interface Address {
	public RegistryBoth getRegion();
	public RegistryBoth getTrack();
	public RegistryBoth getStation();
	public boolean isTrain();
	public boolean setAddress(String s);
	public boolean setAddress(Address a);
	public boolean setTrain(boolean istrain);
	public boolean isValid();
	public void remove();
	public String toString();
}
