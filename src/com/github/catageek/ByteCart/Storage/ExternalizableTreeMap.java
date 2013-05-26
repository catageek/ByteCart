package com.github.catageek.ByteCart.Storage;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class ExternalizableTreeMap<K extends Externalizable, V extends Externalizable>
	extends TreeMap<K, V> implements Externalizable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1074583778619610579L;

	public ExternalizableTreeMap() {
		super();
	}

	public ExternalizableTreeMap(Comparator<? super K> comparator) {
		super(comparator);
	}

	public ExternalizableTreeMap(Map<? extends K, ? extends V> m) {
		super(m);
	}

	public ExternalizableTreeMap(SortedMap<K, ? extends V> arg0) {
		super(arg0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput s) throws IOException,
	ClassNotFoundException {
		// Read in size
		int size = s.readShort();
		
		for (int i = 0; i < size; i++) {
			K key = (K) s.readObject();
			V value = (V) s.readObject();
			this.put(key, value);
		}

	}

	@Override
	public void writeExternal(ObjectOutput s) throws IOException {
		// Write out size (number of Mappings)
		s.writeShort(size());

		// Write out keys and values (alternating)
		for (Iterator<Map.Entry<K,V>> i = entrySet().iterator(); i.hasNext(); ) {
			Map.Entry<K,V> e = i.next();
			s.writeObject(e.getKey());
			s.writeObject(e.getValue());
		}
	}
}
