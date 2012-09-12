package com.github.catageek.ByteCart;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

public final class BlockMap {
	final private ConcurrentHashMap<org.bukkit.block.Block,Integer> TokenMap = new ConcurrentHashMap<org.bukkit.block.Block,Integer>();

	/*
	 * creates a token with a pair of keys (block, id)
	 */
	public final boolean createEntry(Block block, int id) {
		if ( ! this.TokenMap.containsKey(block) ) {
			this.TokenMap.put(block, id);
			return true;
		}
		return false;
	}

	/*
	 * delete the token associated with key 'block' 
	 */
	public final void deleteEntry(Block block) {
		this.TokenMap.remove(block);
	}

	/*
	 * tells if there is a token with keypair (block, vehicle)
	 */
	public final boolean hasEntry(Block block, Vehicle v) {
		if (this.TokenMap.containsKey(block) && v != null && this.TokenMap.get(block) == v.getEntityId()) {
			return true;
		}
		return false;
	}
	/*
	 * tells if there is a token with key 'block'
	 */
	public final boolean hasEntry(Block block) {
		return this.TokenMap.containsKey(block);
	}

	/*
	 * returns the vehicle id associated with the block
	 */
	public final int getValue(Block block) {
		return (this.TokenMap.get(block) == null) ? 0 : this.TokenMap.get(block);
	}
	
	public final void updateValue(Block block, int id) {
		this.TokenMap.replace(block, id);
	}
}
