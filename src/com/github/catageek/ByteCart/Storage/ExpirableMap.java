package com.github.catageek.ByteCart.Storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.catageek.ByteCart.ThreadManagement.Expirable;

public final class ExpirableMap<K, T> extends Expirable<K> {

	private final Map<K, T> Map = Collections.synchronizedMap(new HashMap<K,T>());
	
	public ExpirableMap(long duration, boolean isSync, String name) {
		super(duration, isSync, name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void expire(Object... objects) {
		((Map<K,?>)objects[1]).remove((K)objects[0]);
	}

	public boolean put(K key, T value, boolean reset) {
//		if(ByteCart.debug)
//			ByteCart.log.info("ByteCart: create ephemeral key (" + key +") in " + this.getName() + " for " + this.getDuration() + " ticks");
		if (reset)
			this.reset(key, key, Map);
		return (Map.put(key, value) == null);
	}

	public boolean put(K key, T value) {
		return this.put(key, value, true);
	}
	
	@Override
	public void reset(K key, Object...objects) {
		super.reset(key, key, Map);
	}
	
	public final void remove(K key) {
		Map.remove(key);
		this.cancel(key);
	}


	public T get(K key) {
		return Map.get(key);
	}
	
	public boolean contains(K key) {
		return Map.containsKey(key);
	}
	
	public void clear() {
		Map.clear();
	}
}
