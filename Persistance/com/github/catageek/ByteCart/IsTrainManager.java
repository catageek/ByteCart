package com.github.catageek.ByteCart;

import org.bukkit.Location;

public final class IsTrainManager {

	private ExpirableMap<Location, Boolean> IsTrain = new ExpirableMap<Location, Boolean>(14, false, "isTrain");
	
	public ExpirableMap<Location, Boolean> getMap() {
		return IsTrain;
	}
	

}
