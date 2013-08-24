package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.Triggable;

/**
 * A builder for router collision avoider
 */
public class RouterCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public RouterCollisionAvoiderBuilder(Triggable ic, Location loc) {
		super(ic, loc);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder#getCollisionAvoider()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {
		return (T) new StraightRouter (this.ic.getCardinal().getOppositeFace(), this.loc);
	}


}
