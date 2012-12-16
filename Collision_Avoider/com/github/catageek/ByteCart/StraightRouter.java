package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public class StraightRouter extends AbstractRouter implements Router {

	public StraightRouter(BlockFace from, org.bukkit.Location loc) {
		super(from, loc);

		FromTo.put(Side.BACK, Side.STRAIGHT);
		FromTo.put(Side.LEFT, Side.LEFT);
		FromTo.put(Side.STRAIGHT, Side.RIGHT);
		FromTo.put(Side.RIGHT, Side.BACK);

		setSecondpos(Integer.parseInt("00100101", 2));


	}

	@Override
	public void route(BlockFace from) {
		// activate main levers
		this.getOutput(0).setAmount((new DirectionRegistry(from.getOppositeFace())).getAmount());

	}

	@Override
	public final BlockFace getTo() {
		return this.getFrom().getOppositeFace();
	}
}
