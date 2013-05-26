package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.Triggable;

public class RouterCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public RouterCollisionAvoiderBuilder(Triggable ic, Location loc) {
		super(ic, loc);
	}

	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {
		return (T) new StraightRouter (this.ic.getCardinal().getOppositeFace(), this.loc);
	}


}
