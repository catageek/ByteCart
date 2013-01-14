package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.UpdaterLocal;
import com.github.catageek.ByteCart.Util.MathUtil;


public class BC9000 extends AbstractBC9000 implements TriggeredIC {

	public BC9000(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super("BC9000", block, vehicle);

		this.FriendlyName = "Collision avoider";
	}

	@Override
	protected SimpleCollisionAvoider.Side route() {
		return SimpleCollisionAvoider.Side.RIGHT;
	}
	
	@Override
	protected void manageUpdater(SimpleCollisionAvoider intersection) {
		// it's an updater, so let it choosing direction
		UpdaterLocal updater = new UpdaterLocal(getVehicle(),
				AddressFactory.getAddress(this.getInventory()), null, 0, getLevel());

		// routing
		intersection.WishToGo(route(), false);

		// here we perform routes update
		updater.leaveSubnet();
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
