package com.github.catageek.ByteCart.IO;

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
