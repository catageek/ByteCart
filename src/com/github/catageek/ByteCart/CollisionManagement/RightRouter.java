package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;

/**
 * A router where the cart turns right
 */
public final class RightRouter extends AbstractRouter implements
		Router {

	public RightRouter(BlockFace from, org.bukkit.Location loc) {
		super(from, loc);
		
		FromTo.put(Side.BACK, Side.RIGHT);
		FromTo.put(Side.LEFT, Side.LEFT);
		FromTo.put(Side.STRAIGHT, Side.STRAIGHT);
		FromTo.put(Side.RIGHT, Side.BACK);

		setSecondpos(Integer.parseInt("00101001", 2));


	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.CollisionManagement.AbstractRouter#route(org.bukkit.block.BlockFace)
	 */
	@Override
	public void route(BlockFace from) {
		// activate main levers
		this.getOutput(0).setAmount((new DirectionRegistry(MathUtil.anticlockwise(from))).getAmount());

	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.CollisionManagement.AbstractRouter#getTo()
	 */
	@Override
	public BlockFace getTo() {
		return MathUtil.anticlockwise(this.getFrom());
	}

}
