package com.github.catageek.ByteCart.Routing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

public final class RoutingTableExchange {

	private Map<Integer, Integer> tablemap = new HashMap<Integer, Integer>();

	private final Updater.Level Level;

	private final int Region;

	//internal variable used by updaters
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

	protected void setRoute(int number, int distance) {
		tablemap.put(number, distance);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : setting counter of ring " + number + " to " + distance);
	}

	protected int getMinDistance(RoutingTable routes, DirectionRegistry from) {
		Iterator<Entry<Integer, Integer>> it = routes.getEntrySet().iterator();

		if (! it.hasNext())
			return -1;

		// we skip route 0
		it.next();

		Entry<Integer, Integer> tmp;
		int min = 10000, ret = -1; // big value

		while (it.hasNext()) {
			tmp = it.next();
			if (routes.getDirection(tmp.getKey()).getAmount() != from.getAmount()) {
				if (! this.hasRouteTo(tmp.getKey())) {
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : found ring " + tmp.getKey() + " was never visited");
					return tmp.getKey();
				}

				else {
					if (getDistance(tmp.getKey()) < min) {
						min = getDistance(tmp.getKey());
						ret = tmp.getKey();
					}
				}
			}
		}
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : minimum found ring " + ret + " with " + min);
		return ret;
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
