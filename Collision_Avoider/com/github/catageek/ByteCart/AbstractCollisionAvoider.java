package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public abstract class AbstractCollisionAvoider extends AbstractIC {

	public boolean hasTrain = false, recentlyUsed = false;

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

	/**
	 * @param hasTrain the hasTrain to set
	 */
	private void setHasTrain(boolean hasTrain) {
		this.hasTrain = hasTrain;
	}

	/**
	 * @param recentlyUsed the recentlyUsed to set
	 */
	private void setRecentlyUsed(boolean recentlyUsed) {
		this.recentlyUsed = recentlyUsed;
	}

	final protected class SetRecentFalse implements Runnable {

		final private AbstractCollisionAvoider ic;

		SetRecentFalse(AbstractCollisionAvoider ic) {
			this.ic = ic;
		}

		@Override
		public void run() {
			ic.setRecentlyUsed(false);
		}

	}

	final protected class SetTrainFalse implements Runnable {

		final private AbstractCollisionAvoider ic;

		SetTrainFalse(AbstractCollisionAvoider ic) {
			this.ic = ic;
		}

		@Override
		public void run() {
			ic.setHasTrain(false);
		}

	}

	public void Ping() {
		ByteCart.myPlugin.getDelayedThreadManager().renewAsync(getLocation(), 14, new SetTrainFalse(this));
	}

	public void Book(boolean isTrain, int duration) {
		setRecentlyUsed(true);
		ByteCart.myPlugin.getDelayedThreadManager().renewAsync(getBlock().getRelative(BlockFace.DOWN).getLocation(), ByteCart.myPlugin.Lockduration + duration, new SetRecentFalse(this));

		this.hasTrain |= isTrain;
		if (this.hasTrain)
			ByteCart.myPlugin.getDelayedThreadManager().renewAsync(getLocation(), 14, new SetTrainFalse(this));
	}

	public void Book(boolean isTrain) {
		this.Book(isTrain, 0);
	}
}
