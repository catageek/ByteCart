package com.github.catageek.ByteCart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.github.catageek.ByteCartAPI.HAL.RegistryBoth;
import com.github.catageek.ByteCartAPI.HAL.VirtualRegistry;

/**
 * A time metric
 */
class Delay implements Externalizable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 970735722356168776L;

	// hard coded value, if modified, change RouteProperty.writeObject() and readObject()
	private final RegistryBoth value = new VirtualRegistry(12);
	
	public Delay() {
	}
	
	Delay(int value) {
		this.value.setAmount(value);
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeShort(this.getValue());
	}
	
	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.value.setAmount(in.readUnsignedShort());
	}

	/**
	 * @return the value
	 */
	int getValue() {
		return value.getAmount();
	}
}
