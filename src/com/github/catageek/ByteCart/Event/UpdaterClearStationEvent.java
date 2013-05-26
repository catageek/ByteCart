package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.Routing.Updater;

/**
 * Event triggered when a region reset updater clears the address
 * of a BC9001 sign.
 */
public class UpdaterClearStationEvent extends UpdaterEvent {
	private final Address oldAddress;

	/**
	 * Default constructor
	 * 
	 * @param updater The updater involved
	 * @param oldAddress The old address of the station
	 */
	public UpdaterClearStationEvent(Updater updater, Address oldAddress) {
		super(updater);
		this.oldAddress = oldAddress;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
        return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }

	/**
	 * Get the old address erased
	 *
	 * @return A String containing the address
	 */
	public String getOldAddress() {
		return oldAddress.toString();
	}
}
