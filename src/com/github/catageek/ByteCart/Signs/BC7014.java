package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.IO.InputFactory;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Util.MathUtil;

public class BC7014 extends BC7010 implements TriggeredSign {

	public BC7014(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC7014";
		this.FriendlyName = "setStation";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;
		this.StorageCartAllowed = true;
	}

	@Override
	protected Address getAddressToWrite() {
		addIO();
		AddressRouted InvAddress = AddressFactory.getAddress(this.getInventory());
		
		RegistryInput wire = this.getInput(0);

		if (wire == null || wire.getAmount() == 0)
			return null;

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: "+ this.getName() + " wire : " + wire.getAmount());

		return AddressFactory.getAddress(format(wire, InvAddress));
	}

	protected String format(RegistryInput wire, AddressRouted InvAddress) {
		return ""+InvAddress.getRegion().getAmount()+"."
				+InvAddress.getTrack().getAmount()+"."
				+wire.getAmount();
	}
	
	protected void addIO() {
		// Input[0] : wire on left
		org.bukkit.block.Block block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal()));
		RegistryInput wire =  InputFactory.getInput(block);
		this.addInputRegistry(wire);
	}


}
