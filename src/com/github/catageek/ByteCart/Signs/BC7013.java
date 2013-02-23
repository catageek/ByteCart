package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.HAL.SuperRegistry;
import com.github.catageek.ByteCart.IO.InputFactory;
import com.github.catageek.ByteCart.IO.InputPin;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Util.MathUtil;

public class BC7013 extends BC7014 implements Triggable {

	public BC7013(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	@Override
	protected String format(RegistryInput wire, AddressRouted InvAddress) {
		return ""+InvAddress.getRegion().getAmount()+"."
				+wire.getAmount()+"."
				+InvAddress.getStation().getAmount();
	}
	
	@Override
	protected void addIO() {
		// Input[0] : wire on left
		org.bukkit.block.Block block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal()));
		RegistryInput wire =  InputFactory.getInput(block);

		InputPin[] levers = new InputPin[2];
		block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal()));
		levers[0] =  InputFactory.getInput(block);
		
		block = this.getBlock().getRelative(getCardinal().getOppositeFace());
		levers[1] =  InputFactory.getInput(block);

		RegistryInput ret = new SuperRegistry<RegistryInput>(new PinRegistry<InputPin>(levers), wire);

		this.addInputRegistry(ret);

	}

	@Override
	public String getName() {
		return "BC7013";
	}

	@Override
	public String getFriendlyName() {
		return "setTrack";
	}
	
	@Override
	protected boolean forceTicketReuse() {
		return true;
	}
}
