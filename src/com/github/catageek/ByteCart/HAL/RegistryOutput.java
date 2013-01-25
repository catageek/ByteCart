package com.github.catageek.ByteCart.HAL;

public interface RegistryOutput extends Registry {
	public void setBit(int index, boolean value);
	public void setAmount(int amount);
}
