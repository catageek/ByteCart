package com.github.catageek.ByteCart;

import org.bukkit.Material;
import org.bukkit.block.Block;

final public class OutputPinFactory {
	static public OutputPin getOutput(Block block) {
		
		if(block.getType().equals(Material.LEVER))
			return new ComponentLever(block);

		if(block.getType().equals(Material.STONE_BUTTON) || block.getType().equals(Material.WOOD_BUTTON))
			return new ComponentButton(block);

		return null;

	}

}
