package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Signs.Triggable;

/**
 * A builder for simple collision avoider, i.e for a T cross-roads
 */
public class SimpleCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public SimpleCollisionAvoiderBuilder(Triggable ic, Location loc) {
		super(ic, loc);
	}



	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder#getCollisionAvoider()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {

		return (T) new SimpleCollisionAvoider (this.ic, this.loc);
	}




}
