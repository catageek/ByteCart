package com.github.catageek.ByteCart;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Block;

public class BlockMap<T> {
	
	final private ConcurrentHashMap<org.bukkit.block.Block,T> Map = new ConcurrentHashMap<org.bukkit.block.Block,T>();

	/*
	 * creates a token with a pair of keys (block, id)
	 */
	public boolean createEntry(Block block, T id) {
		if ( ! this.getMap().containsKey(block) ) {
			this.getMap().put(block, id);
			return true;
		}
		return false;
	}

	/*
	 * delete the token associated with key 'block' 
	 */
	public void deleteEntry(Block block) {
		this.getMap().remove(block);
	}

	/*
	 * tells if there is a token with key 'block'
	 */
	public final boolean hasEntry(Block block) {
		return this.getMap().containsKey(block);
	}

	/*
	 * returns the vehicle id associated with the block
	 */
	public final T getValue(Block block) {
		return this.getMap().get(block);
	}
	
	public void updateValue(Block block, T id) {
		this.getMap().replace(block, id);
	}

	/**
	 * @return the map
	 */
	public ConcurrentHashMap<org.bukkit.block.Block,T> getMap() {
		return Map;
	}
}
