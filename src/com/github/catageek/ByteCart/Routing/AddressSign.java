package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.Block;

import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.IO.AbstractComponent;
import com.github.catageek.ByteCart.IO.ComponentSign;


public final class AddressSign extends AbstractComponent implements Address {
	
	private final AddressString Address;

	public AddressSign(Block block, int ligne) {
		
		super(block.getLocation());
		
		this.Address = new AddressString((new ComponentSign(block)).getLine(ligne));
		
/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: creating AddressSign line #" + ligne + " at " + block.getLocation().toString());
	*/	
	}
	
	@Override
	public final RegistryBoth getRegion() {
		return Address.getRegion();
	}

	@Override
	public final RegistryBoth getTrack() {
		return Address.getTrack();
	}

	@Override
	public final RegistryBoth getStation() {
		return Address.getStation();
	}


	@Override
	public final boolean setAddress(String s) {
		this.Address.setAddress(s);
		updateSign();		
		return true;
	}

	@Override
	public boolean setAddress(String s, String name) {
		(new ComponentSign(this.getLocation().getBlock())).setLine(2, name);		
		return setAddress(s);
	}

	@Override
	public boolean isTrain() {
		return this.Address.isTrain();
	}

	@Override
	public boolean setAddress(com.github.catageek.ByteCart.Routing.Address a, String name) {
		return this.setAddress(a.toString(), name);
	}

	@Override
	public boolean setTrain(boolean istrain) {
		this.Address.setIsTrain(istrain);
		updateSign();
		return true;
	}
	
	private void updateSign() {
		(new ComponentSign(this.getLocation().getBlock())).setLine(3, this.Address.toString());		
	}

	@Override
	public boolean isValid() {
		return this.Address.isValid;
	}

	@Override
	public void remove() {
		this.Address.remove();
		(new ComponentSign(this.getLocation().getBlock())).setLine(3, "");		
	}
	
	@Override
	final public String toString() {
		return Address.toString();
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}
