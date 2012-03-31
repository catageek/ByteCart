package com.github.catageek.ByteCart;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;

public abstract class AbstractTriggeredIC extends AbstractIC {
	
	private final org.bukkit.entity.Vehicle Vehicle;
	private final org.bukkit.inventory.Inventory Inventory;

	public AbstractTriggeredIC(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block);
		this.Vehicle = vehicle;
		
		this.Inventory = this.extractInventory();
	}
	
	/**
	 * @return the vehicle
	 */
	final public org.bukkit.entity.Vehicle getVehicle() {
		return Vehicle;
	}
	
	final private org.bukkit.inventory.Inventory extractInventory() {
		// we load inventory of cart or player
		if (this.Vehicle != null) {

			if (this.getVehicle() instanceof StorageMinecart)
				return ((StorageMinecart) this.getVehicle()).getInventory();
			else if ((this.getVehicle() instanceof Minecart)
					&& (! this.getVehicle().isEmpty())
					&& ((Minecart) this.getVehicle()).getPassenger() instanceof Player) {

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: loading player inventory :" + ((Player) this.getVehicle().getPassenger()).getDisplayName());

				return ((Player) this.getVehicle().getPassenger()).getInventory();
			}
		}
		return Bukkit.createInventory(null, 27);

	}

	/**
	 * @return the inventory
	 */
	public org.bukkit.inventory.Inventory getInventory() {
		return Inventory;
	}

}
