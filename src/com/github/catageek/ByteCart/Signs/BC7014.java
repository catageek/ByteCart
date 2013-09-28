package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.IO.InputFactory;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.HAL.RegistryInput;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A station field setter using a redstone signal strength
 */
class BC7014 extends BC7010 implements Triggable {

	BC7014(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.StorageCartAllowed = true;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7010#getAddressToWrite()
	 */
	@Override
	protected Address getAddressToWrite() {
		addIO();
		AddressRouted InvAddress = AddressFactory.getAddress(this.getInventory());
		
		if (InvAddress == null)
			return null;
		
		RegistryInput wire = this.getInput(0);

		if (wire == null || wire.getAmount() == 0)
			return null;

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: "+ this.getName() + " wire : " + wire.getAmount());

		return AddressFactory.getAddress(format(wire, InvAddress));
	}

	/**
	 * Build the address string
	 *
	 * @param wire the wire to take as input
	 * @param InvAddress the address to modify
	 * @return a string containing the address
	 */
	protected String format(RegistryInput wire, AddressRouted InvAddress) {
		return ""+InvAddress.getRegion().getAmount()+"."
				+InvAddress.getTrack().getAmount()+"."
				+wire.getAmount();
	}
	
	/**
	 * Register the input wire on the left of the sign
	 *
	 */
	protected void addIO() {
		// Input[0] : wire on left
		org.bukkit.block.Block block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal()));
		RegistryInput wire =  InputFactory.getInput(block);
		this.addInputRegistry(wire);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7010#getIsTrain()
	 */
	@Override
	protected final boolean getIsTrain() {
		boolean signtrain = super.getIsTrain();
		Address address;
		if((address = AddressFactory.getAddress(this.getInventory())) != null)
			return address.isTrain() || signtrain;
		return signtrain;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7010#getName()
	 */
	@Override
	public String getName() {
		return "BC7014";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7010#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "setStation";
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7010#forceTicketReuse()
	 */
	@Override
	protected boolean forceTicketReuse() {
		return true;
	}
}
