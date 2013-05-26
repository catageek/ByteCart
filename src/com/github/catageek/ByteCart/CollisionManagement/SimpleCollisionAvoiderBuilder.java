package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.Triggable;

public class SimpleCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public SimpleCollisionAvoiderBuilder(Triggable ic, Location loc) {
		super(ic, loc);
	}



	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {

		return (T) new SimpleCollisionAvoider (this.ic, this.loc);
	}




}
