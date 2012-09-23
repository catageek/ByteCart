package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public final class DelayedThreadManager {

	final private BlockMap<Integer> DelayedThread = new BlockMap<Integer>();

	/*
	 * create a release task
	 */
	protected final void createReleaseTask(Block block, int duration, Runnable ReleaseTask) {
		int id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Execute(this, ReleaseTask, block)
				, duration);

		// the id of the thread is stored in a static map
		this.DelayedThread.createEntry(block, id);
	}

	protected final boolean hasReleaseTask(Block block) {
		return this.DelayedThread.hasEntry(block);
	}


	/*
	 * Renew the timer of release task
	 */
	protected final void renew(Block block, int duration, Runnable ReleaseTask) {
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

	private final void free(Block block) {
		this.DelayedThread.deleteEntry(block);
	}

	private final class Execute implements Runnable {

		private final Runnable task;
		private final DelayedThreadManager dtm;
		private final Block block;

		public Execute(DelayedThreadManager dtm, Runnable task, Block block) {
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
