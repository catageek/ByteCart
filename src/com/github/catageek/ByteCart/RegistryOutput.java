package com.github.catageek.ByteCart;

public interface RegistryOutput extends Registry {
	public void setBit(int index, boolean value);
	public void setAmount(int amount);
}
