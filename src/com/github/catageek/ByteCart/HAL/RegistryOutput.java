package com.github.catageek.ByteCart.HAL;

public interface RegistryOutput {
	public void setBit(int index, boolean value);
	public void setAmount(int amount);
	public int length();
}
