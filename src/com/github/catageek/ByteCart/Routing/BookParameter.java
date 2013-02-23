package com.github.catageek.ByteCart.Routing;

import com.github.catageek.ByteCart.IO.BookProperties;
import com.github.catageek.ByteCart.IO.BookProperties.Conf;
import com.github.catageek.ByteCart.Util.Ticket;

final class BookParameter {

	private final BookProperties properties;
	private final Parameter parameter;

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
	
	public BookParameter(Ticket ticket, Parameter parameter) {
		properties = new BookProperties(ticket, Conf.NETWORK);
		this.parameter = parameter;
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
	 * @return the properties
	 */
	BookProperties getProperties() {
		return properties;
	}


}