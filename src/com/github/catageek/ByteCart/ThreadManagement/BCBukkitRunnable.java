package com.github.catageek.ByteCart.ThreadManagement;

import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.catageek.ByteCart.ByteCart;

/**
 * @author catageek
 *
 * @param <K> key used in the map or set
 */
final class BCBukkitRunnable<K> {

	private final Expirable<K> Expirable;
	private final K Key;

	BCBukkitRunnable(Expirable<K> expirable, K key) {
		this.Expirable = expirable;
		this.Key = key;
	}

	BukkitTask renewTaskLater(Object...objects) {
		BukkitTask task;
		Map<K,BukkitTask> map = Expirable.getThreadMap();
		synchronized(map) {
			if(! Expirable.getThreadMap().containsKey(Key)) {
				task = (Expirable.isSync() ? this.runTaskLater(objects)
						: this.runTaskLaterAsynchronously(objects));

			}
			else {
				BukkitTask old = Expirable.getThreadMap().get(Key);
				BukkitRunnable runnable = new Expire(Expirable, Key, objects);


				if (old.isSync())
					task = runnable.runTaskLater(ByteCart.myPlugin, Expirable.getDuration());
				else
					task = runnable.runTaskLaterAsynchronously(ByteCart.myPlugin, Expirable.getDuration());
				old.cancel();
			}

			Expirable.getThreadMap().put(Key, task);
		}

		return task;
	}

	void cancel() {
		if(Expirable.getThreadMap().containsKey(Key))
			Expirable.getThreadMap().remove(Key);
	}

	BukkitTask runTaskLater(Object...objects) {
		BukkitRunnable runnable = new Expire(Expirable, Key, objects);
		org.bukkit.scheduler.BukkitTask task = runnable.runTaskLater(ByteCart.myPlugin, Expirable.getDuration());
		return task;
	}

	BukkitTask runTaskLaterAsynchronously(Object...objects) {
		BukkitRunnable runnable = new Expire(Expirable, Key, objects);
		org.bukkit.scheduler.BukkitTask task = runnable.runTaskLaterAsynchronously(ByteCart.myPlugin, Expirable.getDuration());
		return task;
	}

	private final class Expire extends BukkitRunnable {

		private final Expirable<K> expirable;
		private final K key;
		private final Object[] params;

		public Expire(Expirable<K> expirable, K key, Object...objects) {
			this.key = key;
			this.expirable = expirable;
			this.params = objects;
		}

		@Override
		public void run() {
			Map<K,BukkitTask> map = this.expirable.getThreadMap();

			map.remove(key);
			this.expirable.expire(params);
		}

	}

}

