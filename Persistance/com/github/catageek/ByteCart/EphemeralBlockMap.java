package com.github.catageek.ByteCart;

/*
 * HashMap that keeps entries during "duration" ticks
 */

public final class EphemeralBlockMap<K, T> extends BlockMap<K, T> {
	
	private int Duration;

	public EphemeralBlockMap(int duration) {
		super();
		Duration = duration;
	}
	
	@Override
	public boolean createEntry(K block, T id) {
		if ( this.getMap().put(block, id) == null ) {
			ByteCart.myPlugin.getDelayedThreadManager().renewAsync(block, this.Duration, new Suppress(this, block));
			return true;
		}
		return false;
	}

	/*
	 * update the value associated with key 'block' and reset timer 
	 */
	@Override
	public final boolean updateValue(K block, T id) {
		ping(block);
		return this.getMap().put(block, id) == null;
	}

	/*
	 * reset timer
	 */
	public final void ping(K block) {
		ByteCart.myPlugin.getDelayedThreadManager().renewAsync(block, this.Duration, new Suppress(this, block));
	}

	private final class Suppress implements Runnable {
		
		private final K block;
		private final BlockMap<K, T> map;
		
		public Suppress(EphemeralBlockMap<K, T> map, K block) {
			this.block = block;
			this.map = map;
		}

		@Override
		public void run() {
			map.getMap().remove(block);
		}
		
	}

}
