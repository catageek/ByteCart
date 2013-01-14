package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.EventManagement.TriggeredIC;


public class BC9016 extends AbstractBC9000 implements TriggeredIC {

	public BC9016(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super("BC9016", block, vehicle);
		this.netmask = 0;
		this.FriendlyName = "16-station subnet";
	}

}
