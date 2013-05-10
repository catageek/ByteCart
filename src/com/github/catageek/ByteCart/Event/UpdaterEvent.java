package com.github.catageek.ByteCart.Event;

import com.github.catageek.ByteCart.HAL.IC;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.Updater.Level;

/**
 * An event concerning an updater.
 * Implementations must inherit this class.
 */
abstract public class UpdaterEvent extends BCEvent {
	
	private final Updater updater;
	
	/**
	 * Default constructor
	 * 
	 * @param updater involved in the event
	 */
	UpdaterEvent(Updater updater) {
		super(updater.getBcSign());
		this.updater = updater;
	}
	
	protected final IC getSign() {
		return updater.getBcSign();
	}

	/**
	 * @return The track number currently in use, or -1 if invalid
	 */
	public final int getCurrentTrack() {
		return updater.getTrackNumber();
	}
	
	/**
	 * Get the level of the updater.
	 * 
	 * 	Possible values are:
	 * 	- LOCAL or RESET_LOCAL
	 *  - REGION or RESET_REGION
	 *  - BACKBONE or RESET_BACKBONE
	 * 
	 * @return The level of the updater.
	 */
	public final Level getUpdaterLevel() {
		return updater.getLevel();
	}
	
	/**
	 * @return The region where the updater is registered.
	 * returns 0 for backbone.
	 */
	public final int getUpdaterRegion() {
		return updater.getRegion();
	}
	
	/**
	 * @return the entity id of the vehicle
	 */
	public final int getVehicleId() {
		return updater.getVehicle().getEntityId();
	}
	
	/**
	 * @return The updater involved
	 */
	protected final Updater getUpdater() {
		return updater;
	}
}
