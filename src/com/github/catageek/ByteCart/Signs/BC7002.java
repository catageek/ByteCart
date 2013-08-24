package com.github.catageek.ByteCart.Signs;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;



/**
 * A cart detector
 */
final class BC7002 extends AbstractTriggeredSign implements Triggable {

	BC7002(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Triggable#trigger()
	 */
	@Override
	public void trigger() {
		OutputPin[] lever = new OutputPin[1];

		// Right
		lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal()));

		// OutputRegistry[1] = red light signal
		this.addOutputRegistry(new PinRegistry<OutputPin>(lever));

		this.getOutput(0).setAmount(1);
		//		if(ByteCart.debug)
		//			ByteCart.log.info("ByteCart : BC7002 count 1");

//		ByteCart.myPlugin.getDelayedThreadManager().renew(getLocation(), 4, new Release(this));
		(new Release(this)).runTaskLater(ByteCart.myPlugin, 4);

	}

	private final class Release extends BukkitRunnable {

		private final AbstractIC bc;

		public Release(AbstractIC bc) {
			this.bc = bc;
		}

		@Override
		public void run() {
			this.bc.getOutput(0).setAmount(0);
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	public final String getName() {
		return "BC7002";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "Cart detector";
	}
}