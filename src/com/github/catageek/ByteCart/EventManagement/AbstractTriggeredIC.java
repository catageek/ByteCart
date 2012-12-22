package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.ItemStack;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.AddressRouted;


public abstract class AbstractTriggeredIC extends AbstractIC {

	private final org.bukkit.entity.Vehicle Vehicle;
	private org.bukkit.inventory.Inventory Inventory;

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

		org.bukkit.inventory.Inventory newInv = Bukkit.createInventory(null, 27);


		// we load inventory of cart or player
		if (this.Vehicle != null) {

			if (this.getVehicle() instanceof StorageMinecart)
				return ((StorageMinecart) this.getVehicle()).getInventory();

			else if (this.getVehicle() instanceof Minecart) {
				if (! this.getVehicle().isEmpty()) {
					if (((Minecart) this.getVehicle()).getPassenger() instanceof Player) {

						if(ByteCart.debug)
							ByteCart.log.info("ByteCart: loading player inventory :" + ((Player) this.getVehicle().getPassenger()).getDisplayName());

						return ((Player) this.getVehicle().getPassenger()).getInventory();
					}
				}



			}

			/* There is no inventory, so we create one */

			// we have a default route ? so we write it in inventory
			if (ByteCart.myPlugin.getConfig().contains("EmptyCartsDefaultRoute"))
			{
				String DefaultRoute = ByteCart.myPlugin.getConfig().getString("EmptyCartsDefaultRoute");
				//construct address object
				AddressRouted myAddress = AddressFactory.getAddress(newInv);
				//fill empty inventory with stuff
				ItemStack stack = new ItemStack(1);
				stack.setAmount(stack.getMaxStackSize());
				newInv.addItem(stack);
				newInv.addItem(stack);
				newInv.addItem(stack);
				newInv.addItem(stack);
				//write address
				myAddress.setAddress(DefaultRoute);
				myAddress.initializeTTL();
			}

		}
		return newInv;
	}

	/**
	 * @return the inventory
	 */
	public org.bukkit.inventory.Inventory getInventory() {
		return Inventory;
	}

	protected void setInventory(org.bukkit.inventory.Inventory inv) {
		this.Inventory = inv;
	}

	public final boolean isTrain() {
		Address IPaddress = AddressFactory.getAddress(this.getInventory());
		return IPaddress.isTrain();
	}

	public final boolean wasTrain(Location loc) {
		boolean ret;
		if (ByteCart.myPlugin.getIsTrainManager().getMap().contains(loc)) {
			ret = ByteCart.myPlugin.getIsTrainManager().getMap().get(loc);
/*			if(ByteCart.debug  && ret)
				ByteCart.log.info("ByteCart: "+ this.getName() + " at " + this.getLocation() + " : " + this.getVehicle() + " is wagon !");
*/			return ret;
		}
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: "+ this.getName() + " at " + this.getLocation() + " : " + this.getVehicle() + " is not wagon !");
*/		return false;
	}
	
	protected final void setWasTrain(Location loc, boolean b) {
		if (b)
			ByteCart.myPlugin.getIsTrainManager().getMap().put(loc, true);
		
	}

}
