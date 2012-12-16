package com.github.catageek.ByteCart;

import org.bukkit.Location;

public abstract class AbstractCollisionAvoiderBuilder {

	protected final TriggeredIC ic;

	protected final Location loc;

	public AbstractCollisionAvoiderBuilder(TriggeredIC ic, Location loc) {
		this.ic = ic;
		this.loc = loc;
	}

	public Location getLocation() {
		return this.loc;
	}

	public TriggeredIC getIc() {
		return ic;
	}

}
