package com.github.catageek.ByteCart.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleMoveEvent;

/**
 * Event trigger when an updater moves from 1 block
 */
public final class UpdaterMoveEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
	private final VehicleMoveEvent event;

	public UpdaterMoveEvent(VehicleMoveEvent event) {
		this.event = event;
	}

	/**
	 * @return the event
	 */
	public VehicleMoveEvent getEvent() {
		return event;
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

}
