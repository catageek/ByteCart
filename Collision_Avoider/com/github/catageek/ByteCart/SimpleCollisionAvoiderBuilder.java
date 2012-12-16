package com.github.catageek.ByteCart;

import org.bukkit.Location;

public class SimpleCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public SimpleCollisionAvoiderBuilder(TriggeredIC ic, Location loc) {
		super(ic, loc);
	}



	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {

		return (T) new SimpleCollisionAvoider (this.ic, this.loc);
	}




}
