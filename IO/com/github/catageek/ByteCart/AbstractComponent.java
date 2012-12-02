package com.github.catageek.ByteCart;

import org.bukkit.Location;

public abstract class AbstractComponent implements Component {
	private final Location location;
	
	protected AbstractComponent(Location loc) {
		location = loc;
	}

	public Location getLocation() {
		return location;
	}

}
