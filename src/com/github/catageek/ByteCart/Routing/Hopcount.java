package com.github.catageek.ByteCart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.HAL.VirtualRegistry;


final class Hopcount implements Externalizable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8527436068446548316L;
	private final RegistryBoth value = new VirtualRegistry(8);

	public Hopcount() {
	}
	
	Hopcount(int amount) {
		this.value.setAmount(amount);
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeByte(this.value.getAmount());
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.value.setAmount(in.readUnsignedByte());
	}

	final int getValue() {
		return value.getAmount();
	}
}
