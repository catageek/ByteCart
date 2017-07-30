package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.Triggable;

/**
 * Abstract class for colllision avoider builders
 */
abstract class AbstractCollisionAvoiderBuilder {

	/**
	 * The first IC attached to the collision avoiders created
	 */
	protected final Triggable ic;

	/**
	 * The location to where the collision avoiders will be attached
	 */
	protected final Location loc;

	AbstractCollisionAvoiderBuilder(Triggable ic, Location loc) {
		this.ic = ic;
		this.loc = loc;
	}

	/**
	 * Get the location to where the collision avoiders created will be attached
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return this.loc;
	}

	/**
	 * Get the IC to which the collision avoiders created will be attached
	 *
	 *
	 * @return the IC
	 */
	public Triggable getIc() {
		return ic;
	}

}
