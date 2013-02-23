package com.github.catageek.ByteCart.Routing;

import org.bukkit.entity.Player;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.Routing.BookParameter.Parameter;
import com.github.catageek.ByteCart.Util.Ticket;

public final class AddressBook implements AddressRouted {

	private final String TTL = "net.ttl";
	private final BookParameter parameter;
	private final Ticket ticket;

	AddressBook(Ticket ticket, Parameter parameter) {
		this.parameter = new BookParameter(ticket, parameter);
		this.ticket = ticket;
	}

	AddressBook(Ticket ticket) {
		this.parameter = new BookParameter(ticket, Parameter.DESTINATION);
		this.ticket = ticket;
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

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	@Override
	public boolean setAddress(String s) {
		return setAddress(s, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean setAddress(String s, String name) {
		parameter.getProperties().setProperty(parameter.getParameter().getName(), s);
		if (this.parameter.getParameter().equals(Parameter.DESTINATION))
			ticket.appendTitle(name, s);
		if (this.getTicket().getTicketHolder() instanceof Player)
			((Player) this.getTicket().getTicketHolder()).updateInventory();
		return true;
	}

	@Override
	public boolean setAddress(Address a, String name) {
		return setAddress(a.toString(), name);
	}

	@Override
	public boolean setTrain(boolean istrain) {
		Address address = getAddress();
		address.setTrain(istrain);
		return setAddress(address, null);
	}

	@Override
	public boolean isValid() {
		return getAddress().isValid();
	}

	@Override
	public int getTTL() {
		return parameter.getProperties().getInt(TTL, 64);
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
