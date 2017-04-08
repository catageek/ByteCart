package com.github.catageek.ByteCart.CollisionManagement;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.block.BlockFace;

/**
 * A router where the cart goes back
 */
public final class BackRouter extends AbstractRouter implements
		Router {

	public BackRouter(BlockFace from, org.bukkit.Location loc, boolean b) {
		super(from, loc, b);
		FromTo.put(Side.BACK, Side.BACK);

		Set<Side> left = EnumSet.of(Side.BACK, Side.LEFT);
		Possibility.put(Side.LEFT, left);
		
		Set<Side> straight = EnumSet.of(Side.RIGHT, Side.LEFT, Side.BACK);
		Possibility.put(Side.STRAIGHT, straight);
		
		Set<Side> right = EnumSet.of(Side.STRAIGHT, Side.BACK, Side.RIGHT);
		Possibility.put(Side.RIGHT, right);

		setSecondpos(Integer.parseInt("10000000", 2));
		setPosmask(Integer.parseInt("11000001", 2));

	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.CollisionManagement.AbstractRouter#getTo()
	 */
	@Override
	public final BlockFace getTo() {
		return this.getFrom();
	}

}
