package com.github.catageek.ByteCart.IO;

import java.util.Comparator;
import java.util.Map;

/*
 * This comparator orders the keys of
 * the map by ascending distance to
 * integer Value of the values associated
 * with them 
 */

public final class InventoryWriterComparator<E> implements Comparator<E> {

	final private int Value;
	final private Map<Integer, Integer> Map; 
	
	public InventoryWriterComparator(int value, Map<Integer, Integer> map) {
		Value = value;
		Map = map;
	}
	
	
	
	@Override
	public int compare(E o1, E o2) {
		
		Integer a = (Map.get(o1) != null ? Map.get(o1) : 0) - this.getValue();
		Integer b = (Map.get(o2) != null ? Map.get(o2) : 0) - this.getValue();
		
//		return ((Integer) Math.abs(a)).compareTo((Integer) Math.abs(b));
		return b.compareTo(a);

		
	}



	/**
	 * @return the value
	 */
	private int getValue() {
		return Value;
	}




}
