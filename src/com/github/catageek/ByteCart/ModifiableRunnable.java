package com.github.catageek.ByteCart;

import org.bukkit.inventory.Inventory;

public interface ModifiableRunnable<T> extends Runnable {

	void SetParam(Inventory inventory);

}
