package com.github.catageek.ByteCart.Signs;


public class BC9004 extends AbstractBC9000 implements TriggeredSign {

	public BC9004(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle, "BC9004");
		this.netmask = 2;
		this.Name = "BC9004";
		this.FriendlyName = "4-station subnet";
	}

}
