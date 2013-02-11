package com.github.catageek.ByteCart.Routing;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.Routing.BookParameter.Parameter;

public final class ReturnAddressBook implements Returnable {

	public ReturnAddressBook(Inventory inv, int index) {
		this.address = new AddressBook(inv, index, Parameter.RETURN);
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
	public boolean setAddress(Address a) {
		return address.setAddress(a);
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
	public Address initializeTTL() {
		return address.initializeTTL();
	}

	@Override
	public boolean isReturnable() {
		return address.isReturnable();
	}
}
