package com.github.catageek.ByteCart;

import org.bukkit.Location;

public final class CollisionAvoiderManager {

	private final BlockMap<Location, CollisionAvoider> manager = new EphemeralBlockMap<Location, CollisionAvoider>(80);

	/**
	 * @return the manager
	 */
	private BlockMap<Location, CollisionAvoider> getManager() {
		return manager;
	}

	@SuppressWarnings("unchecked")
	public final synchronized <T extends CollisionAvoider> T getCollisionAvoider(CollisionAvoiderBuilder builder) {
		Location loc = builder.getLocation();
		T cm;

		if(	(cm = (T) this.getManager().getValue(loc)) != null)
			cm.Add(builder.getIc());
		else
		{
			cm = builder.<T>getCollisionAvoider();
			this.getManager().createEntry(loc, cm);
		}
		return cm;
	}
	
	public final void setCollisionAvoider(Location loc, CollisionAvoider ca) {
		this.getManager().updateValue(loc, ca);
	}



}
