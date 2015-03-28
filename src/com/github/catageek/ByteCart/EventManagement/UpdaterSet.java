package com.github.catageek.ByteCart.EventManagement;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Storage.ExpirableSet;
import com.github.catageek.ByteCartAPI.Event.UpdaterRemoveEvent;

/**
 * A set for integers with a timeout of 1h
 */
final class UpdaterSet {

	// entries stay for 1h
	UpdaterSet() {
		long duration = ByteCart.myPlugin.getConfig().getInt("updater.timeout", 60)*1200;
		updateSet = new ExpirableSet<Integer>(duration, false, "UpdaterRoutes");
	}

	private final ExpirableSet<Integer> updateSet;

	ExpirableSet<Integer> getMap() {
		return updateSet;
	}
	
	boolean isUpdater(Integer id) {
		return updateSet.contains(id);
	}
	
	void addUpdater(int id) {
		this.updateSet.add(id);
	}
	
	void clear() {
		Iterator<Integer> it = updateSet.getIterator();
		while (it.hasNext()) {
			Bukkit.getServer().getPluginManager().callEvent((Event) new UpdaterRemoveEvent(it.next()));
		}
		updateSet.clear();
	}
}
