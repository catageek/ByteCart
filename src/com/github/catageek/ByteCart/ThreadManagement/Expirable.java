package com.github.catageek.ByteCart.ThreadManagement;

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
	
	public void reset(K key, Object...objects) {
		(new BCBukkitRunnable<K>(this, key)).renewTaskLater(objects);
	}

	public final void cancel(K key) {
		(new BCBukkitRunnable<K>(this, key)).cancel();
	}

	protected final Map<K, BukkitTask> getThreadMap() {
		return ThreadMap;
	}

	public final long getDuration() {
		return Duration;
	}

	public final boolean isSync() {
		return IsSync;
	}

	public final String getName() {
		return name;
	}


}
