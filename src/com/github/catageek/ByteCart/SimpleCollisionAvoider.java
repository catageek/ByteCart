package com.github.catageek.ByteCart;

public class SimpleCollisionAvoider extends AbstractCollisionAvoider implements CollisionAvoider {

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

	public SimpleCollisionAvoider(TriggeredIC ic, org.bukkit.block.Block block) {
		super(block);
		Lever1 = ic.getOutput(0);
		Initialize();
	}

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
		super.Book(isTrain, 6);

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



}
