package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public class BC9001 extends BC9016 implements TriggeredIC, PoweredIC {

	final static private BlockMap wavecount = new BlockMap();

	public BC9001(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 4;
		this.Name = "BC9001";
		this.FriendlyName = "Station";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = "bytecart." + this.Name;
	}


	@Override
	public void trigger() {
		super.trigger();

		// adding lever as output 1
		this.AddOutputIO();

		// We treat the counter
		if (!this.hasReleaseTask(getBlock().getRelative(BlockFace.UP)))
			this.createReleaseTask(getBlock().getRelative(BlockFace.UP), 4, new UpdateCount(this, this.getOutput(1)));
		else
			this.renew(getBlock().getRelative(BlockFace.UP), 4, new UpdateCount(this, this.getOutput(1)));
	}
	
	@Override
	public void power() {

		if (! this.getBlock().getRelative(MathUtil.clockwise(getCardinal())).isBlockPowered() && ! this.getBlock().getRelative(MathUtil.anticlockwise(getCardinal())).isBlockPowered()) {
			return;
		}

		// add input command = redstone

		InputPin[] wire = new InputPin[2];

		// Right
		wire[0] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal())));
		// left
		wire[1] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal())));

		// InputRegistry[6] = detector
		this.addInputRegistry(new PinRegistry<InputPin>(wire));

		// Adding lever as output 0
		this.AddOutputIO();

		// if detector is on, the signal is red (= off)
		if (this.getInput(0).getAmount() != 0) {

			// setting red signal
			this.getOutput(0).setAmount(0);

			this.incrementWaveCount();

		}

		if (this.hasReleaseTask(getBlock().getRelative(BlockFace.DOWN)))
			this.renew(getBlock().getRelative(BlockFace.DOWN), 400, new RemoveCount(this, this.getOutput(0)));
		else			
			this.createReleaseTask(getBlock().getRelative(BlockFace.DOWN), 400, new RemoveCount(this, this.getOutput(0)));
	}

	final private void incrementWaveCount() {
		if (!wavecount.hasEntry(this.getBlock()))
			wavecount.createEntry(getBlock(), 1);
		else
			wavecount.updateValue(getBlock(), wavecount.getValue(getBlock()) + 1);
		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart." + getName() + ": ++count = " + wavecount.getValue(getBlock()));

	}

	final private boolean decrementWaveCount() {
		if (BC9001.wavecount.hasEntry(getBlock()) && wavecount.getValue(getBlock()) > 1)
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

	final private void deleteWaveCount() {
		wavecount.deleteEntry(getBlock());
	}

	private final void AddOutputIO() {
		// Declare red light signal = lever

		OutputPin[] lever = new OutputPin[1];

		// Right
		lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal(), 2));

		// OutputRegistry[1] = red light signal
		this.addOutputRegistry(new PinRegistry<OutputPin>(lever));
	}

	final private class UpdateCount implements Runnable {

		BC9001 bc;
		RegistryOutput output;

		UpdateCount(BC9001 bc, RegistryOutput registryOutput) {
			this.bc = bc;
			output = registryOutput;
		}

		@Override
		public void run() {
			// decrement count. if count reaches 0, signal is green ( = on)
			try {

				this.bc.freeThread(bc.getBlock().getRelative(BlockFace.UP));

				if (!this.bc.decrementWaveCount()) {
					
					this.bc.renew(getBlock().getRelative(BlockFace.DOWN), 80, new RemoveCount(bc, output));
				}
			}
			catch (Exception e) {
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : "+ e.toString());

				e.printStackTrace();
			}
		}

	}

	final private class RemoveCount implements Runnable {

		BC9001 bc;
		RegistryOutput output;

		RemoveCount(BC9001 bc, RegistryOutput o) {
			this.bc = bc;
			this.output = o;
		}

		@Override
		public void run() {
			// suppress counter
			try {
				this.bc.deleteWaveCount();
				this.bc.freeThread(getBlock().getRelative(BlockFace.DOWN));
				this.output.setAmount(1);
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : " + bc.getName() + ": Lever on");

			}
			catch (NullPointerException e) {
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : "+ e.toString());

				e.printStackTrace();
			}
		}

	}

}
