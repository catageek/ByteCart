package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Powerable;


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
		
		if(block.getBlockData() instanceof AnaloguePowerable) {
			return (T) new ComponentWire(block);
		}
		if(block.getBlockData() instanceof Powerable) {
			return (T) new ComponentLever(block);
		}

		return null;
		
	}

}
