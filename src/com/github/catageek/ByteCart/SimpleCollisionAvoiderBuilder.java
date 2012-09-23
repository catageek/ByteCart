package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public class SimpleCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	private final TriggeredIC ic;

	private final Block block;



	public SimpleCollisionAvoiderBuilder(TriggeredIC ic, Block block) {
		this.ic = ic;
		this.block = block;
	}



	public CollisionAvoider getCollisionAvoider() {

		return new SimpleCollisionAvoider (this.ic, this.block);
	}



	@Override
	public Block getblock() {
		return this.block;
	}

	public TriggeredIC getIc() {
		return ic;
	}

}
