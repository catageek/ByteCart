package com.github.catageek.ByteCart.Storage;

import com.github.catageek.ByteCart.Routing.RoutingTableExchange;

public final class UpdaterManager {

	// entries stay for 1h
	private ExpirableMap<Integer, RoutingTableExchange> UpdateMap = new ExpirableMap<Integer, RoutingTableExchange>(72000, false, "Updater");
	
	public ExpirableMap<Integer, RoutingTableExchange> getMap() {
		return UpdateMap;
	}
	
	public boolean isUpdater(Integer id) {
		return UpdateMap.contains(id);
	}


}
