package com.github.catageek.ByteCart.Routing;


import org.bukkit.block.BlockFace;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Signs.BCSign;

/**
 * @author catageek
 * 
 * This class implements a wanderer that will run through all routers
 * randomly, without going to branches.
 * 
 * Wanderers implementors may extends this class and overrides its methods
 *
 */
public class DefaultRouterWanderer extends AbstractWanderer {

	public DefaultRouterWanderer(BCSign bc, int region) {
		super(bc, region);
	}

	@Override
	public void doAction(Side To) {
		return;
	}

	@Override
	public void doAction(BlockFace To) {
	}


	@Override
	public Side giveSimpleDirection() {
		return Side.LEFT;
	}

	@Override
	public BlockFace giveRouterDirection() {
		return DefaultRouterWanderer.getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
	}
}
