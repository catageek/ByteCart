package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public class SimpleCollisionAvoider extends AbstractIC implements CollisionAvoider {

	public enum Side {
		RIGHT (3),
		LEFT (0);
		
		private int Value;
		
		Side(int b) {
			Value = b;
		}
		
		public int Value() {
			return Value;
		}
	}
	
	private boolean hasTrain = false, recentlyUsed = false;
	private Side state = Side.RIGHT;
	private RegistryOutput Lever1 = null, Lever2 = null;
	
	public SimpleCollisionAvoider(TriggeredIC ic, org.bukkit.block.Block block) {
		super(block);
		Lever1 = ic.getOutput(0);
		Initialize();
	}

	@Override
	public void WishToGo(Side s, boolean isTrain) {

/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : WishToGo to side " + s + " and isTrain is " + isTrain);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : state is " + state + " and Lever2 is " + Lever2);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : recentlyUsed is " + recentlyUsed + " and hasTrain is " + hasTrain);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Lever1 is " + Lever1);
*/
		if ( s != state && ( Lever2 == null || ( !recentlyUsed && !hasTrain))) {
			Permute();
		}
		setRecentlyUsed(true);
		ByteCart.myPlugin.getDelayedThreadManager().renew(getBlock().getRelative(BlockFace.DOWN), 12, new SetRecentFalse(this));
		
		this.hasTrain |= isTrain;
		if (this.hasTrain)
			ByteCart.myPlugin.getDelayedThreadManager().renew(getBlock(), 40, new SetTrainFalse(this));

		
	}

	@Override
	public void Ping() {
		ByteCart.myPlugin.getDelayedThreadManager().renew(getBlock(), 40, new SetTrainFalse(this));
	}

	@Override
	public void Add(TriggeredIC t) {
		Lever2 = t.getOutput(0);
		Lever2.setAmount(state.Value);
	}

	private void Set(Side s) {
		this.Lever1.setAmount(s.Value);
		if (this.Lever2 != null)
			this.Lever2.setAmount(s.Value);
		this.state = s;
	}

	private void Permute() {
		if (state == Side.LEFT) {
			Set(Side.RIGHT);
			state = Side.RIGHT;
		}
		else {
			Set(Side.LEFT);
			state = Side.LEFT;
		}
		
	}

	private void Initialize() {
		Set(Side.LEFT);
	}
	
	/**
	 * @param hasTrain the hasTrain to set
	 */
	public void setHasTrain(boolean hasTrain) {
		this.hasTrain = hasTrain;
	}

	/**
	 * @param recentlyUsed the recentlyUsed to set
	 */
	public void setRecentlyUsed(boolean recentlyUsed) {
		this.recentlyUsed = recentlyUsed;
	}

	final private class SetRecentFalse implements Runnable {

		final private CollisionAvoider ic;

		SetRecentFalse(CollisionAvoider ic) {
			this.ic = ic;
		}

		@Override
		public void run() {
			ic.setRecentlyUsed(false);
		}

	}

	final private class SetTrainFalse implements Runnable {

		final private CollisionAvoider ic;

		SetTrainFalse(CollisionAvoider ic) {
			this.ic = ic;
		}

		@Override
		public void run() {
			ic.setHasTrain(false);
		}

	}

	
}
