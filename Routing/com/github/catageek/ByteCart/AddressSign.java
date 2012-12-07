package com.github.catageek.ByteCart;

import org.bukkit.block.Block;


public class AddressSign extends AbstractComponent implements Address {
	
	private AddressString Address;

	public AddressSign(Block block, int ligne) {
		
		super(block.getLocation());
		
		org.bukkit.block.Sign sign;

		if (block.getState() instanceof org.bukkit.block.Sign) {
			sign = (org.bukkit.block.Sign) block.getState();
			this.Address = new AddressString(sign.getLine(ligne));
/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: creating AddressSign line #" + ligne + " at " + block.getLocation().toString());
	*/	}
		else {
			ByteCart.log.info("ByteCart: AddressSign cannot be built");
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public Registry getRegion() {
		return Address.getRegion();
	}

	@Override
	public Registry getTrack() {
		return Address.getTrack();
	}

	@Override
	public Registry getStation() {
		return Address.getStation();
	}


	@Override
	public boolean setAddress(String s) {
		this.Address.setAddress(s);
		return true;
	}

	@Override
	public boolean isTrain() {
		return this.Address.isTrain();
	}

	@Override
	public boolean copy(com.github.catageek.ByteCart.Address a) {
		// TODO Auto-generated method stub
		return false;
	}



}
