package com.github.catageek.ByteCart;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class MathUtil {

    public static final int floor(double d) {
        int rt = (int) d;
        return rt > d ? rt - 1 : rt;
    }

    public static final boolean isSameBlock(Location from, Location to) {
        return floor(from.getX()) == floor(to.getX()) && floor(from.getZ()) == floor(to.getZ());
    }

    public static final double abs(double d) {
        return d < 0 ? -d : d;
    }
    
    public static final BlockFace clockwise(BlockFace b) {
    	switch(b) {
    	case NORTH:
    		return BlockFace.EAST;
    	case EAST:
    		return BlockFace.SOUTH;
    	case SOUTH:
    		return BlockFace.WEST;
    	case WEST:
    		return BlockFace.NORTH;
    	}
		return b;
    	
    }
 
    public static final BlockFace anticlockwise(BlockFace b) {
    	switch(b) {
    	case NORTH:
    		return BlockFace.WEST;
    	case EAST:
    		return BlockFace.NORTH;
    	case SOUTH:
    		return BlockFace.EAST;
    	case WEST:
    		return BlockFace.SOUTH;
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

}