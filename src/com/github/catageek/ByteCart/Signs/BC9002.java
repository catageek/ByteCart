package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;


public class BC9002 extends AbstractBC9000 implements TriggeredIC {

	public BC9002(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super("BC9002", block, vehicle);
		this.netmask = 3;
		this.FriendlyName = "2-station subnet";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

}
