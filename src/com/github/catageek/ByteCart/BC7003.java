package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;


public final class BC7003 extends AbstractIC implements TriggeredIC, PoweredIC {

	final static private BlockMap<Integer> wavecount = new BlockMap<Integer>();

	public BC7003(org.bukkit.block.Block block) {
		super(block);
		this.Name = "BC7003";
		this.FriendlyName = "Cart counter";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}

	public BC7003(org.bukkit.block.Block block, RegistryOutput io) {
		this(block);
		// forcing output[0] to be the one in parameter
		this.addOutputRegistry(io);
	}


	@Override
	public void trigger() {

		// adding lever as output 0 (if not forced in constructor)
		this.AddOutputIO();

		// We treat the counter
		try {


			if (!this.decrementWaveCount()) {

				ByteCart.myPlugin.getDelayedThreadManager().renew(getBlock().getRelative(BlockFace.DOWN), ByteCart.myPlugin.Lockduration + 6, new RemoveCount(this));
			}
		}
		catch (Exception e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : "+ e.toString());

			e.printStackTrace();
		}
	}

	@Override
	public void power() {
		// check if we are really powered
		if (! this.getBlock().getRelative(MathUtil.clockwise(getCardinal())).isBlockPowered() && ! this.getBlock().getRelative(MathUtil.anticlockwise(getCardinal())).isBlockPowered()) {
			return;
		}

		// add input command = redstone

		InputPin[] wire = new InputPin[2];

		// Right
		wire[0] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal())));
		// left
		wire[1] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal())));

		// InputRegistry[0] = detector
		this.addInputRegistry(new PinRegistry<InputPin>(wire));

		// Adding lever as output 0
		this.AddOutputIO();

		// if detector is on, the signal is red (= on)
		if (this.getInput(0).getAmount() != 0) {

			// setting red signal
			this.getOutput(0).setAmount(1);

			this.incrementWaveCount();

		}

		ByteCart.myPlugin.getDelayedThreadManager().renew(getBlock().getRelative(BlockFace.DOWN), 400, new RemoveCount(this));
	}

	final private void incrementWaveCount() {
		synchronized(wavecount) {
			if (!wavecount.hasEntry(this.getBlock())) {
				wavecount.createEntry(getBlock(), 1);
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart." + getName() + ": count = " + wavecount.getValue(getBlock()) + " init");
			}
			else {
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart." + getName() + ": ++count = " + wavecount.getValue(getBlock()) + " before");
				wavecount.updateValue(getBlock(), wavecount.getValue(getBlock()) + 1);
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart." + getName() + ": ++count = " + wavecount.getValue(getBlock()) + " after");
			}
		}


	}

	final private boolean decrementWaveCount() {
		synchronized(wavecount) {
			if (wavecount.hasEntry(getBlock()) && wavecount.getValue(getBlock()) > 1)
				wavecount.updateValue(getBlock(), wavecount.getValue(getBlock()) - 1);

			else {
				wavecount.deleteEntry(getBlock());
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart." + getName() + ": --count = 0");
				return false;
			}
		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart." + getName() + ": --count = " + wavecount.getValue(getBlock()));

		return true;
		}
	}

	final private void deleteWaveCount() {
		wavecount.deleteEntry(getBlock());
	}

	private final void AddOutputIO() {
		// Declare red light signal = lever

		OutputPin[] lever = new OutputPin[1];

		// Right
		lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal(), 2));

		// OutputRegistry = red light signal
		this.addOutputRegistry(new PinRegistry<OutputPin>(lever));
	}


	final private class RemoveCount implements Runnable {

		BC7003 bc;
		RegistryOutput output;

		RemoveCount(BC7003 bc) {
			this.bc = bc;
			this.output = bc.getOutput(0);
		}

		@Override
		public void run() {
			// suppress counter
			try {
				this.bc.deleteWaveCount();
				this.output.setAmount(0);

			}
			catch (NullPointerException e) {
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : "+ e.toString());

				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean isTrain() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wasTrain(org.bukkit.block.Block block) {
		// TODO Auto-generated method stub
		return false;
	}

}


