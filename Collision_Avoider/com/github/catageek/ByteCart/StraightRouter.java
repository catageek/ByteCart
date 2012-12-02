package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public class StraightRouter extends AbstractRouter implements Router {

	public StraightRouter(BlockFace from, org.bukkit.block.Block block) {
		super(from, block);

		FromTo.put(Side.BACK, Side.STRAIGHT);
		FromTo.put(Side.LEFT, Side.LEFT);
		FromTo.put(Side.STRAIGHT, Side.RIGHT);
		FromTo.put(Side.RIGHT, Side.BACK);

		secondpos = Integer.parseInt("00100101", 2);


	}

	@Override
	public void route(BlockFace from) {
		// activate main levers
		this.getOutput(0).setAmount((new DirectionRegistry(from.getOppositeFace())).getAmount());

	}


}
