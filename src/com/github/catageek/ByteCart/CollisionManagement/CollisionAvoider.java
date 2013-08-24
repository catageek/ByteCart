package com.github.catageek.ByteCart.CollisionManagement;

import com.github.catageek.ByteCart.Signs.Triggable;

/**
 * A state machine depending of 2 elements
 */
public interface CollisionAvoider {
	/**
	 * Get the value stored as second pos
	 *
	 * @return the value of the second position
	 */
	public int getSecondpos();
	
	/**
	 * Add the second triggered IC to current CollisonAvoider
	 *
	 *
	 * @param t
	 */
	public void Add(Triggable t);
//	public void WishToGo(Side s, boolean isTrain);
}
