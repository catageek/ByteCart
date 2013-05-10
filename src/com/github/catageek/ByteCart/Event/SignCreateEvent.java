package com.github.catageek.ByteCart.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.github.catageek.ByteCart.HAL.IC;

/**
 * Event triggered when a player creates a valid sign.
 */
public class SignCreateEvent extends BCEvent {

	private final Player player;
	private final String[] strings;

	public SignCreateEvent(IC ic, Player player, String[] strings) {
		super(ic);
		this.player = player;
		this.strings = strings;
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

	/**
	 * @return the player that created the block
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the lines of the sign
	 */
	public String[] getStrings() {
		return strings;
	}
}
