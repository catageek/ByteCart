package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.TriggeredSign;

public interface CollisionAvoiderBuilder {
	public <T extends CollisionAvoider> T getCollisionAvoider();
	public Location getLocation();
	public TriggeredSign getIc();

}
