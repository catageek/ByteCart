package com.github.catageek.ByteCart;

public class IsTrainManager {

	private EphemeralBlockMap<Boolean> IsTrain = new EphemeralBlockMap<Boolean>(14);
	
	public EphemeralBlockMap<Boolean> getMap() {
		return IsTrain;
	}
	

}
