package com.github.catageek.ByteCart.Signs;

import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.Updater.Level;

public interface BCSign {
	public Level getLevel();
	public Vehicle getVehicle();
	public Address getSignAddress();
}
