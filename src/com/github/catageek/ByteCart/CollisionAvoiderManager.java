package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public final class CollisionAvoiderManager {

	private final BlockMap<CollisionAvoider> manager = new EphemeralBlockMap<CollisionAvoider>(80);

	/**
	 * @return the manager
	 */
	private BlockMap<CollisionAvoider> getManager() {
		return manager;
	}

	@SuppressWarnings("unchecked")
	public final synchronized <T extends CollisionAvoider> T getCollisionAvoider(CollisionAvoiderBuilder builder) {
		Block block = builder.getblock();
		T cm;

		if(	(cm = (T) this.getManager().getValue(block)) != null)
			cm.Add(builder.getIc());
		else
		{
			cm = builder.<T>getCollisionAvoider();
			this.getManager().createEntry(block, cm);
		}
		return cm;
	}
	
	public final void setCollisionAvoider(Block block, CollisionAvoider ca) {
		this.getManager().updateValue(block, ca);
	}



}
