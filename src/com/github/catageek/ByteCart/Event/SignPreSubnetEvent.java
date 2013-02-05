package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Signs.Subnet;

/**
 * Event triggered when a vehicle is using a subnet sign,
 * before the collision avoidance layer operations.
 * 
 * The direction may be modified by collision avoidance layer.
 */
public class SignPreSubnetEvent extends SignPostSubnetEvent {

	/**
	 * Default constructor
	 * 
	 * The side parameter may be:
	 * - LEFT: the vehicle wish not to enter the subnet
	 * - RIGHT: the vehicle wish to enter the subnet OR wish to leave the subnet if it was inside
	 * 
	 * @param subnet The BC9XXX sign involved
	 * @param side The direction wished of the vehicle
	 */
	public SignPreSubnetEvent(Subnet subnet, Side side) {
		super(subnet, side);
	}

	
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
        return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Event.SignPostSubnetEvent#getSign()
	 */
	@Override
	protected BCSign getSign() {
		return subnet;
	}

	/**
	 * Change the direction taken by the vehicle on the fly
	 * This will modify internal state of the sign before actual operations.
	 * This will not change the destination address recorded in the vehicle.
	 * 
	 * The final direction is undefined until routing layer operations occur.
	 *
	 * @param side A value from SimpleCollisionAvoider.Side enum
	 */
	public void setSide(Side side) {
		this.side = side;
	}
}
