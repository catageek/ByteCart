package com.github.catageek.ByteCart.IO;

import org.bukkit.Material;
import org.bukkit.block.Block;


final public class InputFactory {
	
	@SuppressWarnings("unchecked")
	static public <T> T getInput(Block block) {
		
		if(block.getType().equals(Material.REDSTONE_WIRE)) {
			return (T) new ComponentWire(block);
		}
		if(block.getType().equals(Material.LEVER)) {
			return (T) new ComponentLever(block);
		}
		return null;
		
	}

}
