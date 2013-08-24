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

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.Storage.PartitionedHashSet;
import com.github.catageek.ByteCart.Storage.ExternalizableTreeMap;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

/**
 * A routing table in a book
 */
final class RoutingTableBook extends AbstractRoutingTable implements
RoutingTable, Externalizable {

	private boolean wasModified = false;

	private ExternalizableTreeMap<RouteNumber,RouteProperty> map = new ExternalizableTreeMap<RouteNumber,RouteProperty>();

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

	RoutingTableBook(Inventory inv) {
		this.inventory = inv;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTable#clear(boolean)
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
	 * @see com.github.catageek.ByteCart.Routing.RoutingTable#getRouteNumbers()
	 */
	@Override
	public final Iterator<RouteNumber> getOrderedRouteNumbers() {
		Iterator<RouteNumber> it = ((SortedSet<RouteNumber>) map.keySet()).iterator();
		return it;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getMetric(int, com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public int getMetric(int entry, DirectionRegistry direction) {
		SortedMap<Metric, PartitionedHashSet<DirectionRegistry>> smap;
		RouteNumber route = new RouteNumber(entry);
		if (map.containsKey(route) && (smap = map.get(route).getMap()) != null
				&& ! smap.isEmpty()) {
			Iterator<Metric> it = smap.keySet().iterator();
			Metric d;
			while (it.hasNext())
				if (smap.get((d = it.next())).contains(direction))
					return d.value();
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getMinMetric(int)
	 */
	@Override
	public Metric getMinMetric(int entry) {
		SortedMap<Metric, PartitionedHashSet<DirectionRegistry>> smap;
		RouteNumber route = new RouteNumber(entry);
		if (map.containsKey(route) && (smap = map.get(route).getMap()) != null
				&& ! smap.isEmpty()) {
			return smap.firstKey();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#setEntry(int, com.github.catageek.ByteCart.Util.DirectionRegistry, com.github.catageek.ByteCart.Routing.Metric)
	 */
	private void setMapEntry(int entry, DirectionRegistry direction, Metric metric) {

		RouteNumber route = new RouteNumber(entry);
		Metric dist = new Metric(metric);
		RouteProperty smap;
		PartitionedHashSet<DirectionRegistry> set;

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


	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#setEntry(int, com.github.catageek.ByteCart.Util.DirectionRegistry, com.github.catageek.ByteCart.Routing.Metric)
	 */
	@Override
	public void setEntry(int entry, DirectionRegistry direction, Metric metric) {
		setMapEntry(entry, direction, metric);
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
	public DirectionRegistry getDirection(int entry) {
		RouteNumber route = new RouteNumber(entry);
		Set<DirectionRegistry> set;
		TreeMap<Metric, PartitionedHashSet<DirectionRegistry>> pmap;
		if (map.containsKey(route) && (pmap = map.get(route).getMap()) != null && ! pmap.isEmpty()) {
			set = pmap.firstEntry().getValue();
			if (! set.isEmpty())
				return set.toArray(new DirectionRegistry[set.size()])[0];
			throw new AssertionError("Set<DirectionRegistry> in RoutingTable is empty.");
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.AbstractRoutingTable#getDirectlyConnectedList(com.github.catageek.ByteCart.Util.DirectionRegistry)
	 */
	@Override
	public Set<Integer> getDirectlyConnectedList(DirectionRegistry direction) {
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
					&& set.contains(direction)) {
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
	protected Set<Integer> getNotDirectlyConnectedList(
			DirectionRegistry direction) {
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
							&& set.contains(direction))) {
				// extract routes going to directions with distance > 0
				smap = smap.tailMap(one);
				Iterator<Metric> it2 = smap.keySet().iterator();
				while (it2.hasNext()) {
					Metric d = it2.next();
					if (smap.get(d).contains(direction)) {
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
	public void removeEntry(int entry, DirectionRegistry from) {
		RouteNumber route = new RouteNumber(entry);
		TreeMap<Metric,PartitionedHashSet<DirectionRegistry>> smap;
		Set<DirectionRegistry> set;

		if (map.containsKey(route) && (smap = map.get(route).getMap()) != null) {
			Iterator<Metric> it = smap.keySet().iterator();
			while (it.hasNext()) {
				wasModified |= (set = smap.get(it.next())).remove(from);
				if (set.isEmpty())
					it.remove();
				if (smap.isEmpty())
					map.remove(route);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTable#serialize()
	 */
	@Override
	public void serialize() throws IOException {
		if (! wasModified)
			return;
		try (BookFile file = new BookFile(inventory, 0, true)) {
			file.clear();
			ObjectOutputStream oos = new ObjectOutputStream(file.getOutputStream());
			oos.writeObject(this);
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : serialize() : object written, now closing");
			oos.flush();
			wasModified = false;
		}
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException,
	ClassNotFoundException {
		this.map = (ExternalizableTreeMap<RouteNumber,RouteProperty>) in.readObject();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(map);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Routing.RoutingTable#size()
	 */
	@Override
	public int size() {
		return map.size();
	}
}
