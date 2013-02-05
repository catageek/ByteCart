package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;
import com.github.catageek.ByteCart.Routing.Updater;

/**
 * Event triggered when a region updater modifies the address
 * on a BC8010 sign.
 */
public class UpdaterSetRingEvent extends UpdaterClearRingEvent {

	private static final HandlerList handlers = new HandlerList();
	private final int newring;

	public HandlerList getHandlers() {
        return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }

	/**
	 * Default constructor
	 * 
	 * @param updater The updater involved
	 * @param old The old value of the ring
	 * @param newring The new value of the ring
	 */
	public UpdaterSetRingEvent(Updater updater, int old, int newring) {
		super(updater, old);
		this.newring = newring;
	}

	/**
	 * @return the newring
	 */
	public int getNewring() {
		return newring;
	}
}
