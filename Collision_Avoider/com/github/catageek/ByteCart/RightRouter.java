package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public final class RightRouter extends AbstractRouter implements
		Router {

	public RightRouter(BlockFace from, org.bukkit.block.Block block) {
		super(from, block);
		
		FromTo.put(Side.BACK, Side.RIGHT);
		FromTo.put(Side.LEFT, Side.LEFT);
		FromTo.put(Side.STRAIGHT, Side.STRAIGHT);
		FromTo.put(Side.RIGHT, Side.BACK);

		secondpos = Integer.parseInt("00101001", 2);


	}

	@Override
	public void route(BlockFace from) {
		// activate main levers
		this.getOutput(0).setAmount((new DirectionRegistry(MathUtil.anticlockwise(from))).getAmount());

	}

}
