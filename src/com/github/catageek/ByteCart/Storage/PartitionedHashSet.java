package com.github.catageek.ByteCart.Storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A set containing powers of 2 of an integer
 * 
 * @param <E> A partitionable type
 */
public class PartitionedHashSet<E extends Partitionable> extends HashSet<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7798172721619367114L;

	public PartitionedHashSet() {
	}

	public PartitionedHashSet(Collection<? extends E> c) {
		super(c);
	}

	public PartitionedHashSet(int initialCapacity) {
		super(initialCapacity);
	}

	public PartitionedHashSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	/**
	 * Get the addition of values from the set
	 *
	 * @return the value
	 */
	public final int getPartitionedValue() {
		int ret = 0;
		Iterator<E> it = this.iterator();
		while (it.hasNext()) {
			ret |= it.next().getAmount();
		}
		return ret;
	}
}
