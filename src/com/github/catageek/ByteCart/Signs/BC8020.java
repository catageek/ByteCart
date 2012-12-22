package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.RoutingTable;



public class BC8020 extends BC8010 implements TriggeredIC {


	public BC8020(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC8020";
		this.FriendlyName = "L2 router";
		this.Triggertax = ByteCart.myPlugin.getConfig().getInt("usetax." + this.Name);
		this.Permission = this.Permission + this.Name;

	}

	@Override
	protected BlockFace SelectRoute(AddressRouted IPaddress, Address sign, RoutingTable RoutingTable) {
		// if TTL reached end of life, then we lookup region 0
		if (IPaddress.getTTL() == 0) {
			return RoutingTable.getDirection(0).getBlockFace();
		} else
		{	// lookup destination region
			return RoutingTable.getDirection(IPaddress.getRegion().getAmount()).getBlockFace();

		}

	}


}
