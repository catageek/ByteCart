package com.github.catageek.ByteCart.AddressLayer;

import com.github.catageek.ByteCart.AddressLayer.AddressBook.Parameter;
import com.github.catageek.ByteCart.HAL.RegistryBoth;

final class ReturnAddressBook implements Returnable {

	ReturnAddressBook(Ticket ticket, Parameter parameter) {
		this.address = new AddressBook(ticket, parameter);
	}

	private final AddressBook address;

	@Override
	public RegistryBoth getRegion() {
		return address.getRegion();
	}

	@Override
	public RegistryBoth getTrack() {
		return address.getTrack();
	}

	@Override
	public RegistryBoth getStation() {
		return address.getStation();
	}

	@Override
	public boolean isTrain() {
		return address.isTrain();
	}

	@Override
	public boolean setAddress(String s) {
		return address.setAddress(s);
	}

	@Override
	public boolean setAddress(String s, String name) {
		return address.setAddress(s);
	}

	@Override
	public boolean setAddress(Address a, String name) {
		return address.setAddress(a, name);
	}

	@Override
	public boolean setTrain(boolean istrain) {
		return address.setTrain(istrain);
	}

	@Override
	public boolean isValid() {
		return address.isValid();
	}

	@Override
	public void remove() {
		address.remove();
	}

	@Override
	public int getTTL() {
		return address.getTTL();
	}

	@Override
	public void updateTTL(int i) {
		address.updateTTL(i);
	}

	@Override
	public void initializeTTL() {
		address.initializeTTL();
	}

	@Override
	public boolean isReturnable() {
		return address.isReturnable();
	}

	@Override
	public String toString() {
		return address.toString();
	}

	@Override
	public void finalizeAddress() {
		address.finalizeAddress();
	}
}
