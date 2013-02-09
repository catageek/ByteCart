package com.github.catageek.ByteCart.Signs;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.AddressRouted;


public class BC7010 extends AbstractTriggeredSign implements Triggable, Clickable {

	protected boolean PlayerAllowed = true;
	protected boolean StorageCartAllowed = false;

	// Constructor : !! vehicle can be null !!

	public BC7010(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	public BC7010(Block block, Player player) {
		super(block, null);
		this.setInventory(player.getInventory());
	}

	@Override
	public final void trigger() {

		if (! this.isHolderAllowed())
			return;

		// if this is a cart in a train
		if (this.wasTrain(this.getLocation())) {
			ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
			return;
		}

		Address address = getAddressToWrite();
		
		if (address == null)
			return;
		
		this.setAddress(address);

		// if this is the first car of a train
		// we save the state during 2 s
		if (address.isTrain()) {
			this.setWasTrain(this.getLocation(), true);
		}


	}

	protected Address getAddressToWrite() {
		Address Address = AddressFactory.getAddress(this.getBlock(), 3);
		return Address;
	}

	public final boolean setAddress(Address SignAddress){
		AddressRouted IPaddress = AddressFactory.getAddress(this.getInventory());

		if (!IPaddress.setAddress(SignAddress)) {

			if (this.getInventory().getHolder() instanceof Player) {
					((Player) this.getInventory().getHolder()).sendMessage(ChatColor.GREEN+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );
			}
			return false;
		}
		if (this.getInventory().getHolder() instanceof Player) {
			((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " (" + ChatColor.RED + IPaddress + ")");
			if (this.getVehicle() == null)
				((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress2") );
		} else
			IPaddress.initializeTTL();
		return true;
	}

	protected final boolean isHolderAllowed() {
		InventoryHolder holder = this.getInventory().getHolder();
		if (holder instanceof Player)
			return PlayerAllowed;
		if (holder instanceof StorageMinecart) {
			return StorageCartAllowed;
		}
		return false;
	}

	@Override
	public final void click() {
		this.trigger();

	}

	@Override
	public String getName() {
		return "BC7010";
	}

	@Override
	public String getFriendlyName() {
		return "Goto";
	}
}
