package com.github.catageek.ByteCart.Signs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.ReturnAddressFactory;

public final class BC7017 extends AbstractTriggeredSign implements Triggable {

	public BC7017(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	@Override
	public String getName() {
		return "BC7017";
	}

	@Override
	public String getFriendlyName() {
		return "Return back";
	}

	@Override
	public void trigger() {
		Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 7017 : Reading address " + returnAddress.toString());

		if (! returnAddress.isReturnable())
			return;

		String returnAddressString = returnAddress.toString();
		AddressRouted targetAddress = AddressFactory.getAddress(getInventory());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: 7017 : Writing address " + returnAddressString);
		returnAddress.remove();
		targetAddress.setAddress(returnAddressString);
		if (this.getInventory().getHolder() instanceof Player)
			((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " (" + ChatColor.RED + returnAddressString + ")");
		else
			targetAddress.initializeTTL();

	}
}
