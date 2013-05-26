package com.github.catageek.ByteCart.Storage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.catageek.ByteCart.ThreadManagement.Expirable;

public final class ExpirableSet<K> extends Expirable<K> {

	private final Set<K> Set = Collections.synchronizedSet(new HashSet<K>());
	
	public ExpirableSet(long duration, boolean isSync, String name) {
		super(duration, isSync, name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void expire(Object... objects) {
		((Set<K>)objects[1]).remove((K)objects[0]);
	}

	public boolean add(K key) {
//		if(ByteCart.debug)
//			ByteCart.log.info("ByteCart: create ephemeral key (" + key +") in " + this.getName() + " for " + this.getDuration() + " ticks");
		this.reset(key, key, Set);
		return Set.add(key);
	}
	
	@Override
	public void reset(K key, Object...objects) {
		super.reset(key, key, Set);
	}
	
	public final void remove(K key) {
		Set.remove(key);
		this.cancel(key);
	}


	public boolean contains(K key) {
		return Set.contains(key);
	}
	
	public boolean isEmpty() {
		return Set.isEmpty();
	}
}