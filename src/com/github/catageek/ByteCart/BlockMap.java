package com.github.catageek.ByteCart;

import java.util.Collections;
import java.util.HashMap;
import org.bukkit.block.Block;

public class BlockMap<T> {
	
	final private java.util.Map<org.bukkit.block.Block,T> Map = Collections.synchronizedMap(new HashMap<org.bukkit.block.Block,T>());

	/*
	 * creates a token with a pair of keys (block, id)
	 */
	public boolean createEntry(Block block, T id) {
		return this.getMap().put(block, id) == null;
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
	
	public boolean updateValue(Block block, T id) {
		return this.createEntry(block, id);
	}

	/**
	 * @return the map
	 */
	public java.util.Map<org.bukkit.block.Block,T> getMap() {
		return Map;
	}
}
