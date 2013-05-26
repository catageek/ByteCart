package com.github.catageek.ByteCart.EventManagement;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Storage.ExpirableSet;

final class UpdaterSet {

	// entries stay for 1h
	UpdaterSet() {
		long duration = ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60)*1200;
		updateSet = new ExpirableSet<Integer>(duration, false, "UpdaterRoutes");
	}

	private final ExpirableSet<Integer> updateSet;

	ExpirableSet<Integer> getMapRoutes() {
		return updateSet;
	}
	
	boolean isUpdater(Integer id) {
		return updateSet.contains(id);
	}
	
	void addUpdater(int id) {
		this.updateSet.add(id);
	}
}
