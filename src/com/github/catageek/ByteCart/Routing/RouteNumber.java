package com.github.catageek.ByteCart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.github.catageek.ByteCartAPI.ByteCartAPI;
import com.github.catageek.ByteCartAPI.Wanderer.RouteValue;




/**
 * A track number on ByteCartAPI.MAXRINGLOG bits
 */
final class RouteNumber extends RoutingTableContent<RouteNumber>
implements Comparable<RouteNumber>, Externalizable, RouteValue {

	
	private static final int rlength = ByteCartAPI.MAXRINGLOG;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8112012047943458459L;

	public RouteNumber() {
		super(rlength);
	}
	
	RouteNumber(int route) {
		super(route, rlength);
	}
	
	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeShort(this.value());
	}
	
	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.setValue(in.readShort() & ((1 << rlength) - 1));
	}
}
