package com.github.catageek.ByteCart;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.block.BlockFace;

public final class LeftRouter extends AbstractRouter implements
		Router {

	public LeftRouter(BlockFace from, org.bukkit.block.Block block) {
		super(from, block);
		FromTo.put(Side.BACK, Side.LEFT);
		
		Set<Side> left = EnumSet.of(Side.LEFT, Side.STRAIGHT, Side.RIGHT);
		Possibility.put(Side.LEFT, left);
		
		Set<Side> straight = EnumSet.of(Side.STRAIGHT, Side.LEFT, Side.BACK);
		Possibility.put(Side.STRAIGHT, straight);
		
		Set<Side> right = EnumSet.of(Side.LEFT, Side.BACK);
		Possibility.put(Side.RIGHT, right);

		secondpos = Integer.parseInt("01000000", 2);
		posmask = Integer.parseInt("11100000", 2);

	}

}
