package com.github.catageek.ByteCart.IO;

import org.bukkit.Material;
import org.bukkit.block.Block;


final public class InputPinFactory {
	static public InputPin getInput(Block block) {
		
		if(block.getType().equals(Material.REDSTONE_WIRE)) {
/*			if(ByteCart.debug)
				ByteCart.log.info("Wire as input at (" + block.getLocation().toString() + ")");
*/			return new ComponentWire(block);
		}
		return null;
		
	}

}
