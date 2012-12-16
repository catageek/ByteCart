package com.github.catageek.ByteCart;



public class BC7002 extends AbstractTriggeredIC implements TriggeredIC {

	public BC7002(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC7002";
		this.FriendlyName = "Cart detector";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

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

		ByteCart.myPlugin.getDelayedThreadManager().renew(getLocation(), 4, new Release(this));


	}

	private final class Release implements Runnable {

		private final AbstractIC bc;

		public Release(AbstractIC bc) {
			this.bc = bc;
		}

		@Override
		public void run() {
			this.bc.getOutput(0).setAmount(0);
		}

	}

}
