package com.github.catageek.ByteCart;

import java.util.Collections;
import java.util.HashMap;

public class BlockMap<K, T> {
	
	final private java.util.Map<K,T> Map = Collections.synchronizedMap(new HashMap<K,T>());

	/*
	 * creates a token with a pair of keys (block, id)
	 */
	public boolean createEntry(K block, T id) {
		return this.getMap().put(block, id) == null;
	}

	/*
	 * delete the token associated with key 'block' 
	 */
	public void deleteEntry(K block) {
		this.getMap().remove(block);
	}

	/*
	 * tells if there is a token with key 'block'
	 */
	public final boolean hasEntry(K block) {
		return this.getMap().containsKey(block);
	}

	/*
	 * returns the vehicle id associated with the block
	 */
	public final T getValue(K block) {
		return this.getMap().get(block);
	}
	
	public boolean updateValue(K block, T id) {
		return this.createEntry(block, id);
	}

	/**
	 * @return the map
	 */
	public java.util.Map<K,T> getMap() {
		return Map;
	}
}
