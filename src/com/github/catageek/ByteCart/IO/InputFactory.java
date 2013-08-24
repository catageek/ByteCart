package com.github.catageek.ByteCart.IO;

import org.bukkit.Material;
import org.bukkit.block.Block;


/**
 * A factory for input pins
 */
final public class InputFactory {
	
	/**
	 * Get an instance of the input component
	 *
	 * @param block block containing the component
	 * @return the instance
	 */
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
