package com.github.catageek.ByteCart.CollisionManagement;

import com.github.catageek.ByteCart.Signs.TriggeredSign;

public interface CollisionAvoider {
	public int getSecondpos();
	public void Add(TriggeredSign t);
//	public void WishToGo(Side s, boolean isTrain);
}
