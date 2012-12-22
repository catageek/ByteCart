package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.InventoryHolder;


public class BC7010 extends AbstractTriggeredIC implements TriggeredIC, ClickedIC {

	protected boolean PlayerAllowed = true;
	protected boolean StorageCartAllowed = false;

	// Constructor : !! vehicle can be null !!

	public BC7010(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC7010";
		this.FriendlyName = "Goto";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;
	}

	public BC7010(Block block, Player player) {
		super(block, null);
		this.setInventory(player.getInventory());
	}

	@Override
	public void trigger() {

		if (! this.isHolderAllowed())
			return;

		// if this is a cart in a train
		if (this.wasTrain(this.getLocation())) {
			ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
			return;
		}

		Address SignAddress = AddressFactory.getAddress(this.getBlock(), 3);

		this.setAddress(SignAddress);

		// if this is the first car of a train
		// we save the state during 2 s
		if (SignAddress.isTrain()) {
			this.setWasTrain(this.getLocation(), true);
		}


	}

	final protected boolean setAddress(Address SignAddress){
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

	final protected boolean isHolderAllowed() {
		InventoryHolder holder = this.getInventory().getHolder();
		if (holder instanceof Player)
			return PlayerAllowed;
		if (holder instanceof StorageMinecart) {
			return StorageCartAllowed;
		}
		return false;
	}

	@Override
	public void click() {
		this.trigger();

	}
}
