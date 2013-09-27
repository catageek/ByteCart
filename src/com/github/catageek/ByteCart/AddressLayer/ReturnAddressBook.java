package com.github.catageek.ByteCart.AddressLayer;

import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.HAL.RegistryBoth;

/**
 * A class implementing a return address in a book
 */
final class ReturnAddressBook implements Returnable {

	/**
	 * Creates a return address from a ticket of a certain type
	 * 
	 * @param ticket the ticket to use as support
	 * @param parameter the type of the address
	 */
	ReturnAddressBook(Ticket ticket, Parameter parameter) {
		this.address = new AddressBook(ticket, parameter);
	}

	/**
	 * The book address that this class will proxify
	 */
	private final AddressBook address;

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getRegion()
	 */
	@Override
	public RegistryBoth getRegion() {
		return address.getRegion();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getTrack()
	 */
	@Override
	public RegistryBoth getTrack() {
		return address.getTrack();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getStation()
	 */
	@Override
	public RegistryBoth getStation() {
		return address.getStation();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isTrain()
	 */
	@Override
	public boolean isTrain() {
		return address.isTrain();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String)
	 */
	@Override
	public boolean setAddress(String s) {
		return address.setAddress(s);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setAddress(String s, String name) {
		return address.setAddress(s);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(com.github.catageek.ByteCart.AddressLayer.Address, java.lang.String)
	 */
	@Override
	public boolean setAddress(Address a, String name) {
		return address.setAddress(a, name);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setTrain(boolean)
	 */
	@Override
	public boolean setTrain(boolean istrain) {
		return address.setTrain(istrain);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isValid()
	 */
	@Override
	public boolean isValid() {
		return address.isValid();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#remove()
	 */
	@Override
	public void remove() {
		address.remove();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.AddressRouted#getTTL()
	 */
	@Override
	public int getTTL() {
		return address.getTTL();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.AddressRouted#updateTTL(int)
	 */
	@Override
	public void updateTTL(int i) {
		address.updateTTL(i);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.AddressRouted#initializeTTL()
	 */
	@Override
	public void initializeTTL() {
		address.initializeTTL();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isReturnable()
	 */
	@Override
	public boolean isReturnable() {
		return address.isReturnable();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return address.toString();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#finalizeAddress()
	 */
	@Override
	public void finalizeAddress() {
		address.finalizeAddress();
	}
}
