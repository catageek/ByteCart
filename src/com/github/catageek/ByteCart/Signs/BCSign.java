package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.HAL.IC;
import com.github.catageek.ByteCart.Routing.Updater.Level;

/**
 * A network sign should implement this
 */
public interface BCSign extends IC {
	/**
	 * Get the hierarchical level of the IC
	 *
	 * @return the level
	 */
	public Level getLevel();
	/**
	 * Get the vehicle that uses this IC
	 *
	 * @return the vehicle
	 */
	public Vehicle getVehicle();
	/**
	 * Get the address stored in the IC
	 *
	 * @return the address
	 */
	public Address getSignAddress();
	/**
	 * Get the address stored in the ticket
	 *
	 * @return the address
	 */
	public String getDestinationIP();
	/**
	 * Get the center of the IC.
	 *
	 * @return the center
	 */
	public Block getCenter();
}
