package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.block.Block;

import com.github.catageek.ByteCart.IO.AbstractComponent;
import com.github.catageek.ByteCart.IO.ComponentSign;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.HAL.RegistryBoth;


/**
 * Implements an address using a line of a sign as support
 */
final class AddressSign extends AbstractComponent implements Address {
	
	/**
	 * String used as internal storage
	 */
	private final AddressString Address;

	/**
	 * Creates the address
	 * 
	 * @param block the sign block containing the address
	 * @param ligne the line number containing the address
	 */
	AddressSign(Block block, int ligne) {
		
		super(block);
		
		this.Address = new AddressString((new ComponentSign(block)).getLine(ligne), false);
		
/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: creating AddressSign line #" + ligne + " at " + block.getLocation().toString());
	*/	
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getRegion()
	 */
	@Override
	public final RegistryBoth getRegion() {
		return Address.getRegion();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getTrack()
	 */
	@Override
	public final RegistryBoth getTrack() {
		return Address.getTrack();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getStation()
	 */
	@Override
	public final RegistryBoth getStation() {
		return Address.getStation();
	}


	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String)
	 */
	@Override
	public final boolean setAddress(String s) {
		this.Address.setAddress(s);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setAddress(String s, String name) {
		(new ComponentSign(this.getBlock())).setLine(2, name);		
		return setAddress(s);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isTrain()
	 */
	@Override
	public boolean isTrain() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setTrain(boolean)
	 */
	@Override
	public boolean setTrain(boolean istrain) {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#finalizeAddress()
	 */
	@Override
	public final void finalizeAddress() {
		(new ComponentSign(this.getBlock())).setLine(3, this.Address.toString());		
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isValid()
	 */
	@Override
	public boolean isValid() {
		return this.Address.isValid;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#remove()
	 */
	@Override
	public void remove() {
		this.Address.remove();
		(new ComponentSign(this.getBlock())).setLine(3, "");		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	final public String toString() {
		return Address.toString();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isReturnable()
	 */
	@Override
	public boolean isReturnable() {
		return false;
	}
}
