package com.github.catageek.ByteCart;


public interface Address {
	public Registry getRegion();
	public Registry getTrack();
	public Registry getStation();
	public boolean isTrain();
	public Address setRegion(int region);
	public Address setTrack(int track);
	public Address setStation(int station);
	public Address setIsTrain(boolean isTrain);
	public String getAddress();
	public Address setAddress(String s);
	public boolean copy(Address a);
}
