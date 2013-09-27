package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCartAPI.HAL.IC;

/**
 * An IC that a player can right-click should implement this
 */
public interface Clickable extends IC {
	/**
	 * Method called when a player right-clicks the IC
	 *
	 */
	public void click();
}
