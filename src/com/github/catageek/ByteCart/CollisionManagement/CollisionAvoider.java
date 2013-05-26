package com.github.catageek.ByteCart.CollisionManagement;

import com.github.catageek.ByteCart.Signs.Triggable;

public interface CollisionAvoider {
	public int getSecondpos();
	public void Add(Triggable t);
//	public void WishToGo(Side s, boolean isTrain);
}
