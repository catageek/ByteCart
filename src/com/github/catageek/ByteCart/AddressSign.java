package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public class AddressSign extends AbstractComponent implements Address {
	
	private AddressString Address = null;;

	public AddressSign(Block block, int ligne) {
		
		super(block.getLocation());
		
		org.bukkit.block.Sign sign = null;

		if (block.getState() instanceof org.bukkit.block.Sign) {
			sign = (org.bukkit.block.Sign) block.getState();
			this.Address = new AddressString(sign.getLine(ligne));
/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: creating AddressSign line #" + ligne + " at " + block.getLocation().toString());
	*/	}
		else {
			ByteCart.log.info("ByteCart: AddressSign cannot be built");
		}
	}
	
	public VirtualRegistry getRegion() {
		return Address.getRegion();
	}

	public VirtualRegistry getTrack() {
		return Address.getTrack();
	}

	public VirtualRegistry getStation() {
		return Address.getStation();
	}

}
