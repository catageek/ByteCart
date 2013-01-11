package com.github.catageek.ByteCart.Routing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

public class RoutingTableExchange {

	private Map<Integer, Integer> tablemap = new HashMap<Integer, Integer>();

	private final Updater.Level Level;

	private final int Region;

	private int Current = -2;

	public int getDistance(int entry) {
		return tablemap.get(entry);
	}

	public int getSize() {
		return tablemap.size();
	}

	public boolean hasRouteTo(int ring) {
		return tablemap.containsKey(ring);
	}

	public Set<Entry<Integer,Integer>> getEntrySet() {
		return tablemap.entrySet();
	}

	public RoutingTableExchange(RoutingTable table, DirectionRegistry direction, Updater.Level level, int region) {
		this(level, region);
		for(int i = 0; i< table.getSize(); i++) {
			if( table.getDirection(i) != null && table.getDirection(i).getAmount() != direction.getAmount()) {
				tablemap.put(i, table.getDistance(i));
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : Route exchange : give ring " + i + " to " + table.getDirection(i).getBlockFace());
			}

		}
	}

	public RoutingTableExchange(Updater.Level level, int region) {
		this.Region = region;
		this.Level = level;
	}

	public Updater.Level getLevel() {
		return Level;
	}

	public int getRegion() {
		return Region;
	}

	public int getCurrent() {
		return Current;
	}

	public void setCurrent(int current) {
		Current = current;
	}

}
