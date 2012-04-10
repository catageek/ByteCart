package com.github.catageek.ByteCart;

public interface Address {
	public Registry getRegion();
	public Registry getTrack();
	public Registry getStation();
	public Registry getService();
	public Address setRegion(int region);
	public Address setTrack(int track);
	public Address setStation(int station);
	public String getAddress();
}
