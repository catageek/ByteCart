package com.github.catageek.ByteCart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.github.catageek.ByteCart.ByteCart;



final class RouteNumber extends RoutingTableContent<RouteNumber>
implements Comparable<RouteNumber>, Externalizable {

	
	private static final int rlength = 11;
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
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeShort(this.value());
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.setValue(in.readShort() & ((1 << rlength) - 1));
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : loading route : loading route to " + this.value());
	}
}
