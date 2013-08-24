package com.github.catageek.ByteCart.HAL;

import java.io.Serializable;

/**
 * A registry with an internal implementation
 */
public class VirtualRegistry implements RegistryBoth, Serializable {
	
	// bit index 0 est le bit de poids fort
	
	private static final long serialVersionUID = -7296392835005177288L;
	private int Virtual = 0;
	private final int Length;
	
	/**
	 * @param length the length of the registry to create
	 */
	public VirtualRegistry(int length) {
		this.Length = length;

		/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : creating VirtualRegistry of " + this.length() + " bit(s).");
*/
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryOutput#setBit(int, boolean)
	 */
	@Override
	public void setBit(int index, boolean value) {

		if(value)
			this.Virtual |= 1 << (length() - index - 1);
		else
			this.Virtual &= ~(1 << (length() - index - 1));					
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryInput#getBit(int)
	 */
	@Override
	public boolean getBit(int index) {
		if (((this.Virtual >> (length() - index - 1)) & 1) == 0)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#length()
	 */
	@Override
	public int length() {
		return this.Length;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#getAmount()
	 */
	@Override
	public int getAmount() {
		return Virtual;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryOutput#setAmount(int)
	 */
	@Override
	public void setAmount(int amount) {
		this.Virtual = amount % (1 << this.length());
	}

}
