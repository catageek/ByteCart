package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.EventManagement.TriggeredIC;

public class SimpleCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public SimpleCollisionAvoiderBuilder(TriggeredIC ic, Location loc) {
		super(ic, loc);
	}



	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {

		return (T) new SimpleCollisionAvoider (this.ic, this.loc);
	}




}
