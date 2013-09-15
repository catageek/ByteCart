package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryOutput;
import com.github.catageek.ByteCart.Signs.Triggable;
import com.github.catageek.ByteCart.Storage.ExpirableMap;

public class SimpleCollisionAvoider extends AbstractCollisionAvoider implements CollisionAvoider {

	private static final ExpirableMap<Location, Boolean> recentlyUsedMap = new ExpirableMap<Location, Boolean>(20, false, "recentlyUsed9000");
	private static final ExpirableMap<Location, Boolean> hasTrainMap = new ExpirableMap<Location, Boolean>(14, false, "hastrain");

	private RegistryOutput Lever1 = null, Lever2 = null, Active = null;
	private final Location loc1;

	private Side state;

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

		public Side opposite() {
			if (this.equals(LEFT))
				return RIGHT;
			return LEFT;
		}
	}

	public SimpleCollisionAvoider(Triggable ic, org.bukkit.Location loc) {
		super(loc);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: new SimpleCollisionAvoider() at " + loc);

		Lever1 = ic.getOutput(0);
		Active = Lever1;
		loc1 = ic.getLocation();
		state = (Lever1.getAmount() == 0 ? Side.LEFT : Side.RIGHT);
	}

	public Side WishToGo(Side s, boolean isTrain) {

		Side trueside = getTrueSide(s);

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : WishToGo to side " + trueside + " and isTrain is " + isTrain);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : state is " + state);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : recentlyUsed is " + this.getRecentlyUsed() + " and hasTrain is " + this.getHasTrain());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Lever1 is " + Lever1.getAmount());

		if ( trueside != state 
				&& (Lever2 == null 
				|| ( !this.getRecentlyUsed()) && !this.getHasTrain())) {
			Set(trueside);
		}
		this.setRecentlyUsed(true);
		return state;

	}

	/**
	 * Get the fixed side if it is the second lever
	 * 
	 * @param s the original side
	 * @return the fixed side
	 */
	private Side getTrueSide(Side s) {
		Side trueside = (Active == Lever2 ? s.opposite() : s);
		return trueside;
	}


	@Override
	public void Add(Triggable t) {
		if (t.getLocation().equals(loc1)) {
			Active = Lever1;
			return;
		}
		if (Lever2 != null) {
			Active = Lever2;
			return;
		}
		Lever2 = t.getOutput(0);
		Active = Lever2;
		Lever2.setAmount(getTrueSide(state).Value());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Add and setting lever2 to " + Lever2.getAmount());
	}

	private void Set(Side s) {
		this.Lever1.setAmount(s.Value());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Setting lever1 to " + Lever1.getAmount());
		if (this.Lever2 != null) {
			this.Lever2.setAmount(s.opposite().Value());
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: Setting lever2 to " + Lever2.getAmount());
		}
		state = s;
	}

	@Override
	public int getSecondpos() {
		throw new UnsupportedOperationException();
	}

	protected ExpirableMap<Location, Boolean> getRecentlyUsedMap() {
		return recentlyUsedMap;
	}

	protected ExpirableMap<Location, Boolean> getHasTrainMap() {
		return hasTrainMap;
	}



}
