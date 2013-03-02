package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.DefaultRouterWanderer;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Storage.UpdaterManager;



public final class BC8020 extends BC8010 implements BCRouter, Triggable, HasRoutingTable {


	BC8020(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.IsTrackNumberProvider = true;
	}

	/**
	 * Return true if it's a local updater or a normal cart to route normally
	 */
	@Override
	protected boolean selectUpdater(int id) {
		UpdaterManager um = ByteCart.myPlugin.getUm();
		return (! um.isUpdater(id))
				|| um.isUpdater(id, Level.LOCAL)
				|| um.isUpdater(id, Level.RESET_LOCAL);
	}

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

	@Override
	public Updater.Level getLevel() {
		return Updater.Level.BACKBONE;
	}

	@Override
	public String getName() {
		return "BC8020";
	}

	@Override
	public String getFriendlyName() {
		return "L2 Router";
	}
}
