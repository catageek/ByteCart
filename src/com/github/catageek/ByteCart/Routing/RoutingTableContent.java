package com.github.catageek.ByteCart.Routing;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.HAL.VirtualRegistry;

abstract class RoutingTableContent<T extends RoutingTableContent<T>> implements Comparable<T> {

	/**
	 * 
	 */
	private final int length;
	private final RegistryBoth data;

	final int size() {
		return length;
	}

	RoutingTableContent(int length) {
		this.length = length;
		data = new VirtualRegistry(length);
	}
	
	RoutingTableContent(int amount, int length) {
		this(length);
		this.data.setAmount(amount);
	}
	
	protected void setValue(int amount) {
		data.setAmount(amount);
	}

	final int value() {
		return data.getAmount();
	}

	@Override
	public int compareTo(T o) {
		return new CompareToBuilder().append(value(), o.value()).toComparison(); 
	}

	@Override
	public int hashCode() {
		return value();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof RoutingTableContent<?>))
			return false;
		
		RoutingTableContent<?> rhs = (RoutingTableContent<?>) o;
		
		return new EqualsBuilder().append(value(),rhs.value()).isEquals();
	}
}