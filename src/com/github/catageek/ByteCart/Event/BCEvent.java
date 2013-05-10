package com.github.catageek.ByteCart.Event;

import org.bukkit.event.Event;

import com.github.catageek.ByteCart.HAL.IC;

/**
 * A container class for all events
 */
public abstract class BCEvent extends Event {
	public BCEvent(IC ic) {
		super();
		this.ic = ic;
	}

	/**
	 * @return the component
	 */
	public IC getIc() {
		return ic;
	}

	private final IC ic;
	
	
}
