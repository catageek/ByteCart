package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.Signs.BCRouter;
import com.github.catageek.ByteCart.Signs.BCSign;

/**
 * Event triggered when a vehicle is leaving a router,
 * after the collision avoidance layer operations.
 * 
 * The target track is definitive.
 */
public class SignPostRouteEvent extends BCSignEvent {

    private static final HandlerList handlers = new HandlerList();
    
    private final BCRouter router;

	protected int to;

	/**
	 * Default constructor
	 * 
	 * @param router The router involved
	 * @param to The ring number of the track where the vehicle is currently (not the destination ring) 
	 */
	public SignPostRouteEvent(BCRouter router, int to) {
		super();
		this.router = router;
		this.to = to;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Event#getHandlers()
	 */
	public HandlerList getHandlers() {
        return handlers;
	}

	/**
	 * Needed for Bukkit Event API usage
	 */
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	/**
	 * Get the track from where the vehicle entered the router
	 *
	 * @return The number of the ring
	 */
	public final int getOriginTrack() {
		return router.getOriginTrack();
	}
	
	/**
	 * Get the track to where the vehicle leaves the router
	 *
	 * @return The number of the ring
	 */
	public final int getTargetTrack() {
		return to;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Event.BCSignEvent#getSign()
	 */
	protected final BCSign getSign() {
		return router;
	}
}
