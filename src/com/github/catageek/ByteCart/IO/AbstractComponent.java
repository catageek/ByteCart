package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;

/**
 * Abstract class containing common methods for all components
 */
public abstract class AbstractComponent implements Component {
	private final Block block;
	
	/**
	 * @param block the block containing the component
	 */
	protected AbstractComponent(Block block) {
		this.block = block;
	}

	/**
	 * @return the block
	 */
	@Override
	public Block getBlock() {
		return block;
	}
}
