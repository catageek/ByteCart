package com.github.catageek.ByteCart.Storage;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.RoutingTableExchange;
import com.github.catageek.ByteCart.Routing.Updater;

public final class UpdaterManager {

	// entries stay for 1h
	public UpdaterManager() {
		long duration = ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60)*1200;
		UpdateMap = new ExpirableMap<Integer, RoutingTableExchange>(duration, false, "UpdaterRoutes");
	}

	private final ExpirableMap<Integer, RoutingTableExchange> UpdateMap;

	public ExpirableMap<Integer, RoutingTableExchange> getMapRoutes() {
		return UpdateMap;
	}
	
	public boolean isUpdater(Integer id) {
		return UpdateMap.contains(id);
	}
	
	public boolean isUpdater(Integer id, Updater.Level level) {
		return (UpdateMap.contains(id) && (UpdateMap.get(id).getLevel().number) == level.number);
	}
	
	public void addUpdater(int id, Updater.Level level, int region) {
		this.UpdateMap.put(id, new RoutingTableExchange(level, region));
	}

}
