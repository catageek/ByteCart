package com.github.catageek.ByteCart;

public abstract class AbstractAddress implements Address {
	public boolean copy(Address a) {
		boolean ret = (this.setRegion(a.getRegion().getAmount()) != null);
		ret &= (this.setTrack(a.getTrack().getAmount()) != null);
		ret &= (this.setStation(a.getStation().getAmount()) != null);
		ret &= (this.setIsTrain(a.isTrain()) != null);
		return ret;
	}

}
