package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.Updater;

/**
 * Event triggered when a local updater leaves a subnet
 */
public class UpdaterLeaveSubnetEvent extends UpdaterEvent {
	private final Address address;
	private final int length;

	/**
	 * Default constructor
	 * 
	 * @param updater The updater involved
	 * @param address The address of the subnet
	 * @param length number of stations this subnet can contain
	 */
	public UpdaterLeaveSubnetEvent(Updater updater, Address address, int length) {
		super(updater);
		this.address = address;
		this.length = length;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
        return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address.toString();
	}
}
