package com.github.catageek.ByteCart.Signs;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.TicketFactory;
import com.github.catageek.ByteCart.IO.ComponentSign;


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
		
		boolean isTrain = (new ComponentSign(this.getBlock())).getLine(0).equalsIgnoreCase("train");

		this.setAddress(address, this.getNameToWrite(), isTrain);

		// if this is the first car of a train
		// we save the state during 2 s
		if (isTrain) {
			this.setWasTrain(this.getLocation(), true);
		}
	}

	protected Address getAddressToWrite() {
		Address Address = AddressFactory.getAddress(this.getBlock(), 3);
		return Address;
	}

	protected String getNameToWrite() {
		return (new ComponentSign(this.getBlock())).getLine(2);
	}

	protected AddressRouted getTargetAddress() {
		return AddressFactory.getAddress(this.getInventory());
	}

	public final boolean setAddress(Address SignAddress, String name){
		return setAddress(SignAddress, name, false);
	}

	public final boolean setAddress(Address SignAddress, String name, boolean train){
		Player player = null;

		if (this.getInventory().getHolder() instanceof Player)
			player = (Player) this.getInventory().getHolder();

		if (player == null)
			TicketFactory.getOrCreateTicket(this.getInventory());
		else
			TicketFactory.getOrCreateTicket(player, forceTicketReuse());

		AddressRouted IPaddress = getTargetAddress();

		if (IPaddress == null || !IPaddress.setAddress(SignAddress, name)) {

			if (this.getInventory().getHolder() instanceof Player) {
				((Player) this.getInventory().getHolder()).sendMessage(ChatColor.GREEN+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );
			}
			return false;
		}
		if (this.getInventory().getHolder() instanceof Player) {
			this.infoPlayer(IPaddress);
		} else
			IPaddress.initializeTTL();
		
		IPaddress.setTrain(train);
		
		IPaddress.finalizeAddress();
		return true;
	}

	protected final boolean isHolderAllowed() {
		InventoryHolder holder = this.getInventory().getHolder();
		if (holder instanceof Player)
			return PlayerAllowed;
		return StorageCartAllowed;
	}

	protected void infoPlayer(Address address) {
		((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " (" + ChatColor.RED + address + ")");
		if (this.getVehicle() == null  && ! ByteCart.myPlugin.getConfig().getBoolean("usebooks"))
			((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress2") );
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

	protected boolean forceTicketReuse() {
		return false;
	}
}
