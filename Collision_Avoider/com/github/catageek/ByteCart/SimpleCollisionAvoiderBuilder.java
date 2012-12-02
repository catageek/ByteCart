package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public class SimpleCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public SimpleCollisionAvoiderBuilder(TriggeredIC ic, Block block) {
		super(ic, block);
	}



	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {

		return (T) new SimpleCollisionAvoider (this.ic, this.block);
	}




}
