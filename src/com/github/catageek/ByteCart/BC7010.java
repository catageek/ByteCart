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
		if (this.getState(this.getBlock()) != 0) {
			this.renew(getBlock(), 40, new ReleaseTask(this));
			return;
		}
		
		Address SignAddress = AddressFactory.getAddress(this.getBlock(), 3);

		AddressRouted IPaddress = AddressFactory.getAddress(this.getInventory());

		IPaddress.setRegion(SignAddress.getRegion().getAmount());
		IPaddress.setTrack(SignAddress.getTrack().getAmount());
		IPaddress.setStation(SignAddress.getStation().getAmount());
		IPaddress.setIsTrain(SignAddress.isTrain());

		if (this.getInventory().getHolder() instanceof Player) {
			if (! IPaddress.getAddress().equals(SignAddress.getAddress())) {
				((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );
			}
			else {
				((Player) this.getInventory().getHolder()).sendMessage(ChatColor.RED+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " (" + ChatColor.RED + IPaddress.getAddress() + ")");
				if (this.getVehicle() == null)
					((Player) this.getInventory().getHolder()).sendMessage(ChatColor.RED+"[Bytecart] " + ChatColor.GREEN + ByteCart.myPlugin.getConfig().getString("Info.SetAddress2") );
			}

		}
		else
			IPaddress.initializeTTL();
		
		// if this is the first car of a train
		// we save the state during 2 s
		if (SignAddress.isTrain()) {
			this.setState(this.getBlock(), 1);
			this.createReleaseTask(getBlock(), 40, new ReleaseTask(this));
		}


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

	private class ReleaseTask implements Runnable {

		AbstractIC bc;

		ReleaseTask(AbstractIC bc) {
			this.bc = bc;
		}

		@Override
		public void run() {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: BC7010 : running delayed thread (set busy line OFF)");

			// we get back to normal state

			bc.free(bc.getBlock());


		}


	}

}
