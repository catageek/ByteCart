package com.github.catageek.ByteCart.Storage;

import com.github.catageek.ByteCart.ByteCart;

public final class WandererManager {

	// entries stay for 1h
	private final ExpirableMap<Integer, Integer> WandererMap;

	public WandererManager() {
		long duration = ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60)*1200;
		WandererMap = new ExpirableMap<Integer, Integer>(duration, false, "WandererMap");
	}
	public boolean isWanderer(int id) {
		return WandererMap.contains(id);
	}

	public Integer getRegion(int id) {
		return WandererMap.get(id);
	}

	public boolean isWanderer(Integer id, int region) {
		return (WandererMap.contains(id) && ((WandererMap.get(id) == region)));
	}
	
	public void addWanderer(int id, int region) {
		this.WandererMap.put(id, region);
	}


}
