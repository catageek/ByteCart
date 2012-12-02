package com.github.catageek.ByteCart;

public interface RegistryOutput {
	public void setBit(int index, boolean value);
	public void setAmount(int amount);
	public int length();
}
