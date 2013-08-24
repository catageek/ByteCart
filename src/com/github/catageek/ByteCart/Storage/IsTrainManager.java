package com.github.catageek.ByteCart.Storage;

import org.bukkit.Location;

/**
 * A map that contain the train bit for each component
 * 
 * The train bit set to true means that a train is currently using the component
 */
public final class IsTrainManager {

	private ExpirableMap<Location, Boolean> IsTrain = new ExpirableMap<Location, Boolean>(14, false, "isTrain");
	
	/**
	 * Get the map
	 *
	 * @return the map
	 */
	public ExpirableMap<Location, Boolean> getMap() {
		return IsTrain;
	}
	

}
