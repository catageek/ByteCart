package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.ReturnAddressFactory;
import com.github.catageek.ByteCart.Util.MathUtil;

public final class BC7016 extends AbstractTriggeredSign implements Triggable {

	public BC7016(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	@Override
	public String getName() {
		return "BC7016";
	}

	@Override
	public String getFriendlyName() {
		return "Is returnable ?";
	}

	@Override
	public void trigger() {
		addIO();
		Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());
		
		if (returnAddress.isReturnable())
			this.getOutput(0).setAmount(3);
		else
			this.getOutput(0).setAmount(0);
	}

	private void addIO() {
		// Output[0] = 2 bits registry representing levers on the left and on the right of the sign
		OutputPin[] lever2 = new OutputPin[2];

		// Left
		lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
		// Right
		lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

		PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

		this.addOutputRegistry(command1);
	}

}
