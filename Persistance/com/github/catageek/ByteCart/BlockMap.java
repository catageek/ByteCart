package com.github.catageek.ByteCart;

import java.util.Collections;
import java.util.HashMap;

public class BlockMap<K, T> {
	
	final private java.util.Map<K,T> Map = Collections.synchronizedMap(new HashMap<K,T>());

	/*
	 * creates an entry with a pair (key, value)
	 */
	public boolean createEntry(K key, T value) {
		return this.getMap().put(key, value) == null;
	}

	/*
	 * delete the entry associated with key 'key' 
	 */
	public void deleteEntry(K key) {
		this.getMap().remove(key);
	}

	/*
	 * tells if there is a entry with key 'key'
	 */
	public final boolean hasEntry(K key) {
		return this.getMap().containsKey(key);
	}

	/*
	 * returns the value associated with the key
	 */
	public final T getValue(K key) {
		return this.getMap().get(key);
	}
	
	public boolean updateValue(K key, T value) {
		return this.createEntry(key, value);
	}

	/**
	 * @return the map
	 */
	public java.util.Map<K,T> getMap() {
		return Map;
	}
}
