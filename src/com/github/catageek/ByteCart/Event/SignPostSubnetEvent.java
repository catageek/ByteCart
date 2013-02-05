package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Signs.Subnet;

/**
 * Event triggered when a vehicle is using a subnet sign,
 * after the collision avoidance layer operations.
 * 
 * The direction is definitive.
 */
public class SignPostSubnetEvent extends BCSignEvent {

	/**
	 * Default constructor
	 * 
	 * The side parameter may be:
	 * - LEFT: the vehicle will not enter the subnet OR will not leave the subnet if it was inside
	 * - RIGHT: the vehicle enters the subnet OR leaves the subnet if it was inside
	 * 
	 * @param subnet The BC9XXX sign involved
	 * @param side The direction taken by the vehicle
	 */
	public SignPostSubnetEvent(Subnet subnet, Side side) {
		super();
		this.subnet = subnet;
		this.side = side;
	}

	
	protected final Subnet subnet;
    protected Side side;
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
        return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Event.BCSignEvent#getSign()
	 */
	@Override
	protected BCSign getSign() {
		return subnet;
	}

	/**
	 * Get the direction taken by the vehicle
	 *
	 * @return A value from SimpleCollisionAvoider.Side enum
	 */
	public Side getSide() {
		return side;
	}
	
	/**
	 * Get the mask of the subnet.
	 * 
	 * Possible values are:
	 * 	- 0: No mask (BC9000)
	 *  - 1: 8-station subnet (BC9008)
	 *  - 2: 4-station subnet (BC9004)
	 *  - 3: 2-station subnet (BC9002)
	 *  - 4: station (BC9001)
	 *
	 * @return The mask of the subnet
	 */
	public int getNetmask() {
		return subnet.getNetmask();
	}

}
