package com.github.catageek.ByteCart;

import org.bukkit.Location;

public class RouterCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public RouterCollisionAvoiderBuilder(TriggeredIC ic, Location loc) {
		super(ic, loc);
	}

	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {
		return (T) new StraightRouter (this.ic.getCardinal().getOppositeFace(), this.loc);
	}


}
