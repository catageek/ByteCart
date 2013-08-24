package com.github.catageek.ByteCart.Signs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.ReturnAddressFactory;

/**
 * A block that makes the cart return to its origin using return address
 */
public final class BC7017 extends AbstractTriggeredSign implements Triggable {

	public BC7017(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	public BC7017(org.bukkit.block.Block block, Player player) {
		super(block, null);
		this.setInventory(player.getInventory());
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	public String getName() {
		return "BC7017";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "Return back";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Triggable#trigger()
	 */
	@Override
	public void trigger() {
		Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());

		if (returnAddress == null || ! returnAddress.isReturnable())
			return;

		String returnAddressString = returnAddress.toString();
		AddressRouted targetAddress = AddressFactory.getAddress(getInventory());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 7017 : Writing address " + returnAddressString);
		returnAddress.remove();
		returnAddress.finalizeAddress();
		boolean isTrain = targetAddress.isTrain();
		targetAddress.setAddress(returnAddressString);
		targetAddress.setTrain(isTrain);
		if (this.getInventory().getHolder() instanceof Player)
			((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " (" + ChatColor.RED + returnAddressString + ")");
		targetAddress.initializeTTL();
		targetAddress.finalizeAddress();

	}
}
