package com.github.catageek.ByteCart.Util;

public interface ModifiableRunnable<T> extends Runnable {
	public void SetParam(T t);
}
