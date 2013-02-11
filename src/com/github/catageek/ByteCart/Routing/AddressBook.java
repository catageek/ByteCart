package com.github.catageek.ByteCart.Routing;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.Routing.BookParameter.Parameter;

public final class AddressBook implements AddressRouted {

	private final String TTL = "net.ttl";
	private final BookParameter parameter;
	
	AddressBook(Inventory inv, int index, Parameter parameter) {
		this.parameter = new BookParameter(inv, index, parameter);
	}

	public AddressBook(Inventory inv, int index) {
		this.parameter = new BookParameter(inv, index, Parameter.DESTINATION);
	}

	@Override
	public RegistryBoth getRegion() {
		return getAddress().getRegion();
	}

	@Override
	public RegistryBoth getTrack() {
		return getAddress().getTrack();
	}

	@Override
	public RegistryBoth getStation() {
		return getAddress().getStation();
	}

	@Override
	public boolean isTrain() {
		return getAddress().isTrain();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean setAddress(String s) {
		parameter.getProperties().setProperty(parameter.getParameter().getName(), s);
		if (this.parameter.getInventory().getHolder() instanceof Player)
			((Player) this.parameter.getInventory().getHolder()).updateInventory();
		return true;
	}

	@Override
	public boolean setAddress(Address a) {
		return setAddress(a.toString());
	}

	@Override
	public boolean setTrain(boolean istrain) {
		Address address = getAddress();
		address.setTrain(istrain);
		return setAddress(address);
	}

	@Override
	public boolean isValid() {
		return getAddress().isValid();
	}

	@Override
	public int getTTL() {
		return parameter.getProperties().getInt(TTL, 0);
	}

	@Override
	public void updateTTL(int i) {
		parameter.getProperties().setProperty(TTL, ""+i);

	}

	@Override
	public Address initializeTTL() {
		parameter.getProperties().setProperty(TTL, "64");
		return getAddress();
	}

	@Override
	public String toString() {
		return getAddress().toString();
	}

	private Address getAddress() {
		String defaultaddr = ByteCart.myPlugin.getConfig().getString("EmptyCartsDefaultRoute", "0.0.0");
		return new AddressString(parameter.getProperties().getString(parameter.getParameter().getName(), defaultaddr));
	}

	/**
	 * @return the parameter
	 */
	public BookParameter getParameter() {
		return parameter;
	}

	@Override
	public void remove() {
		parameter.remove();
	}

	@Override
	public boolean isReturnable() {
		return parameter.getProperties().getString(Parameter.RETURN.getName()) != null;
	}

}
