package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public final class CollisionAvoiderManager {

	private final BlockMap<CollisionAvoider> manager = new BlockMap<CollisionAvoider>();

	/**
	 * @return the manager
	 */
	private BlockMap<CollisionAvoider> getManager() {
		return manager;
	}

	public final CollisionAvoider getCollisionAvoider(CollisionAvoiderBuilder builder) {
		Block block = builder.getblock();
		CollisionAvoider cm;
		if (this.getManager().hasEntry(block)) {
			cm = this.getManager().getValue(block);
			cm.Add(builder.getIc());
		} else
		{
			cm = builder.getCollisionAvoider();
			this.getManager().createEntry(block, cm);
		}
		return cm;
	}



}
