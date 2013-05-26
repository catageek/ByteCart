package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.entity.Player;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryBoth;

final class AddressBook implements AddressRouted {

	public enum Parameter {
		DESTINATION("net.dst.addr"),
		RETURN("net.src.addr"),
		TTL("net.ttl"),
		TRAIN("net.train");

		private final String name;

		Parameter(String s) {
			name = s;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}

	private final Ticket ticket;
	private final Parameter parameter;

	AddressBook(Ticket ticket, Parameter parameter) {
		this.ticket = ticket;
		this.parameter = parameter;
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
		return ticket.getString(Parameter.TRAIN, "false").equalsIgnoreCase("true");
	}

	@Override
	public boolean setAddress(String s) {
		return setAddress(s, null);
	}

	@Override
	public boolean setAddress(String value, String stationname) {
		boolean ret = this.ticket.setEntry(parameter, value);

		if (parameter.equals(Parameter.DESTINATION)) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : set title");
			ticket.appendTitle(stationname, value);
		}
		return ret;
	}

	@Override
	public boolean setAddress(Address a, String name) {
		return setAddress(a.toString(), name);
	}

	@Override
	public boolean setTrain(boolean istrain) {
		if (istrain)
			return ticket.setEntry(Parameter.TRAIN, "true");
		ticket.remove(Parameter.TRAIN);
		return true;
	}

	@Override
	public boolean isValid() {
		return getAddress().isValid();
	}

	@Override
	public int getTTL() {
		return ticket.getInt(Parameter.TTL, ByteCart.myPlugin.getConfig().getInt("TTL.value"));
	}

	@Override
	public void updateTTL(int i) {
		ticket.setEntry(Parameter.TTL, ""+i);
	}

	@Override
	public void initializeTTL() {
		this.ticket.resetValue(Parameter.TTL, "64");
	}

	@Override
	public String toString() {
		return getAddress().toString();
	}

	private Address getAddress() {
		String defaultaddr = ByteCart.myPlugin.getConfig().getString("EmptyCartsDefaultRoute", "0.0.0");
		return new AddressString(ticket.getString(parameter, defaultaddr));
	}

	@Override
	public void remove() {
		ticket.remove(parameter);
	}

	@Override
	public boolean isReturnable() {
		return ticket.getString(Parameter.RETURN) != null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void finalizeAddress() {
		ticket.close();
		if (ticket.getTicketHolder() instanceof Player) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : update player inventory");
			((Player) ticket.getTicketHolder()).updateInventory();
		}
	}
}
