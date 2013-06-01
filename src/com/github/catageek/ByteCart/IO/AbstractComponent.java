package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;

public abstract class AbstractComponent implements Component {
	private final Block block;
	
	protected AbstractComponent(Block block) {
		this.block = block;
	}

	/**
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}
}
