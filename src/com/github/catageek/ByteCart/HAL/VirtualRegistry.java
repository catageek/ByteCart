package com.github.catageek.ByteCart.HAL;

public class VirtualRegistry implements RegistryBoth {
	
	// bit index 0 est le bit de poids fort
	
	private int Virtual = 0;
	private final int Length;
	
	public VirtualRegistry(int length) {
		this.Length = length;

		/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : creating VirtualRegistry of " + this.length() + " bit(s).");
*/
	}

	@Override
	public void setBit(int index, boolean value) {

		if(value)
			this.Virtual |= 1 << (length() - index - 1);
		else
			this.Virtual &= ~(1 << (length() - index - 1));					
	}

	@Override
	public boolean getBit(int index) {
		if (((this.Virtual >> (length() - index - 1)) & 1) == 0)
			return false;
		return true;
	}

	@Override
	public int length() {
		return this.Length;
	}

	@Override
	public int getAmount() {
		return Virtual;
	}

	@Override
	public void setAmount(int amount) {
		this.Virtual = amount % (1 << this.length());
	}

}
