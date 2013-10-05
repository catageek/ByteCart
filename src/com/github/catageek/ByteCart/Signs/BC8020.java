package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.Routing.DefaultRouterWanderer;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.Routing.Updater;
import com.github.catageek.ByteCartAPI.Routing.Updater.Scope;
import com.github.catageek.ByteCartAPI.Signs.BCRouter;



/**
 * An IC at the entry of a L2 router
 */
final class BC8020 extends BC8010 implements BCRouter, Triggable, HasRoutingTable {


	BC8020(Block block, org.bukkit.entity.Vehicle vehicle) throws ClassNotFoundException, IOException {
		super(block, vehicle);
		this.IsTrackNumberProvider = true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#selectUpdater()
	 */
	@Override
	protected boolean selectUpdater() {
		return (! WandererContentFactory.isWanderer(this.getInventory()))
				|| WandererContentFactory.isWanderer(this.getInventory(), Scope.LOCAL);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#SelectRoute(com.github.catageek.ByteCart.AddressLayer.AddressRouted, com.github.catageek.ByteCart.AddressLayer.Address, com.github.catageek.ByteCart.Routing.RoutingTable)
	 */
	@Override
	protected BlockFace SelectRoute(AddressRouted IPaddress, Address sign, RoutingTable RoutingTable) {

		try {
			if (IPaddress.getTTL() != 0) {
				// lookup destination region
				return RoutingTable.getDirection(IPaddress.getRegion().getAmount()).getBlockFace();
			}
		} catch (NullPointerException e) {
		}

		// if TTL reached end of life and is not returnable, then we lookup region 0
		try {
			return RoutingTable.getDirection(0).getBlockFace();
		} catch (NullPointerException e) {
		}

		// If everything has failed, then we randomize output direction
		return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());

	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#getLevel()
	 */
	@Override
	public Updater.Level getLevel() {
		return Updater.Level.BACKBONE;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#getName()
	 */
	@Override
	public String getName() {
		return "BC8020";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "L2 Router";
	}
}
