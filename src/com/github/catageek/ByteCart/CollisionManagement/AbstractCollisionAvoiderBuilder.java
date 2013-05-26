package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.Triggable;

public abstract class AbstractCollisionAvoiderBuilder {

	protected final Triggable ic;

	protected final Location loc;

	public AbstractCollisionAvoiderBuilder(Triggable ic, Location loc) {
		this.ic = ic;
		this.loc = loc;
	}

	public Location getLocation() {
		return this.loc;
	}

	public Triggable getIc() {
		return ic;
	}

}
