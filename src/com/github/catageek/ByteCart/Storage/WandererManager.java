package com.github.catageek.ByteCart.Storage;

public final class WandererManager {

	// entries stay for 1h
	private final ExpirableMap<Integer, Integer> WandererMap = new ExpirableMap<Integer, Integer>(72000, false, "WandererMap");

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
