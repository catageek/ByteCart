package com.github.catageek.ByteCart;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class MathUtil {

    public static final int floor(double d) {
        int rt = (int) d;
        return rt > d ? rt - 1 : rt;
    }

    public static final int offsetfloor(double d) {
    	double e = d - 0.5;
        int rt = (int) e;
        return rt > e ? rt - 1 : rt;
    }

    public static final boolean isSameBlock(Location from, Location to) {

        return floor(from.getX()) == floor(to.getX()) && floor(from.getZ()) == floor(to.getZ());
    }
    
    public static final boolean isMiddleBlock(Location from, Location to) {
		if(ByteCart.debug && offsetfloor(from.getZ()) <= 168 && offsetfloor(from.getZ()) >= 166) {
			ByteCart.log.info("ByteCart : isMiddleblock " + from.getZ() + " " + to.getZ());
			ByteCart.log.info("ByteCart : isMiddleblock offset" + offsetfloor(from.getZ()) + " " + offsetfloor(to.getZ()));
		}
    	return offsetfloor(from.getX()) == offsetfloor(to.getX()) && offsetfloor(from.getZ()) == offsetfloor(to.getZ());
    	
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