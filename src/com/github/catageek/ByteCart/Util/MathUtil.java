package com.github.catageek.ByteCart.Util;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;


public final class MathUtil {

	public static final BlockFace clockwise(BlockFace f) {
		BlockFace b = MathUtil.straightUp(f);
		switch(b) {
		case NORTH:
			return BlockFace.EAST;
		case EAST:
			return BlockFace.SOUTH;
		case SOUTH:
			return BlockFace.WEST;
		case WEST:
			return BlockFace.NORTH;
		default:
			break;
		}
		return b;

	}

	public static final BlockFace anticlockwise(BlockFace f) {
		BlockFace b = MathUtil.straightUp(f);
		switch(b) {
		case NORTH:
			return BlockFace.WEST;
		case EAST:
			return BlockFace.NORTH;
		case SOUTH:
			return BlockFace.EAST;
		case WEST:
			return BlockFace.SOUTH;
		default:
			break;
		}
		return b;

	}

	public static final BlockFace straightUp(BlockFace b) {
		switch(b) {
		case NORTH:
		case NORTH_NORTH_WEST:
		case NORTH_NORTH_EAST:
			return BlockFace.NORTH;
		case EAST:
		case EAST_NORTH_EAST:
		case EAST_SOUTH_EAST:
			return BlockFace.EAST;
		case SOUTH:
		case SOUTH_SOUTH_WEST:
		case SOUTH_SOUTH_EAST:
			return BlockFace.SOUTH;
		case WEST:
		case WEST_NORTH_WEST:
		case WEST_SOUTH_WEST:
			return BlockFace.WEST;
		default:
			ByteCart.log.severe("ByteCart: Tilted sign found. Please straight it up in the axis of the track");
			break;
		}
		return b;
	}

	public static final void forceUpdate(Block b) {
		byte oldData = b.getData();
		byte notData;
		if (oldData>1) notData = (byte)(oldData-1);
		else if (oldData<15) notData = (byte)(oldData+1);
		else notData = 0;
		b.setData(notData, true);
		b.setData(oldData, true);
	}

	public static final void loadChunkAround(World world, int x, int z, int radius) {
		int j, i = x-radius, k = x+radius, l = z+radius;


		//		long start = System.nanoTime();

		for (; i<=k; ++i) {
			for (j=z-radius;  j<=l ; ++j) {
				world.loadChunk(i, j, false);
			}
		}

	}
}