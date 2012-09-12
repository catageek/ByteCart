package com.github.catageek.ByteCart;

import org.bukkit.block.Block;


public class BC8020 extends BC8010 implements TriggeredIC {


	public BC8020(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC8020";
		this.FriendlyName = "L2 router";
		this.Triggertax = ByteCart.myPlugin.getConfig().getInt("usetax." + this.Name);
		this.Permission = this.Permission + this.Name;

	}

	@Override
	protected DirectionRegistry SelectRoute(AddressRouted IPaddress, Address sign, RoutingTable RoutingTable) {
		// if TTL reached end of life, then we lookup region 0
		if (IPaddress.getTTL().getAmount() == 1) {
			return RoutingTable.getDirection(0);
		} else
		{	// lookup destination region
			return RoutingTable.getDirection(IPaddress.getRegion().getAmount());

		}

	}


}
