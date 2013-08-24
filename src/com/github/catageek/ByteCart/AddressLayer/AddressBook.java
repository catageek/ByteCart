package com.github.catageek.ByteCart.AddressLayer;

import org.bukkit.entity.Player;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryBoth;

/**
 * A class implementing an address using a book as support
 */
final class AddressBook implements AddressRouted {

	/**
	 * Parameters name used in the book
	 */
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

	/**
	 * The ticket used as support for the address
	 */
	private final Ticket ticket;
	
	/**
	 * The type of the address
	 */
	private final Parameter parameter;

	/**
	 * Creates an address using a ticket and a parameter
	 * 
	 * @param ticket the ticket where to store the address
	 * @param parameter the type of address to store
	 */
	AddressBook(Ticket ticket, Parameter parameter) {
		this.ticket = ticket;
		this.parameter = parameter;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getRegion()
	 */
	@Override
	public RegistryBoth getRegion() {
		return getAddress().getRegion();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getTrack()
	 */
	@Override
	public RegistryBoth getTrack() {
		return getAddress().getTrack();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#getStation()
	 */
	@Override
	public RegistryBoth getStation() {
		return getAddress().getStation();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isTrain()
	 */
	@Override
	public boolean isTrain() {
		return ticket.getString(Parameter.TRAIN, "false").equalsIgnoreCase("true");
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String)
	 */
	@Override
	public boolean setAddress(String s) {
		return setAddress(s, null);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(com.github.catageek.ByteCart.AddressLayer.Address, java.lang.String)
	 */
	@Override
	public boolean setAddress(Address a, String name) {
		return setAddress(a.toString(), name);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setTrain(boolean)
	 */
	@Override
	public boolean setTrain(boolean istrain) {
		if (istrain)
			return ticket.setEntry(Parameter.TRAIN, "true");
		ticket.remove(Parameter.TRAIN);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isValid()
	 */
	@Override
	public boolean isValid() {
		return getAddress().isValid();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.AddressRouted#getTTL()
	 */
	@Override
	public int getTTL() {
		return ticket.getInt(Parameter.TTL, ByteCart.myPlugin.getConfig().getInt("TTL.value"));
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.AddressRouted#updateTTL(int)
	 */
	@Override
	public void updateTTL(int i) {
		ticket.setEntry(Parameter.TTL, ""+i);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.AddressRouted#initializeTTL()
	 */
	@Override
	public void initializeTTL() {
		this.ticket.resetValue(Parameter.TTL, "64");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAddress().toString();
	}

	/**
	 * Read the address from the ticket
	 *
	 * @return the address
	 */
	private Address getAddress() {
		String defaultaddr = ByteCart.myPlugin.getConfig().getString("EmptyCartsDefaultRoute", "0.0.0");
		return new AddressString(ticket.getString(parameter, defaultaddr));
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#remove()
	 */
	@Override
	public void remove() {
		ticket.remove(parameter);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#isReturnable()
	 */
	@Override
	public boolean isReturnable() {
		return ticket.getString(Parameter.RETURN) != null;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#finalizeAddress()
	 */
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
