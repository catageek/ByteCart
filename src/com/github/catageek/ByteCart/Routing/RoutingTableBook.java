package com.github.catageek.ByteCart.Routing;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.FileStorage.InventoryFile;
import com.github.catageek.ByteCart.Storage.ExternalizableTreeMap;
import com.github.catageek.ByteCart.Storage.PartitionedHashSet;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

/**
 * A routing table in a book
 */
final class RoutingTableBook extends AbstractRoutingTable implements
RoutingTableWritable, Externalizable {

	private boolean wasModified = false;

	private ExternalizableTreeMap<RouteNumber, RouteProperty> map = new ExternalizableTreeMap<RouteNumber,RouteProperty>();

	private static final long serialVersionUID = -7013741680310224056L;
	private Inventory inventory;
	/**
	 * Set the inventory
	 * 
	 * @param inventory the inventory
	 */
	final void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public RoutingTableBook() {
	}

		/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#clear(boolean)
	 */
	@Override
	public void clear(boolean fullreset) {
		if (map.isEmpty())
			return;

		RouteProperty routes = null;
		RouteNumber route = new RouteNumber(0);

		if (! fullreset && map.containsKey(route))
			routes  = map.get(route);

		map.clear();

		if (! fullreset && routes != null)
			map.put(route, routes);

		if (ByteCart.debug)
			ByteCart.log.info("ByteCart : clear routing table map");
		wasModified = true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#getRouteNumbers()
	 */
	@Override
	public final Iterator<Integer> getOrderedRouteNumbers() {
		TreeSet<Integer> newmap = new TreeSet<Integer>();
		for(RouteNumber key : map.keySet()) {
			newmap.add(key.value());
		}
		return ((SortedSet<Integer>) newmap).iterator();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getMetric(int, com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public int getMetric(int entry, BlockFace direction) {
		SortedMap<Metric, PartitionedHashSet<DirectionRegistry>> smap;
		RouteNumber route = new RouteNumber(entry);
		if (map.containsKey(route) && (smap = map.get(route).getMap()) != null
				&& ! smap.isEmpty()) {
			Iterator<Metric> it = smap.keySet().iterator();
			Metric d;
			while (it.hasNext())
				if (smap.get((d = it.next())).contains(new DirectionRegistry(direction)))
					return d.value();
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getMinMetric(int)
	 */
	@Override
	public int getMinMetric(int entry) {
		SortedMap<Metric, PartitionedHashSet<DirectionRegistry>> smap;
		RouteNumber route = new RouteNumber(entry);
		if (map.containsKey(route) && (smap = map.get(route).getMap()) != null
				&& ! smap.isEmpty()) {
			return smap.firstKey().value();
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#setEntry(int, com.github.catageek.ByteCart.Util.DirectionRegistry, com.github.catageek.ByteCart.Routing.Metric)
	 */
	private void setMapEntry(int entry, DirectionRegistry direction, Metric metric) {

		RouteNumber route = new RouteNumber(entry);
		Metric dist = new Metric(metric);
		RouteProperty smap;
		PartitionedHashSet<DirectionRegistry> set;

		if (metric.value() < this.getMetric(entry, direction.getBlockFace())) {
			this.removeEntry(entry, direction.getBlockFace());
		}

		if ((smap = map.get(route)) == null) {
			smap = new RouteProperty();
			map.put(route, smap);
			wasModified = true;
		}

		if ((set = smap.getMap().get(dist)) == null) {
			set = new PartitionedHashSet<DirectionRegistry>(3);
			smap.getMap().put(dist, set);
			wasModified = true;
		}
		wasModified |= set.add(direction);
	}
	
	private RoutingTableBookJSON convertToJSON() {
		final RoutingTableBookJSON jtable = new RoutingTableBookJSON();
		jtable.setInventory(this.inventory, 0);
		for (Entry<RouteNumber, RouteProperty> entry : map.entrySet()) {
			for (Entry<Metric, PartitionedHashSet<DirectionRegistry>> routemap : entry.getValue().getMap().entrySet()) {
				for (DirectionRegistry route : routemap.getValue()) {
					jtable.setEntry(entry.getKey().value(), route.getBlockFace(), routemap.getKey().value());
				}
			}
		}
		return jtable;
	}


	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#setEntry(int, com.github.catageek.ByteCart.Util.DirectionRegistry, com.github.catageek.ByteCart.Routing.Metric)
	 */
	@Override
	public void setEntry(int entry, BlockFace direction, int metric) {
		setMapEntry(entry, new DirectionRegistry(direction), new Metric(metric));
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#isEmpty(int)
	 */
	@Override
	public boolean isEmpty(int entry) {
		return ! map.containsKey(new RouteNumber(entry));
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getDirection(int)
	 */
	@Override
	public BlockFace getDirection(int entry) {
		RouteNumber route = new RouteNumber(entry);
		Set<DirectionRegistry> set;
		TreeMap<Metric, PartitionedHashSet<DirectionRegistry>> pmap;
		if (map.containsKey(route) && (pmap = map.get(route).getMap()) != null && ! pmap.isEmpty()) {
			set = pmap.firstEntry().getValue();
			if (! set.isEmpty())
				return set.toArray(new DirectionRegistry[set.size()])[0].getBlockFace();
			throw new AssertionError("Set<DirectionRegistry> in RoutingTableWritable is empty.");
		}
		return null;
	}

	@Override
	public BlockFace getAllowedDirection(int entry) {
		return getDirection(entry);
	}

	@Override
	public Boolean isAllowedDirection(BlockFace direction) {
		return true;
	}

	@Override
	public void allowDirection(BlockFace direction, Boolean enable) {
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getDirectlyConnectedList(com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public Set<Integer> getDirectlyConnectedList(BlockFace direction) {
		SortedMap<Integer, Metric> list = new TreeMap<Integer, Metric>();
		Iterator<Entry<RouteNumber, RouteProperty>> it = map.entrySet().iterator();
		Entry<RouteNumber, RouteProperty> entry;
		Metric zero = new Metric(0);
		TreeMap<Metric, PartitionedHashSet<DirectionRegistry>> smap;
		PartitionedHashSet<DirectionRegistry> set;

		while (it.hasNext()) {
			entry = it.next();
			if ((smap = entry.getValue().getMap()) != null && smap.containsKey(zero)
					&& ! (set = smap.get(zero)).isEmpty()
					&& set.contains(new DirectionRegistry(direction))) {
				// just extract the connected route
				list.put(entry.getKey().value(), zero);
			}
		}
		return list.keySet();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getNotDirectlyConnectedList(com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public Set<Integer> getNotDirectlyConnectedList(
			BlockFace direction) {
		SortedMap<Integer, Metric> list = new TreeMap<Integer, Metric>();
		Iterator<Entry<RouteNumber, RouteProperty>> it = map.entrySet().iterator();
		Entry<RouteNumber, RouteProperty> entry;
		Metric zero = new Metric(0);
		Metric one = new Metric(1);
		SortedMap<Metric, PartitionedHashSet<DirectionRegistry>> smap;
		PartitionedHashSet<DirectionRegistry> set;

		while (it.hasNext()) {
			entry = it.next();

			if ((smap = entry.getValue().getMap()) == null
					|| ! (smap = entry.getValue().getMap()).containsKey(zero)
					|| ! (! (set = smap.get(zero)).isEmpty()
							&& set.contains(new DirectionRegistry(direction)))) {
				// extract routes going to directions with distance > 0
				smap = smap.tailMap(one);
				Iterator<Metric> it2 = smap.keySet().iterator();
				while (it2.hasNext()) {
					Metric d = it2.next();
					if (smap.get(d).contains(new DirectionRegistry(direction))) {
						list.put(entry.getKey().value(), d);
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
		RouteNumber route = new RouteNumber(entry);
		TreeMap<Metric,PartitionedHashSet<DirectionRegistry>> smap;
		Set<DirectionRegistry> set;

		if (map.containsKey(route) && (smap = map.get(route).getMap()) != null) {
			Iterator<Metric> it = smap.keySet().iterator();
			while (it.hasNext()) {
				wasModified |= (set = smap.get(it.next())).remove(new DirectionRegistry(from));
				if (set.isEmpty())
					it.remove();
				if (smap.isEmpty())
					map.remove(route);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#serialize()
	 */
	@Override
	public void serialize(boolean allowconversion) throws IOException {
		if (! wasModified)
			return;
		try {
			// we try to convert to JSON
			if (allowconversion) { 
				this.convertToJSON().serialize(false);
				return;
			}
		} catch(IOException e) {
		}
		// if not possible, try with binary format
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: JSON conversion failed, trying binary format");
		InventoryFile file = new InventoryFile(inventory, true, "RoutingTableBinary");
		file.setDescription("Bytecart Routing Table");
		file.clear();
		ObjectOutputStream oos = new ObjectOutputStream(file.getOutputStream());
		oos.writeObject(this);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: binary object written, now closing");
		oos.flush();
		wasModified = false;	
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException,
	ClassNotFoundException {
		this.map = (ExternalizableTreeMap<RouteNumber, RouteProperty>) in.readObject();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(map);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTableWritable#size()
	 */
	@Override
	public int size() {
		return map.size();
	}
}
