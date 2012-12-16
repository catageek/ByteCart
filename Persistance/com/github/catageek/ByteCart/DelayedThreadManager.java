package com.github.catageek.ByteCart;

import org.bukkit.scheduler.BukkitTask;

public final class DelayedThreadManager<K> {

	final private class IdRunnable {
		public final int id;
		public final Runnable task;
		
		public IdRunnable(int id, Runnable task) {
			this.id = id;
			this.task = task;
		}
	}
	
	final private BlockMap<K, IdRunnable> DelayedThread = new BlockMap<K, IdRunnable>();

	/*
	 * create a release task
	 */
	private synchronized final void createReleaseTask(K key, int duration, Runnable ReleaseTask) {
		
		Runnable task = new Execute(this, ReleaseTask, key);
		int id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, task
				, duration);

		// the id of the thread and the task are stored in a static map
		this.DelayedThread.createEntry(key, new IdRunnable(id, task));
	}

	private synchronized final void createUnsynchronizedReleaseTask(K key, int duration, Runnable ReleaseTask) {
		Runnable task = new Execute(this, ReleaseTask, key);
		BukkitTask bt = ByteCart.myPlugin.getServer().getScheduler().runTaskLaterAsynchronously(ByteCart.myPlugin, task, duration);

		// the id of the thread is stored in a static map
		this.DelayedThread.createEntry(key, new IdRunnable(bt.getTaskId(), task));
	}


	private final boolean hasReleaseTask(K key) {
		return this.DelayedThread.hasEntry(key);
	}


	/*
	 * Renew the timer of release task
	 */
	protected synchronized final void renew(K key, int duration, Runnable ReleaseTask) {
		if(this.hasReleaseTask(key)) {
			// we cancel the release task
			ByteCart.myPlugin.getServer().getScheduler().cancelTask((Integer) this.DelayedThread.getValue(key).id);
			// we schedule a new one
			int id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, this.DelayedThread.getValue(key).task, duration);
			// we update the hashmap
			this.DelayedThread.updateValue(key, new IdRunnable(id, this.DelayedThread.getValue(key).task));
		}
		else {
			this.createReleaseTask(key, duration, ReleaseTask);
		}
	}

	protected synchronized final void renewAsync(K key, int duration, Runnable ReleaseTask) {
		if(this.hasReleaseTask(key)) {
			// we cancel the release task
			ByteCart.myPlugin.getServer().getScheduler().cancelTask((Integer) this.DelayedThread.getValue(key).id);
			// we schedule a new one
			//int id = ByteCart.myPlugin.getServer().getScheduler().scheduleAsyncDelayedTask(ByteCart.myPlugin, new Execute(this, ReleaseTask, key), duration);
			BukkitTask bt = ByteCart.myPlugin.getServer().getScheduler().runTaskLaterAsynchronously(ByteCart.myPlugin, this.DelayedThread.getValue(key).task, duration);
			// we update the hashmap
			this.DelayedThread.updateValue(key, new IdRunnable(bt.getTaskId(), this.DelayedThread.getValue(key).task));
		}
		else {
			this.createUnsynchronizedReleaseTask(key, duration, ReleaseTask);
		}
	}

	private final void free(K key) {
		this.DelayedThread.deleteEntry(key);
	}

	private final class Execute implements Runnable {

		private final Runnable task;
		private final DelayedThreadManager<K> dtm;
		private final K key;

		public Execute(DelayedThreadManager<K> dtm, Runnable task, K key) {
			this.task = task;
			this.dtm = dtm;
			this.key = key;
		}

		@Override
		public void run() {
			this.dtm.free(key);			
			this.task.run();
		}

	}

}
