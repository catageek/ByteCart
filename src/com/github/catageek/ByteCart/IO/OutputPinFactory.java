package com.github.catageek.ByteCart.IO;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;


/**
 * Factory to get an instance of an output component
 */
final public class OutputPinFactory {
	/**
	 * Get an instance of the output component
	 *
	 * @param block block containing the component
	 * @return the instance
	 */
	static public OutputPin getOutput(Block block) {
		
		if(block.getBlockData() instanceof Switch) {
			final Switch myswitch = (Switch) block.getBlockData();
			if(myswitch.getMaterial().equals(Material.LEVER)) {
				return new ComponentLever(block);
			}
			else {
				return new ComponentButton(block);
			}
		}

		return null;

	}

}
