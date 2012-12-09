package com.github.catageek.ByteCart;

public interface ModifiableRunnable<T> extends Runnable {
	public void SetParam(T t);
}
