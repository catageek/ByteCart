package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.HAL.RegistryInput;

final class BC7012 extends BC7013 implements Triggable {

	BC7012(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	@Override
	protected String format(RegistryInput wire, AddressRouted InvAddress) {
		return ""+wire.getAmount()+"."
				+InvAddress.getTrack().getAmount()+"."
				+InvAddress.getStation().getAmount();
	}

	@Override
	public final String getName() {
		return "BC7012";
	}

	@Override
	public final String getFriendlyName() {
		return "setRegion";
	}
	
	@Override
	protected boolean forceTicketReuse() {
		return true;
	}

}
