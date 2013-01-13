package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.TriggeredSign;

public abstract class AbstractCollisionAvoiderBuilder {

	protected final TriggeredSign ic;

	protected final Location loc;

	public AbstractCollisionAvoiderBuilder(TriggeredSign ic, Location loc) {
		this.ic = ic;
		this.loc = loc;
	}

	public Location getLocation() {
		return this.loc;
	}

	public TriggeredSign getIc() {
		return ic;
	}

}
