package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Signs.Subnet;

/**
 * Event triggered when a vehicle is leaving a station sign,
 * going into the station or not.
 * 
 * This event is triggered after the "busy line" check, so the
 * direction is definitive.
 */
public class SignPostStationEvent extends SignPostSubnetEvent {

	/**
	 * Default constructor
	 * 
	 * The side parameter may be:
	 * - LEFT: the vehicle will not enter the station
	 * - RIGHT: the vehicle enters the station
	 * 
	 * @param subnet The BC9XXX sign involved
	 * @param side The direction taken by the cart
	 */
	public SignPostStationEvent(Subnet subnet, Side side) {
		super(subnet, side);
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
        return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
}
