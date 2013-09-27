package com.github.catageek.ByteCart.Routing;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.github.catageek.ByteCartAPI.HAL.RegistryBoth;
import com.github.catageek.ByteCartAPI.HAL.VirtualRegistry;

/**
 * A raw routing table entry, i.e a registry
 * 
 * @param <T> the type of content that will be stored
 */
abstract class RoutingTableContent<T extends RoutingTableContent<T>> implements Comparable<T> {

	/**
	 * 
	 */
	private final int length;
	private final RegistryBoth data;

	/**
	 * @return the size of the registry in bits
	 */
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
	
	/**
	 * @param amount the value to store
	 */
	protected void setValue(int amount) {
		data.setAmount(amount);
	}

	/**
	 * @return the value stored
	 */
	final int value() {
		return data.getAmount();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(T o) {
		return new CompareToBuilder().append(value(), o.value()).toComparison(); 
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return value();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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