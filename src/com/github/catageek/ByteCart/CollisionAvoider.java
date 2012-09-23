package com.github.catageek.ByteCart;


import com.github.catageek.ByteCart.SimpleCollisionAvoider.Side;

public interface CollisionAvoider {
	public void WishToGo(Side s, boolean b);
	public void Ping();
	public void Add(TriggeredIC t);
	public void setRecentlyUsed(boolean recentlyUsed);
	public void setHasTrain(boolean hasTrain);
}
