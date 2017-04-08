package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.Triggable;

/**
 * A builder for router collision avoider
 */
public class RouterCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	private boolean IsOldVersion;

	public RouterCollisionAvoiderBuilder(Triggable ic, Location loc, boolean isOldVersion) {
		super(ic, loc);
		this.IsOldVersion = isOldVersion;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder#getCollisionAvoider()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends CollisionAvoider> T getCollisionAvoider() {
		return (T) new StraightRouter (this.ic.getCardinal().getOppositeFace(), this.loc, this.IsOldVersion);
	}


}
