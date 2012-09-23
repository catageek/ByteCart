package com.github.catageek.ByteCart;

import com.github.catageek.ByteCart.SimpleCollisionAvoider.Side;

public class BC9000 extends AbstractBC9000 implements TriggeredIC {

	public BC9000(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC9000";
		this.FriendlyName = "Collision avoider";
	}

	@Override
	protected Side route() {
		return Side.RIGHT;
	}
	
	@Override
	protected void addIO() {
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
