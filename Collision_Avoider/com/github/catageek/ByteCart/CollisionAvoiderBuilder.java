package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public interface CollisionAvoiderBuilder {
	public <T extends CollisionAvoider> T getCollisionAvoider();
	public Block getblock();
	public TriggeredIC getIc();

}
