package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public final class EphemeralBlockMap<T> extends BlockMap<T> {
	
	private int Duration;

	public EphemeralBlockMap(int duration) {
		super();
		Duration = duration;
	}
	
	@Override
	public boolean createEntry(Block block, T id) {
		if ( ! this.getMap().containsKey(block) ) {
			this.getMap().put(block, id);
			ByteCart.myPlugin.getDelayedThreadManager().createReleaseTask(block, this.Duration, new Suppress(this, block));
			return true;
		}
		return false;
	}

	/*
	 * update the value associated with key 'block' and reset timer 
	 */
	@Override
	public final void updateValue(Block block, T id) {
		this.getMap().replace(block, id);
		ByteCart.myPlugin.getDelayedThreadManager().renew(block, this.Duration, new Suppress(this, block));
	}

	private final class Suppress implements Runnable {
		
		private final Block block;
		private final BlockMap<T> map;
		
		public Suppress(EphemeralBlockMap<T> map, Block block) {
			this.block = block;
			this.map = map;
		}

		@Override
		public void run() {
			map.getMap().remove(block);
		}
		
	}

}
