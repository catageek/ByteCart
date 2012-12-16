package com.github.catageek.ByteCart;

import org.bukkit.Location;

public interface CollisionAvoiderBuilder {
	public <T extends CollisionAvoider> T getCollisionAvoider();
	public Location getLocation();
	public TriggeredIC getIc();

}
