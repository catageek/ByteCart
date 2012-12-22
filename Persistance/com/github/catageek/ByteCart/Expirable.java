package com.github.catageek.ByteCart;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitTask;

/*
 * HashMap that keeps entries during "duration" ticks
 */

public abstract class Expirable<K> {

	private final Map<K, BukkitTask> ThreadMap = Collections.synchronizedMap(new HashMap<K,BukkitTask>());
	private final long Duration;
	private final String name;
	private final boolean IsSync;
	
	abstract public void expire(Object...objects);

	public Expirable(long duration, boolean isSync, String name) {
		super();
		this.Duration = duration;
		this.name = name;
		this.IsSync = isSync;
	}
	
	protected void reset(K key, Object...objects) {
		(new BCBukkitRunnable<K>(this, key)).renewTaskLater(objects);
	}

	protected final void cancel(K key) {
		(new BCBukkitRunnable<K>(this, key)).cancel();
	}

	protected final Map<K, BukkitTask> getThreadMap() {
		return ThreadMap;
	}

	protected final long getDuration() {
		return Duration;
	}

	protected final boolean isSync() {
		return IsSync;
	}

	public String getName() {
		return name;
	}


}
