package com.github.catageek.ByteCart.Signs;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.TicketFactory;
import com.github.catageek.ByteCart.IO.ComponentSign;
import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;


/**
 * A ticket spawner for players
 */
public class BC7010 extends AbstractTriggeredSign implements Triggable, Clickable {

	protected boolean PlayerAllowed = true;
	protected boolean StorageCartAllowed = false;

	/**
	 * Constructor : !! vehicle can be null !!
	 */
	public BC7010(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	public BC7010(Block block, Player player) {
		super(block, null);
		this.setInventory(player.getInventory());
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Triggable#trigger()
	 */
	@Override
	public final void trigger() {

		if (! this.isHolderAllowed() || WandererContentFactory.isWanderer(getInventory()))
			return;

		// if this is a cart in a train
		if (this.wasTrain(this.getLocation())) {
			ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
			return;
		}

		Address address = getAddressToWrite();

		if (address == null)
			return;

		boolean isTrain = getIsTrain();

		this.setAddress(address.toString(), this.getNameToWrite(), isTrain);

		// if this is the first car of a train
		// we save the state during 2 s
		if (isTrain) {
			this.setWasTrain(this.getLocation(), true);
		}
	}

	/**
	 * Provide the train bit value to set
	 *
	 * @return the train bit value
	 */
	protected boolean getIsTrain() {
		return (new ComponentSign(this.getBlock())).getLine(0).equalsIgnoreCase("train");
	}

	/**
	 * Provide the address to set in ticket
	 *
	 * @return the address to write
	 */
	protected Address getAddressToWrite() {
		Address Address = AddressFactory.getAddress(this.getBlock(), 3);
		return Address;
	}

	/**
	 * Provide the name to display for the destination
	 *
	 * @return the name
	 */
	protected String getNameToWrite() {
		return (new ComponentSign(this.getBlock())).getLine(2);
	}

	/**
	 * Get the destination address of an existing ticket
	 *
	 * @return the destination address
	 */
	protected AddressRouted getTargetAddress() {
		return AddressFactory.getAddress(this.getInventory());
	}

	/**
	 * Spawn a ticket in inventory and set the destination address
	 * The train bit is not set.
	 *
	 * @param SignAddress the destination address
	 * @param name the destination name
	 * @return true if success, false otherwise
	 */
	public final boolean setAddress(String SignAddress, String name){
		return setAddress(SignAddress, name, false);
	}

	/**
	 * Spawn a ticket in inventory and set the destination address
	 *
	 * @param SignAddress the destination address
	 * @param name the destination name
	 * @param train true if it is a train head
	 * @return true if success, false otherwise
	 */
	public final boolean setAddress(String SignAddress, String name, boolean train){
		Player player = null;

		if (this.getInventory().getHolder() instanceof Player)
			player = (Player) this.getInventory().getHolder();

		if (player == null)
			TicketFactory.getOrCreateTicket(this.getInventory());
		else
			TicketFactory.getOrCreateTicket(player, forceTicketReuse());

		AddressRouted IPaddress = getTargetAddress();

		if (IPaddress == null || !IPaddress.setAddress(SignAddress)) {

			if (this.getInventory().getHolder() instanceof Player) {
				((Player) this.getInventory().getHolder()).sendMessage(ChatColor.GREEN+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );
			}
			return false;
		}
		if (this.getInventory().getHolder() instanceof Player) {
			this.infoPlayer(SignAddress);
		}
		IPaddress.initializeTTL();

		IPaddress.setTrain(train);

		IPaddress.finalizeAddress();
		return true;
	}

	/**
	 * Checks that the requestor is allowed to use this IC
	 *
	 * @return true if the requestor is allowed
	 */
	protected final boolean isHolderAllowed() {
		InventoryHolder holder = this.getInventory().getHolder();
		if (holder instanceof Player)
			return PlayerAllowed;
		return StorageCartAllowed;
	}

	/**
	 * Send message to player in the chat window
	 *
	 * @param signAddress the address got by the player
	 */
	protected void infoPlayer(String signAddress) {
		((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " " + ChatColor.RED + signAddress);
		if (this.getVehicle() == null  && ! ByteCart.myPlugin.getConfig().getBoolean("usebooks"))
			((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress2") );
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Clickable#click()
	 */
	@Override
	public final void click() {
		this.trigger();

	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	public String getName() {
		return "BC7010";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "Goto";
	}

	/**
	 * Tells if we must modify an existing ticket or create a new one
	 *
	 * @return true if modifying, false to create
	 */
	protected boolean forceTicketReuse() {
		return false;
	}
}
