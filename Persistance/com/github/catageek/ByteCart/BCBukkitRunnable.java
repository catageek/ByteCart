package com.github.catageek.ByteCart;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author catageek
 *
 * @param <K> key used in the map or set
 */
public final class BCBukkitRunnable<K> {

	private final Expirable<K> Expirable;
	private final K Key;

	public BCBukkitRunnable(Expirable<K> expirable, K key) {
		this.Expirable = expirable;
		this.Key = key;
	}

	public BukkitTask renewTaskLater(Object...objects) {
		BukkitTask task;
		if(! Expirable.getThreadMap().containsKey(Key)) {
			task = (Expirable.isSync() ? this.runTaskLater(objects)
					: this.runTaskLaterAsynchronously(objects));

		}
		else {
			BukkitTask old = Expirable.getThreadMap().get(Key);
			BukkitRunnable runnable = new Expire(Expirable, Key, objects);

			old.cancel();

			if (old.isSync())
				task = runnable.runTaskLater(ByteCart.myPlugin, Expirable.getDuration());
			else
				task = runnable.runTaskLaterAsynchronously(ByteCart.myPlugin, Expirable.getDuration());
		}

		//		old = null;
		Expirable.getThreadMap().put(Key, task);

		return task;
	}

	public void cancel() {
		if(Expirable.getThreadMap().containsKey(Key))
			Expirable.getThreadMap().remove(Key).cancel();
	}

	public BukkitTask runTaskLater(Object...objects) {
		BukkitRunnable runnable = new Expire(Expirable, Key, objects);
		org.bukkit.scheduler.BukkitTask task = runnable.runTaskLater(ByteCart.myPlugin, Expirable.getDuration());
		return task;
	}

	public BukkitTask runTaskLaterAsynchronously(Object...objects) {
		BukkitRunnable runnable = new Expire(Expirable, Key, objects);
		org.bukkit.scheduler.BukkitTask task = runnable.runTaskLaterAsynchronously(ByteCart.myPlugin, Expirable.getDuration());
		return task;
	}

	private final class Expire extends BukkitRunnable {

		private final Expirable<K> Expirable;
		private final K key;
		private final Object[] params;

		public Expire(Expirable<K> expirable, K key, Object...objects) {
			this.key = key;
			this.Expirable = expirable;
			this.params = objects;
		}

		@Override
		public void run() {
			Expirable.getThreadMap().remove(key);
			Expirable.expire(params);
		}

	}
}
