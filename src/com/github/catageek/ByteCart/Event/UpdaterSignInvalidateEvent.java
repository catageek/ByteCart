package com.github.catageek.ByteCart.Event;

import org.bukkit.event.HandlerList;
import com.github.catageek.ByteCart.Routing.Updater;

/**
 * Event triggered when a sign is invalidated by an updater
 */
public final class UpdaterSignInvalidateEvent extends UpdaterEvent {

	public UpdaterSignInvalidateEvent(Updater updater) {
		super(updater);
	}

	private static final HandlerList handlers = new HandlerList();

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