package com.github.catageek.ByteCart.Routing;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.IO.BookProperties;
import com.github.catageek.ByteCart.IO.BookProperties.Conf;

public final class AddressBook implements AddressRouted {
	
	private final BookProperties Properties;
	private final String Parameter;
	private final String TTL = "net.ttl";
	private final Inventory Inventory;
	
	public AddressBook(Inventory inv, int index, String parameter) {
		Properties = new BookProperties(inv, index, Conf.NETWORK);
		Parameter = parameter;
		Inventory = inv;
	}

	public AddressBook(Inventory inv, int index) {
		this(inv, index, "net.dst.adr");
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
		Properties.setProperty(Parameter, s);
		if (this.Inventory.getHolder() instanceof Player)
			((Player) this.Inventory.getHolder()).updateInventory();
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
	public void remove() {
		Properties.clearProperty(Parameter);
	}

	@Override
	public int getTTL() {
		return Properties.getInt(TTL, 0);
	}

	@Override
	public void updateTTL(int i) {
		Properties.setProperty(TTL, ""+i);

	}

	@Override
	public Address initializeTTL() {
		Properties.setProperty(TTL, "64");
		return getAddress();
	}
	
	@Override
	public String toString() {
		return getAddress().toString();
	}

	private Address getAddress() {
		String defaultaddr = ByteCart.myPlugin.getConfig().getString("EmptyCartsDefaultRoute", "0.0.0");
		return new AddressString(Properties.getString(Parameter, defaultaddr));
	}

}
