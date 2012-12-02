package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public abstract class AbstractCollisionAvoiderBuilder {

	protected final TriggeredIC ic;

	protected final Block block;

	public AbstractCollisionAvoiderBuilder(TriggeredIC ic, Block block) {
		this.ic = ic;
		this.block = block;
	}

	public Block getblock() {
		return this.block;
	}

	public TriggeredIC getIc() {
		return ic;
	}

}
