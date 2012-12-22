package com.github.catageek.ByteCart.CollisionManagement;

import com.github.catageek.ByteCart.EventManagement.TriggeredIC;

public interface CollisionAvoider {
	public int getSecondpos();
	public void Add(TriggeredIC t);
//	public void WishToGo(Side s, boolean isTrain);
}
