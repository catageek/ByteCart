package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.Storage.ExpirableMap;

public final class CollisionAvoiderManager {

	private final ExpirableMap<Location, CollisionAvoider> manager = new ExpirableMap<Location, CollisionAvoider>(80, false, "CollisionAvoider");

	/**
	 * @return the manager
	 */
	private ExpirableMap<Location, CollisionAvoider> getManager() {
		return manager;
	}

	@SuppressWarnings("unchecked")
	public final synchronized <T extends CollisionAvoider> T getCollisionAvoider(CollisionAvoiderBuilder builder) {
		Location loc = builder.getLocation();
		T cm;
		cm = (T) this.getManager().get(loc);
		if(cm != null) {
			cm.Add(builder.getIc());
			this.getManager().reset(loc);
		}
		else
		{
			cm = builder.<T>getCollisionAvoider();
			this.getManager().put(loc, cm);
		}
		return cm;
	}
	
	public final void setCollisionAvoider(Location loc, CollisionAvoider ca) {
		this.getManager().put(loc, ca);
	}



}
