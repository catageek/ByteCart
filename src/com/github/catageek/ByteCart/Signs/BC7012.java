package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.Routing.AddressRouted;

public class BC7012 extends BC7013 implements TriggeredSign {

	public BC7012(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC7012";
		this.FriendlyName = "setRegion";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;
	}

	@Override
	protected String format(RegistryInput wire, AddressRouted InvAddress) {
		return ""+wire.getAmount()+"."
				+InvAddress.getTrack().getAmount()+"."
				+InvAddress.getStation().getAmount();
	}
}
