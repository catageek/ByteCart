package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public class RouterCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder {

	public RouterCollisionAvoiderBuilder(TriggeredIC ic, Block block) {
		super(ic, block);
	}

	@SuppressWarnings("unchecked")
	public <T extends CollisionAvoider> T getCollisionAvoider() {
		return (T) new StraightRouter (this.ic.getCardinal().getOppositeFace(), this.block);
	}


}
