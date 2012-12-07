package com.github.catageek.ByteCart;

public abstract class AbstractAddress implements Address {
	public boolean copy(Address a) {
		this.setRegion(a.getRegion().getAmount());
		this.setTrack(a.getTrack().getAmount());
		this.setStation(a.getStation().getAmount());
		this.setIsTrain(a.isTrain());
		return this.UpdateAddress();

	}
	
	public boolean setAddress(String s) {
		return copy(AddressFactory.getAddress(s));
	}
	
	public String toString() {
		return "" + this.getRegion().getAmount() + "." + this.getTrack().getAmount() + "." + (this.getStation().getAmount());
	}


	abstract protected boolean UpdateAddress();
	abstract protected void setRegion(int region);
	abstract protected void setTrack(int track);
	abstract protected void setStation(int station);
	abstract protected void setIsTrain(boolean isTrain);

}
