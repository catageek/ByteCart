package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public interface CollisionAvoiderBuilder {
	public CollisionAvoider getCollisionAvoider();
	public Block getblock();
	public TriggeredIC getIc();

}
