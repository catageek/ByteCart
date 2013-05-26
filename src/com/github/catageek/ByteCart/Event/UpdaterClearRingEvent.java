package com.github.catageek.ByteCart.Event;

import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.Routing.Updater;

/**
 * Event triggered when a region reset updater clears the address
 * of a BC8010 sign.
 */
public class UpdaterClearRingEvent extends UpdaterEvent {

	private static final HandlerList handlers = new HandlerList();
	private final int old;

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
	 */
	public UpdaterClearRingEvent(Updater updater, int old) {
		super(updater);
		this.old = old;
	}

	/**
	 * Get the face of the router where the sign is cleared
	 *
	 * @return The face of the router
	 */
	public final BlockFace getFrom() {
		return getUpdater().getFrom().getBlockFace();
	}
	
	/**
	 * @return The ring number
	 */
	public int getOldRing() {
		return old;
	}
}
