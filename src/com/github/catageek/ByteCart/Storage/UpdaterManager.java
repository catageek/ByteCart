package com.github.catageek.ByteCart.Storage;

import com.github.catageek.ByteCart.Routing.RoutingTableExchange;
import com.github.catageek.ByteCart.Routing.Updater;

public final class UpdaterManager {

	// entries stay for 1h
	private final ExpirableMap<Integer, RoutingTableExchange> UpdateMap = new ExpirableMap<Integer, RoutingTableExchange>(72000, false, "UpdaterRoutes");

	public ExpirableMap<Integer, RoutingTableExchange> getMapRoutes() {
		return UpdateMap;
	}
	
	public boolean isUpdater(Integer id) {
		return UpdateMap.contains(id);
	}
	
	public boolean isUpdater(Integer id, Updater.Level level) {
		return (UpdateMap.contains(id) && (UpdateMap.get(id).getLevel().number & 7) == level.number);
	}
	
	public void addUpdater(int id, Updater.Level level, int region) {
		this.UpdateMap.put(id, new RoutingTableExchange(level, region));
	}

}
