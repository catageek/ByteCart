package com.github.catageek.ByteCart.AddressLayer;

import com.github.catageek.ByteCart.HAL.VirtualRegistry;



// This class represents a canonical address like xx.xx.xx
public class AddressString extends AbstractAddress implements Address {

	private String String; // address as displayed

	public AddressString(String s) {
		if (AddressString.isAddress(s))
			this.String = s;
		else {
			this.String = null;
			this.isValid = false;
		}
	}

	static public boolean isAddress(String s) {
		if(! (s.matches("([0-9]{1,4}\\.){2,2}[0-9]{1,3}"))) {
			return false;
		}

		return true;

	}

	public VirtualRegistry getRegion() {
		VirtualRegistry ret;
		(ret = new VirtualRegistry(Offsets.REGION.getLength())).setAmount(this.getField(0));
		return ret;
	}

	public VirtualRegistry getTrack() {
		VirtualRegistry ret;
		(ret = new VirtualRegistry(Offsets.TRACK.getLength())).setAmount(this.getField(1));
		return ret;
	}

	public VirtualRegistry getStation() {
		VirtualRegistry ret;
		(ret = new VirtualRegistry(Offsets.STATION.getLength())).setAmount(this.getField(2));
		return ret;
	}

	public boolean isTrain() {
		throw new UnsupportedOperationException();
	}


	private int getField(int index) {
		if (this.String == null)
			throw new IllegalStateException("Address is not valid.");
		String[] st = this.String.split("\\.");
		return Integer.parseInt(st[ index ].trim());
	}

	@Override
	public void setRegion(int region) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTrack(int track) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStation(int station) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIsTrain(boolean isTrain) {
		throw new UnsupportedOperationException();
	}

	@Override
	public java.lang.String toString() {
		if (this.String != null)
			return this.String;
		return "";
	}

	@Override
	public boolean setAddress(java.lang.String s) {
		if (isAddress(s)) {
		this.String = s;
		this.isValid = true;
		}
		else {
			this.String = null;
			this.isValid = false;
		}
		return this.isValid;
	}

	@Override
	protected boolean UpdateAddress() {
		finalizeAddress();
		return true;
	}

	@Override
	public void remove() {
		this.String = null;
		this.isValid = false;
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}
