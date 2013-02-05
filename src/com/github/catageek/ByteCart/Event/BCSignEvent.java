package com.github.catageek.ByteCart.Event;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Signs.BCSign;

/**
 * A sign event. Implementations must inherit this class
 */
public abstract class BCSignEvent extends BCEvent {

	abstract protected BCSign getSign();

	/**
	 * Get the vehicle involved in event
	 * 
	 * @return The vehicle
	 */
	public final Vehicle getVehicle() {

		return getSign().getVehicle();
	}

	/**
	 * Get the destination address of the vehicle
	 * 
	 * @return A String containing the address
	 */
	public final String getDestination() {
		return getSign().getDestinationIP();
	}


	/**
	 * Get the level of the sign.
	 * 
	 * Possible values are:
	 * 	- LOCAL for all BC9XXX signs
	 *  - REGION for a BC8010 router
	 *  - BACKBONE for a BC8020 router
	 *
	 * @return One value from Routing.Updater.Level enum
	 */
	public final Level getLevel() {
		return getSign().getLevel();
	}

	/**
	 * The block containing the sign.
	 * 
	 * For routers, this gives the chest above the router
	 *
	 * @return A Location containing position of sign
	 */
	public final Block getBlock() {
		return getSign().getCenter();
	}
	
	/**
	 * Get the string on the 3rd line of the sign.
	 * This is where users can put a name.
	 *
	 * @return The content of the 3rd line of the sign
	 */
	public final String getFriendlyName() {
		return getSign().getFriendlyName();
	}
}
