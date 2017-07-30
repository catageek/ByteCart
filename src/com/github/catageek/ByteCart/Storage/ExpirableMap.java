package com.github.catageek.ByteCart.Storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.catageek.ByteCart.ThreadManagement.Expirable;

/**
 * A map in which elements are deleted after a timeout
 * 
 * @param <K> the type of keys of the set
 * @param <T> the type of values of the set
 */
public final class ExpirableMap<K, T> extends Expirable<K> {

	private final Map<K, T> Map = Collections.synchronizedMap(new HashMap<K,T>());
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.ThreadManagement.Expirable#Expirable(long, boolean, String)
	 */
	public ExpirableMap(long duration, boolean isSync, String name) {
		super(duration, isSync, name);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.ThreadManagement.Expirable#expire(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void expire(Object... objects) {
		((Map<K,?>)objects[1]).remove(objects[0]);
	}

	/**
	 * Add an element to the map
	 *
	 * @param key key of the element
	 * @param value value of the element
	 * @param reset must be set to true to reset the timeout to initial value, false otherwise
	 * @return true if the element was added
	 */
	private boolean put(K key, T value, boolean reset) {
//		if(ByteCart.debug)
//			ByteCart.log.info("ByteCart: create ephemeral key (" + key +") in " + this.getName() + " for " + this.getDuration() + " ticks");
		if (reset)
			this.reset(key, key, Map);
		return (Map.put(key, value) == null);
	}

	/**
	 * Add an element to the map
	 *
	 * @param key key of the element
	 * @param value value of the element
	 * @return true if the element was added
	 */
	public boolean put(K key, T value) {
		return this.put(key, value, true);
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.ThreadManagement.Expirable#reset(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public void reset(K key, Object...objects) {
		super.reset(key, key, Map);
	}
	
	/**
	 * Remove an element from the map
	 *
	 * @param key the key of the element
	 */
	public final void remove(K key) {
		Map.remove(key);
		this.cancel(key);
	}


	/**
	 * Get the value of the element having a specific key
	 *
	 * @param key the key of the element
	 * @return the value
	 */
	public T get(K key) {
		return Map.get(key);
	}
	
	/**
	 * Tell if the map contains an element
	 *
	 * @param key the element to check
	 * @return true if the element is in the map
	 */
	public boolean contains(K key) {
		return Map.containsKey(key);
	}
	
	/**
	 * Remove all the elements of the map
	 *
	 */
	public void clear() {
		Map.clear();
	}
	
	/**
	 * Tell if the map is empty
	 *
	 * @return true if the map is empty
	 */
	public boolean isEmpty() {
		return Map.isEmpty();
	}
}
