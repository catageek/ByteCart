package com.github.catageek.ByteCart.Routing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Wanderer.Counter;

/**
 * A map containing counters with id
 */
public final class BCCounter implements Serializable, Counter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6858180714411403984L;
	private final Map<Integer, Integer> map = new HashMap<Integer,Integer>();

	public BCCounter() {
	}

	/**
	 * Get a counter by its id
	 * 
	 * @param counter
	 * @return the counter
	 */
	@Override
	public int getCount(int counter) {
		return this.map.containsKey(counter) ? this.map.get(counter) : 0;
	}

	/**
	 * Increment the counter by 1
	 * 
	 * @param counter the counter id
	 */
	public void incrementCount(int counter) {
		incrementCount(counter, 1);
	}

	/**
	 * Add a value to a counter
	 * 
	 * @param counter the counter id
	 * @param value the value to add
	 */
	@Override
	public void incrementCount(int counter, int value) {
		this.map.put(counter, getCount(counter) + value);
	}


	/**
	 * Set the value of a counter
	 * 
	 * @param counter the counter id
	 * @param amount the value to set
	 */
	@Override
	public void setCount(int counter, int amount) {
		this.map.put(counter, amount);
	}

	/**
	 * Get the first empty counter id
	 * 
	 * @return the id
	 */
	public int firstEmpty() {
		int i = 1;
		while (getCount(i) != 0) {
			i++;
		}
		return i;
	}

	/**
	 * Reset a counter to zero
	 * 
	 * @param counter the id of the counter
	 */
	public void reset(int counter) {
		this.map.remove(counter);
	}

	/**
	 * Reset all counters to zero
	 */
	@Override
	public void resetAll() {
		this.map.clear();
	}

	/**
	 * Tell if counters have reached the amount of 64 or more
	 * 
	 * @param start the first counter id
	 * @param end the last counter id
	 * @return true if the amont of all counters between start and end (inclusive) are equal or superior to 64
	 */
	@Override
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

	/**
	 * Get the ring number that has the minimum counter value and the direction is different from from parameter
	 * 
	 * @param routes the routing table
	 * @param from the direction to exclude from the search
	 * @return the ring number, or 0 if no result
	 */
	public int getMinimum(RoutingTableWritable routes, DirectionRegistry from) {
		Iterator<Integer> it = routes.getOrderedRouteNumbers();
		int min = 10000000;  //big value
		int index = -1;
		while (it.hasNext()) {
			int ring = it.next();
			
			if (ring == 0)
				continue;
			
			if (! map.containsKey(ring))
				return ring;

			int value;
			if((value = map.get(ring)) < min
					&& routes.getDirection(ring) != from.getBlockFace()
					&& ring != 0) {
				min = value;
				index  = ring;
			}
		}		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : minimum found ring " + index + " with " + min);
		return index;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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

	/**
	 * @return the number of counters the map can contain
	 */
	public int getCounterLength() {
		return 32;
	}


}
