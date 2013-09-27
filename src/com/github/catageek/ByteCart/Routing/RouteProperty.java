package com.github.catageek.ByteCart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.github.catageek.ByteCart.Storage.PartitionedHashSet;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

/**
 * The content of a routing table entry
 */
final class RouteProperty implements Externalizable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3548458365177323172L;

	private TreeMap<Metric,PartitionedHashSet<DirectionRegistry>> map = new TreeMap<Metric,PartitionedHashSet<DirectionRegistry>>();

	public RouteProperty() {
	}

	/**
	 * @return the map associating each metric with a DirectionRegistry
	 */
	final TreeMap<Metric,PartitionedHashSet<DirectionRegistry>> getMap() {
		return map;
	}

	/**
	 * Decompose the registry value to a set of DirectionRegistry
	 * 
	 * @param value the value to decompose
	 * @return the set
	 */
	public final PartitionedHashSet<DirectionRegistry> getPartitionedHashSet(int value) {
		int reg = value;
		int cur = 1;
		PartitionedHashSet<DirectionRegistry> set = new PartitionedHashSet<DirectionRegistry>();
		while (reg != 0) {
			if ((reg & 1) !=0) {
				set.add(new DirectionRegistry(cur));
			}
			cur = cur << 1;
			reg = reg >> 1;
		}
		return set;
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput arg0) throws IOException,
	ClassNotFoundException {
		int size = arg0.readInt();
		for (int i = 0; i < size; i++) {
			int value = arg0.readUnsignedShort();
			PartitionedHashSet<DirectionRegistry> set;
			if (! (set = getPartitionedHashSet(value & 15)).isEmpty()) {
				map.put(new Metric(value >> 4), set);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput arg0) throws IOException {
		arg0.writeInt(map.size());
		Iterator<Entry<Metric, PartitionedHashSet<DirectionRegistry>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Metric, PartitionedHashSet<DirectionRegistry>> entry = it.next();
			arg0.writeShort((entry.getKey().value() << 4) + entry.getValue().getPartitionedValue());
		}
	}

}
