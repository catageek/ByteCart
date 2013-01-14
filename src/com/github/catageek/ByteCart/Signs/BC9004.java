package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;


public class BC9004 extends AbstractBC9000 implements TriggeredIC {

	public BC9004(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super("BC9004", block, vehicle);
		this.netmask = 2;
		this.FriendlyName = "4-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
