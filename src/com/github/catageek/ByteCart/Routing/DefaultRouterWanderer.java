package com.github.catageek.ByteCart.Routing;


import java.util.Random;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

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
	
	/**
	 * Get a random route that is not from where we are coming
	 * 
	 * @param routingtable the routing table where to pick up a route
	 * @param from the direction from where we are coming
	 * @return the direction
	 */
	public static final BlockFace getRandomBlockFace(RoutingTable routingtable, BlockFace from) {

		// selecting a random destination avoiding ring 0 or where we come from
		DirectionRegistry direction = new DirectionRegistry(1 << (new Random()).nextInt(4));

		while (direction.getBlockFace() == from || routingtable.isDirectlyConnected(0, direction)) {
			direction.setAmount(1 << (new Random()).nextInt(4));
		}

		return direction.getBlockFace();
	}
}
