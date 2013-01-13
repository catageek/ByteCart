package com.github.catageek.ByteCart.Signs;


public class BC9008 extends AbstractBC9000 implements TriggeredSign {

	public BC9008(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle, "BC9008");
		this.netmask = 1;
		this.Name = "BC9008";
		this.FriendlyName = "8-station subnet";
	}

}
