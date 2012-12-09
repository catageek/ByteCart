package com.github.catageek.ByteCart;

public final class UpdaterManager {

	// entries stay for 1h
	private EphemeralBlockMap<Integer, RoutingTableExchange> UpdateMap = new EphemeralBlockMap<Integer, RoutingTableExchange>(72000);
	
	public EphemeralBlockMap<Integer, RoutingTableExchange> getMap() {
		return UpdateMap;
	}
	
	public boolean isUpdater(Integer id) {
		return UpdateMap.hasEntry(id);
	}


}
