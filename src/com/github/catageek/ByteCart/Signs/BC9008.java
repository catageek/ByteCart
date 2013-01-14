package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.EventManagement.TriggeredIC;


public class BC9008 extends AbstractBC9000 implements TriggeredIC {

	public BC9008(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle, "BC9008");
		this.netmask = 1;
		this.Name = "BC9008";
		this.FriendlyName = "8-station subnet";
	}

}
