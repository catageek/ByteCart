package com.github.catageek.ByteCart.ThreadManagement;

import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.catageek.ByteCart.ByteCart;

/**
 * A class that schedule tasks to manage the automatic removal of elements
 *
 * @param <K> key used in the map or set
 */
final class BCBukkitRunnable<K> {

	private final Expirable<K> Expirable;
	private final K Key;

	/**
	 * @param expirable the Collection implementing Expirable
	 * @param key the key of the collection to which this task will be referenced
	 */
	BCBukkitRunnable(Expirable<K> expirable, K key) {
		this.Expirable = expirable;
		this.Key = key;
	}

	/**
	 * Schedule or reschedule an Expirable task with a specific timeout delay
	 *
	 * @param duration the timeout to set
	 * @param objects arguments to pass to the abstract Expirable.expire() method
	 * @return the BukkitTask scheduled
	 */
	BukkitTask renewTaskLater(long duration, Object...objects) {
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
					task = runnable.runTaskLater(ByteCart.myPlugin, duration);
				else
					task = runnable.runTaskLaterAsynchronously(ByteCart.myPlugin, duration);
				old.cancel();
			}

			Expirable.getThreadMap().put(Key, task);
		}

		return task;
	}

	/**
	 * Schedule or reschedule an Expirable task with the default timeout delay
	 *
	 * @param objects arguments to pass to the abstract Expirable.expire() method
	 * @return the BukkitTask scheduled
	 */
	BukkitTask renewTaskLater(Object...objects) {
		return renewTaskLater(Expirable.getDuration(), objects);
	}
	
	/**
	 * Cancel the task
	 *
	 */
	void cancel() {
		if(Expirable.getThreadMap().containsKey(Key))
			Expirable.getThreadMap().remove(Key);
	}

	/**
	 * Schedule an Expirable task synchronously
	 *
	 * @param objects the arguments to pass to Expirable.expire() method
	 * @return the task scheduled
	 */
	BukkitTask runTaskLater(Object...objects) {
		BukkitRunnable runnable = new Expire(Expirable, Key, objects);
		org.bukkit.scheduler.BukkitTask task = runnable.runTaskLater(ByteCart.myPlugin, Expirable.getDuration());
		return task;
	}

	/**
	 * Schedule an Expirable task asynchronously
	 *
	 * @param objects the arguments to pass to Expirable.expire() method
	 * @return the task scheduled
	 */
	BukkitTask runTaskLaterAsynchronously(Object...objects) {
		BukkitRunnable runnable = new Expire(Expirable, Key, objects);
		org.bukkit.scheduler.BukkitTask task = runnable.runTaskLaterAsynchronously(ByteCart.myPlugin, Expirable.getDuration());
		return task;
	}

	/**
	 * A runnable that will execute the expire() method and clean internal map
	 */
	private final class Expire extends BukkitRunnable {

		private final Expirable<K> expirable;
		private final K key;
		private final Object[] params;

		public Expire(Expirable<K> expirable, K key, Object...objects) {
			this.key = key;
			this.expirable = expirable;
			this.params = objects;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Map<K,BukkitTask> map = this.expirable.getThreadMap();

			map.remove(key);
			this.expirable.expire(params);
		}

	}

}

