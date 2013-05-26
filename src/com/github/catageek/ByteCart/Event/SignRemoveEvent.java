package com.github.catageek.ByteCart.Event;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.HAL.IC;

/**
 * Event triggered when a sign is physically removed from the world
 */
public final class SignRemoveEvent extends BCEvent {

	public SignRemoveEvent(IC ic, Entity entity) {
		super(ic);
		this.entity = entity;
	}

	private final Entity entity;

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

	/**
	 * @return the entity that broke the block
	 */
	public Entity getEntity() {
		return entity;
	}

}
