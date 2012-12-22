package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.Storage.ExpirableMap;

public abstract class AbstractCollisionAvoider extends AbstractIC {

	abstract protected ExpirableMap<Location, Boolean> getRecentlyUsedMap();
	abstract protected ExpirableMap<Location, Boolean> getHasTrainMap();
	
	public enum Side {
		BACK (0),
		LEFT (2),
		STRAIGHT (4),
		RIGHT(6);

		private int Value;

		Side(int b) {
			Value = b;
		}

		public int Value() {
			return Value;
		}
	}

	public AbstractCollisionAvoider(org.bukkit.Location loc) {
		super(loc.getBlock());
	}

	protected boolean getHasTrain() {
		 return this.getHasTrainMap().contains(getLocation());
	}

	protected boolean getRecentlyUsed() {
		 return this.getRecentlyUsedMap().contains(getLocation());
	}

	/**
	 * @param hasTrain the hasTrain to set
	 */
	private void setHasTrain(boolean hasTrain) {
		 this.getHasTrainMap().put(getLocation(), hasTrain);
	}

	/**
	 * @param recentlyUsed the recentlyUsed to set
	 */
	protected void setRecentlyUsed(boolean recentlyUsed) {
		this.getRecentlyUsedMap().put(getLocation(), recentlyUsed);
	}

	public void Book(boolean isTrain) {
		setRecentlyUsed(true);
		setHasTrain(this.getHasTrain() | isTrain);
	}
}
