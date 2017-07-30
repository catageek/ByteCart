package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.FileStorage.InventoryFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * A routing table in a book
 */
final class RoutingTableBookJSON extends AbstractRoutingTable implements
RoutingTableWritable {

	private boolean wasModified = false;
	
	private static class RouteEntry {
		private @Expose
		TreeMap<Integer,Set<BlockFace>> routes;
		
		RouteEntry() {
			this.routes = new TreeMap<Integer, Set<BlockFace>>();
		}
	}
	
	@Expose
	private Boolean readonly = false;
	
	@Expose
	private Set<BlockFace> interfaces = new HashSet<>();

	@Expose
	private TreeMap<Integer, RouteEntry> table = new TreeMap<>();

	private Inventory inventory;

	private int slot;

	/**
	 * Set the inventory
	 * 
	 * @param inventory the inventory
	 */
	final void setInventory(Inventory inventory, int slot) {
		this.inventory = inventory;
		this.slot = slot;
	}

	public RoutingTableBookJSON() {
	}

	RoutingTableBookJSON(Inventory inv, int slot) {
		this.inventory = inv;
		this.slot = slot;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#clear(boolean)
	 */
	@Override
	public void clear(boolean fullreset) {
		if (table.isEmpty())
			return;

		RouteEntry routes = null;
		BlockFace route_to_zero = null;
		
		if (! fullreset && table.containsKey(0)) {
			routes  = table.get(0);
			if (this.getMinMetric(0) == 0) {
				route_to_zero = this.getDirection(0);
			}
		}

		table.clear();
		interfaces.clear();

		if (! fullreset && routes != null) {
			table.put(0, routes);
			if (route_to_zero != null) {
				interfaces.add(route_to_zero);
			}
		}
			
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart : clear routing table table");
		wasModified = true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#getRouteNumbers()
	 */
	@Override
	public final Iterator<Integer> getOrderedRouteNumbers() {
		Iterator<Integer> it = ((SortedSet<Integer>) table.keySet()).iterator();
		return it;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getMetric(int, com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public int getMetric(int entry, BlockFace direction) {
		SortedMap<Integer, Set<BlockFace>> smap;
		if (table.containsKey(entry) && (smap = table.get(entry).routes) != null
				&& ! smap.isEmpty()) {
			Iterator<Integer> it = smap.keySet().iterator();
			int d;
			while (it.hasNext())
				if (smap.get((d = it.next())).contains(direction))
					return d;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getMinMetric(int)
	 */
	@Override
	public int getMinMetric(int entry) {
		SortedMap<Integer, Set<BlockFace>> smap;
		if (table.containsKey(entry) && (smap = table.get(entry).routes) != null
				&& ! smap.isEmpty()) {
			return smap.firstKey();
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#setEntry(int, com.github.catageek.ByteCart.Util.DirectionRegistry, com.github.catageek.ByteCart.Routing.Metric)
	 */
	private void setMapEntry(int entry, BlockFace direction, int metric) {

		RouteEntry smap;
		Set<BlockFace> set;

		if (metric < this.getMetric(entry, direction)) {
			this.removeEntry(entry, direction);
		}

		if ((smap = table.get(entry)) == null) {
			smap = new RouteEntry();
			table.put(entry, smap);
			wasModified = true;
		}

		if ((set = smap.routes.get(metric)) == null) {
			set = new HashSet<BlockFace>(3);
			smap.routes.put(metric, set);
			wasModified = true;
		}
		
		if (metric == 0) {
			wasModified |= interfaces.add(direction);
		}
		wasModified |= set.add(direction);
	}


	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#setEntry(int, com.github.catageek.ByteCart.Util.DirectionRegistry, com.github.catageek.ByteCart.Routing.Metric)
	 */
	@Override
	public void setEntry(int entry, BlockFace direction, int metric) {
		setMapEntry(entry, direction, metric);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#isEmpty(int)
	 */
	@Override
	public boolean isEmpty(int entry) {
		return ! table.containsKey(entry);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getDirection(int)
	 */
	@Override
	public BlockFace getDirection(int entry) {
		Set<BlockFace> set;
		TreeMap<Integer, Set<BlockFace>> pmap;
		if (table.containsKey(entry) && (pmap = table.get(entry).routes) != null && ! pmap.isEmpty()) {
			set = pmap.firstEntry().getValue();
			if (! set.isEmpty())
				return set.toArray(new BlockFace[set.size()])[0];
			throw new AssertionError("Set<DirectionRegistry> in RoutingTableWritable is empty.");
		}
		return null;
	}

	@Override
	public BlockFace getAllowedDirection(int entry) {
		TreeMap<Integer, Set<BlockFace>> pmap;
		if (table.containsKey(entry) && (pmap = table.get(entry).routes) != null) {
			for (Entry<Integer, Set<BlockFace>> metricroute : pmap.entrySet()) {
				for (BlockFace direction : metricroute.getValue()) {
					if (isAllowedDirection(direction)) {
						return direction;
					}
				}
				
			}
		}
		return null;
	}

	@Override
	public Boolean isAllowedDirection(BlockFace direction) {
		return interfaces.contains(direction);
	}

	@Override
	public void allowDirection(BlockFace direction, Boolean enable) {
		this.wasModified |= enable ? interfaces.add(direction) : interfaces.remove(direction);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getDirectlyConnectedList(com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public Set<Integer> getDirectlyConnectedList(BlockFace direction) {
		SortedMap<Integer, Integer> list = new TreeMap<Integer, Integer>();
		Iterator<Entry<Integer, RouteEntry>> it = table.entrySet().iterator();
		Entry<Integer, RouteEntry> entry;
		RouteEntry smap;
		Set<BlockFace> set;

		while (it.hasNext()) {
			entry = it.next();
			if ((smap = entry.getValue()) != null && smap.routes.containsKey(0)
					&& ! (set = smap.routes.get(0)).isEmpty()
					&& set.contains(direction)) {
				// just extract the connected route
				list.put(entry.getKey(), 0);
			}
		}
		return list.keySet();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getNotDirectlyConnectedList(com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public Set<Integer> getNotDirectlyConnectedList(BlockFace direction) {
		SortedMap<Integer, Integer> list = new TreeMap<Integer, Integer>();
		Iterator<Entry<Integer, RouteEntry>> it = table.entrySet().iterator();
		Entry<Integer, RouteEntry> entry;
		SortedMap<Integer, Set<BlockFace>> smap;
		Set<BlockFace> set;

		while (it.hasNext()) {
			entry = it.next();

			if ((smap = entry.getValue().routes) == null
					|| ! (smap = entry.getValue().routes).containsKey(0)
					|| ! (! (set = smap.get(0)).isEmpty()
							&& set.contains(direction))) {
				// extract routes going to directions with distance > 0
				smap = smap.tailMap(1);
				Iterator<Integer> it2 = smap.keySet().iterator();
				while (it2.hasNext()) {
					int d = it2.next();
					if (smap.get(d).contains(direction)) {
						list.put(entry.getKey(), d);
						break;
					}
				}
			}
		}
		return list.keySet();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#removeEntry(int, com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public void removeEntry(int entry, BlockFace from) {
		TreeMap<Integer, Set<BlockFace>> smap;
		Set<BlockFace> set;

		if (table.containsKey(entry) && (smap = table.get(entry).routes) != null) {
			Iterator<Integer> it = smap.keySet().iterator();
			while (it.hasNext()) {
				int metric = it.next();
				if (metric == 0) {
					wasModified |= interfaces.remove(from);
				}
				wasModified |= (set = smap.get(metric)).remove(from);
				if (set.isEmpty())
					it.remove();
				if (smap.isEmpty())
					table.remove(entry);
			}
		}
	}

	private RoutingTableBook convertToBinary() {
		final RoutingTableBook btable = new RoutingTableBook();
		btable.setInventory(this.inventory);
		for (Entry<Integer, RouteEntry> entry : table.entrySet()) {
			for (Entry<Integer, Set<BlockFace>> routemap : entry.getValue().routes.entrySet()) {
				for (BlockFace route : routemap.getValue()) {
					btable.setEntry(entry.getKey().intValue(), route, routemap.getKey().intValue());
				}
			}
		}
		return btable;
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#serialize()
	 */
	@Override
	public void serialize(boolean allowconversion) throws IOException {
		if (! this.wasModified || this.readonly)
			return;
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: Trying to store in JSON format");
		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(this);
		InventoryFile file = new InventoryFile(inventory, false, "RoutingTable");
		file.setDescription("Bytecart Routing Table");
		file.clear();
		file.getOutputStream().write(json.getBytes());
		try {
			file.flush();
		} 
		catch (IOException e) {
			if (allowconversion) {
				this.convertToBinary().serialize(false);
			}
			else
				throw e;
		}
/*
		wasModified = false;
		if (ByteCart.debug)
			ByteCart.log.info("Storing in JSON format successful");
*/	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#size()
	 */
	@Override
	public int size() {
		return table.size();
	}
}
