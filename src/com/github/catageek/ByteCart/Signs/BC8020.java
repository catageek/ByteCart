package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;
import com.github.catageek.ByteCart.Routing.AbstractUpdater;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Storage.UpdaterManager;



public class BC8020 extends BC8010 implements TriggeredIC {


	public BC8020(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC8020";
		this.FriendlyName = "L2 router";
		this.Triggertax = ByteCart.myPlugin.getConfig().getInt("usetax." + this.Name);
		this.Permission = this.Permission + this.Name;

	}

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
		
		// if TTL reached end of life, then we lookup region 0
		try {
			return RoutingTable.getDirection(0).getBlockFace();
		} catch (NullPointerException e) {
		}

		return AbstractUpdater.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());

	}

	@Override
	protected Updater getUpdater(RoutingTable routingtable, org.bukkit.entity.Vehicle vehicle,
			Address ringAddress, BlockFace from) {
		return this.getNewUpdater(routingtable, vehicle, ringAddress, from, false, Level.BACKBONE);
	}

	@Override
	protected Updater.Level getLevel() {
		return Updater.Level.BACKBONE;
	}

}
