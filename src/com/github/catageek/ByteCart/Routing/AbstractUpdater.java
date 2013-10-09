package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Wanderer.AbstractWanderer;

abstract class AbstractUpdater extends AbstractWanderer {

	private final RoutingTableWritable RoutingTable;

	protected AbstractUpdater(BCSign bc, int region) {
		super(bc, region);
		
		if (bc instanceof BC8010) {
			BC8010 ic = (BC8010) bc;
			RoutingTable = ic.getRoutingTable();
		}
		else {
			RoutingTable = null;
		}

	}

	/**
	 * Get the direction where to go if we are at the border of a region or the backbone
	 * 
	 * @return the direction where we must go
	 */
	public final BlockFace manageBorder() {
		if ((isAtBorder())) {
			DirectionRegistry dir;
			if ((dir = this.getRoutingTable().getDirection(this.getWandererRegion())) != null)
				return dir.getBlockFace();
			return getFrom().getBlockFace();
		}
		return null;
	}

	/**
	 * @return the routing table
	 */
	protected final RoutingTableWritable getRoutingTable() {
		return RoutingTable;
	}	
}
