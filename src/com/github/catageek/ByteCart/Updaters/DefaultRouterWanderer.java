package com.github.catageek.ByteCart.Updaters;


import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.Signs.BCSign;

/**
 * 
 * This class implements a wanderer that will run through all routers
 * randomly, without going to branches.
 * 
 * Wanderers implementors may extends this class and overrides its methods
 *
 */
public class DefaultRouterWanderer extends AbstractUpdater {

	public DefaultRouterWanderer(BCSign bc, int region) {
		super(bc, region);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractWanderer#doAction(com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side)
	 */
	@Override
	public void doAction(Side To) {
		return;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractWanderer#doAction(org.bukkit.block.BlockFace)
	 */
	@Override
	public void doAction(BlockFace To) {
	}


	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractWanderer#giveSimpleDirection()
	 */
	@Override
	public Side giveSimpleDirection() {
		return Side.LEVER_OFF;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractWanderer#giveRouterDirection()
	 */
	@Override
	public BlockFace giveRouterDirection() {
		return getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
	}
	
}
