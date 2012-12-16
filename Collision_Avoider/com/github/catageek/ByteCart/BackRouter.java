package com.github.catageek.ByteCart;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.block.BlockFace;

public final class BackRouter extends AbstractRouter implements
		Router {

	public BackRouter(BlockFace from, org.bukkit.Location loc) {
		super(from, loc);
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

	@Override
	public final BlockFace getTo() {
		return this.getFrom();
	}

}
