package com.github.catageek.ByteCart;

import org.bukkit.scheduler.BukkitTask;

public final class DelayedThreadManager<K> {

	final private BlockMap<K, Integer> DelayedThread = new BlockMap<K, Integer>();

	/*
	 * create a release task
	 */
	private synchronized final void createReleaseTask(K block, int duration, Runnable ReleaseTask) {
		int id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Execute(this, ReleaseTask, block)
				, duration);

		// the id of the thread is stored in a static map
		this.DelayedThread.createEntry(block, id);
	}

	protected synchronized final void createUnsynchronizedReleaseTask(K block, int duration, Runnable ReleaseTask) {
//		int id = ByteCart.myPlugin.getServer().getScheduler().scheduleAsyncDelayedTask(ByteCart.myPlugin, new Execute(this, ReleaseTask, block)
//				, duration);
		BukkitTask bt = ByteCart.myPlugin.getServer().getScheduler().runTaskLaterAsynchronously(ByteCart.myPlugin, new Execute(this, ReleaseTask, block), duration);

		// the id of the thread is stored in a static map
		this.DelayedThread.createEntry(block, bt.getTaskId());
	}


	protected final boolean hasReleaseTask(K block) {
		return this.DelayedThread.hasEntry(block);
	}


	/*
	 * Renew the timer of release task
	 */
	protected synchronized final void renew(K block, int duration, Runnable ReleaseTask) {
		if(this.hasReleaseTask(block)) {
			// we cancel the release task
			ByteCart.myPlugin.getServer().getScheduler().cancelTask((Integer) this.DelayedThread.getValue(block));
			// we schedule a new one
			int id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Execute(this, ReleaseTask, block), duration);
			// we update the hashmap
			this.DelayedThread.updateValue(block, id);
		}
		else {
			this.createReleaseTask(block, duration, ReleaseTask);
		}
	}

	protected synchronized final void renewAsync(K block, int duration, Runnable ReleaseTask) {
		if(this.hasReleaseTask(block)) {
			// we cancel the release task
			ByteCart.myPlugin.getServer().getScheduler().cancelTask((Integer) this.DelayedThread.getValue(block));
			// we schedule a new one
			//int id = ByteCart.myPlugin.getServer().getScheduler().scheduleAsyncDelayedTask(ByteCart.myPlugin, new Execute(this, ReleaseTask, block), duration);
			BukkitTask bt = ByteCart.myPlugin.getServer().getScheduler().runTaskLaterAsynchronously(ByteCart.myPlugin, new Execute(this, ReleaseTask, block), duration);
			// we update the hashmap
			this.DelayedThread.updateValue(block, bt.getTaskId());
		}
		else {
			this.createUnsynchronizedReleaseTask(block, duration, ReleaseTask);
		}
	}

	private final void free(K block) {
		this.DelayedThread.deleteEntry(block);
	}

	private final class Execute implements Runnable {

		private final Runnable task;
		private final DelayedThreadManager<K> dtm;
		private final K block;

		public Execute(DelayedThreadManager<K> dtm, Runnable task, K block) {
			this.task = task;
			this.dtm = dtm;
			this.block = block;
		}

		@Override
		public void run() {
			this.dtm.free(block);			
			this.task.run();
		}

	}

}
