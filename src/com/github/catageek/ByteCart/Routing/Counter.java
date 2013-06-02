package com.github.catageek.ByteCart.Routing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

public final class Counter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6858180714411403984L;
	private final Map<Integer, Integer> map = new HashMap<Integer,Integer>();

	public Counter() {
	}

	public int getCount(int counter) {
		return this.map.containsKey(counter) ? this.map.get(counter) : 0;
	}

	public void incrementCount(int counter) {
		incrementCount(counter, 1);
	}

	public void incrementCount(int counter, int value) {
		this.map.put(counter, getCount(counter) + value);
	}


	public void setCount(int counter, int amount) {
		this.map.put(counter, amount);
	}

	public int firstEmpty() {
		int i = 1;
		while (getCount(i) != 0) {
			i++;
		}
		return i;
	}

	public void reset(int counter) {
		this.map.remove(counter);
	}

	public void resetAll() {
		this.map.clear();
	}

	public boolean isAllFull(int start, int end) {
		Iterator<Integer> it = map.keySet().iterator();
		int limit = 64;
		int count;
		while (it.hasNext()) {
			if ((count = it.next()) >= start && count <= end && map.get(count) < limit)
				return false;
		}
		return true;
	}

	public int getMinimum(RoutingTable routes, DirectionRegistry from) {
		Iterator<RouteNumber> it = routes.getOrderedRouteNumbers();
		int min = 10000000;  //big value
		int index = -1;
		while (it.hasNext()) {
			int ring = it.next().value();
			
			if (ring == 0)
				continue;
			
			if (! map.containsKey(ring))
				return ring;

			int value;
			if((value = map.get(ring)) < min
					&& routes.getDirection(ring).getAmount() != from.getAmount()
					&& ring != 0) {
				min = value;
				index  = ring;
			}
		}		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : minimum found ring " + index + " with " + min);
		return index;

	}

	@Override
	public final String toString() {
		Iterator<Integer> it = map.keySet().iterator(); 
		String s = "";
		while (it.hasNext()) {
			int ring = it.next();
			int count = map.get(ring);
			s += "ByteCart: Count for ring " + ring + " = " + count + "\n";
		}
		return s;
	}

	public int getCounterLength() {
		return 32;
	}


}
