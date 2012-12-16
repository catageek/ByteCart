package com.github.catageek.ByteCart;

import org.bukkit.Location;

public final class IsTrainManager {

	private EphemeralBlockMap<Location, Boolean> IsTrain = new EphemeralBlockMap<Location, Boolean>(14);
	
	public EphemeralBlockMap<Location, Boolean> getMap() {
		return IsTrain;
	}
	

}
