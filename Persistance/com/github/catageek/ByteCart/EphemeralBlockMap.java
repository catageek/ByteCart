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
	public boolean createEntry(K key, T value) {
		if ( this.getMap().put(key, value) == null ) {
			ByteCart.myPlugin.getDelayedThreadManager().renewAsync(key, this.Duration, new Suppress(this, key));
			return true;
		}
		return false;
	}

	/*
	 * update the value associated with key 'key' and reset timer 
	 */
	@Override
	public final boolean updateValue(K key, T value) {
		ping(key);
		return this.getMap().put(key, value) == null;
	}

	/*
	 * reset timer
	 */
	public final void ping(K key) {
		ByteCart.myPlugin.getDelayedThreadManager().renewAsync(key, this.Duration, new Suppress(this, key));
	}

	private final class Suppress implements Runnable {
		
		private final K key;
		private final BlockMap<K, T> map;
		
		public Suppress(EphemeralBlockMap<K, T> map, K key) {
			this.key = key;
			this.map = map;
		}

		@Override
		public void run() {
			map.getMap().remove(key);
		}
		
	}

}
