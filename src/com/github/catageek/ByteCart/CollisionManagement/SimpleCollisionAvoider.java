package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.Location;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;
import com.github.catageek.ByteCart.HAL.RegistryOutput;
import com.github.catageek.ByteCart.Storage.ExpirableMap;

public class SimpleCollisionAvoider extends AbstractCollisionAvoider implements CollisionAvoider {

	private static final ExpirableMap<Location, Boolean> recentlyUsedMap = new ExpirableMap<Location, Boolean>(20, false, "recentlyUsed9000");
	private static final ExpirableMap<Location, Boolean> hasTrainMap = new ExpirableMap<Location, Boolean>(14, false, "hastrain");
	
	private Side state = Side.RIGHT;
	private RegistryOutput Lever1 = null, Lever2 = null;

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

	public SimpleCollisionAvoider(TriggeredIC ic, org.bukkit.Location loc) {
		super(loc);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: new SimpleCollisionAvoider() at " + loc);
		Lever1 = ic.getOutput(0);
		Initialize();
	}

	public void WishToGo(Side s, boolean isTrain) {
/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : WishToGo to side " + s + " and isTrain is " + isTrain);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : state is " + state + " and Lever2 is " + Lever2);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : recentlyUsed is " + recentlyUsed + " and hasTrain is " + hasTrain);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Lever1 is " + Lever1);
*/
		if ( s != state && ( Lever2 == null || ( !this.getRecentlyUsed()) && !this.getHasTrain())) {
			Permute();
		}
		this.setRecentlyUsed(true);

	}


	@Override
	public void Add(TriggeredIC t) {
		Lever2 = t.getOutput(0);
		Lever2.setAmount(state.Value());
	}

	private void Set(Side s) {
		this.Lever1.setAmount(s.Value());
		if (this.Lever2 != null)
			this.Lever2.setAmount(s.Value());
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

	@Override
	public int getSecondpos() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected ExpirableMap<Location, Boolean> getRecentlyUsedMap() {
		return recentlyUsedMap;
	}

	protected ExpirableMap<Location, Boolean> getHasTrainMap() {
		return hasTrainMap;
	}



}
