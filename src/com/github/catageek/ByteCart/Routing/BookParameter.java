package com.github.catageek.ByteCart.Routing;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.IO.BookProperties;
import com.github.catageek.ByteCart.IO.BookProperties.Conf;

final class BookParameter {

	private final BookProperties properties;
	private final Parameter parameter;
	private final Inventory inventory;

	public enum Parameter {
		DESTINATION("net.dst.addr"),
		RETURN("net.src.addr");
		
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
	
	public BookParameter(Inventory inv, int index, Parameter parameter) {
		properties = new BookProperties(inv, index, Conf.NETWORK);
		this.parameter = parameter;
		inventory = inv;
	}

	void remove() {
		properties.clearProperty(parameter.getName());
	}

	/**
	 * @return the parameter
	 */
	Parameter getParameter() {
		return parameter;
	}

	/**
	 * @return the inventory
	 */
	Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the properties
	 */
	BookProperties getProperties() {
		return properties;
	}

}