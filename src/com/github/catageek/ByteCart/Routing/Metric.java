package com.github.catageek.ByteCart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * The metric component of a routing table entry
 */
public final class Metric implements Comparable<Metric>, Externalizable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7625856925617432369L;
	private Delay delay;

	public Metric() {
	}

	Metric(Metric m) {
		this(m.delay.getValue());
	}

	public Metric(int delay) {
		this.delay = new Delay(delay);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Metric o) {
		return new CompareToBuilder().append(value(), o.value()).toComparison(); 
	}

	/**
	 * @return the value
	 */
	public int value() {
		return delay.getValue();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
	ClassNotFoundException {
		delay = (Delay) in.readObject();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(delay);
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
		if (!(o instanceof Metric))
			return false;

		Metric rhs = (Metric) o;

		return new EqualsBuilder().append(value(),rhs.value()).isEquals();
	}
}
