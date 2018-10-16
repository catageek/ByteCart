package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.ComponentSign;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCartAPI.HAL.RegistryOutput;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

final class BC7009 extends AbstractTriggeredSign implements Triggable {

	private final BlockFace From;

	BC7009(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		From = getCardinal().getOppositeFace();
	}

	@Override
	public void trigger() {
		this.addIO();
		RoundRobin();
	}

	private void RoundRobin() {
		final ComponentSign sign = new ComponentSign(this.getBlock());
		final String line = sign.getLine(3);
		DirectionRegistry dir;
		try {
			int current = Integer.parseInt(line);
			dir = new DirectionRegistry(current);
		}
		catch (NumberFormatException e) {
			dir = new DirectionRegistry(From);
		}
		BlockFace newdir = MathUtil.clockwise(dir.getBlockFace());
		if (newdir.equals(From))
			newdir = MathUtil.clockwise(newdir);
		final int amount = new DirectionRegistry(newdir).getAmount();
		this.getOutput(0).setAmount(amount);
		sign.setLine(3, "" + amount);
	}

	@Override
	public String getName() {
		return "BC7009";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "Load Balancer";
	}

	/**
	 * Registers levers as output
	 *
	 * @param from the origin axis
	 * @param center the center of the router
	 */
	private final void addIO() {

		// Center of the device, at sign level
		org.bukkit.block.Block center = this.getBlock().getRelative(this.getCardinal(), 2).getRelative(MathUtil.clockwise(this.getCardinal()));

		// Main output
		OutputPin[] sortie = new OutputPin[4];
		// East
		sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST,3).getRelative(BlockFace.SOUTH));
		// North
		sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST,3).getRelative(BlockFace.NORTH));
		// South
		sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH,3).getRelative(BlockFace.EAST));
		// West
		sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH,3).getRelative(BlockFace.WEST));

		RegistryOutput main = new PinRegistry<OutputPin>(sortie);

		// output[0] is main levers
		this.addOutputRegistry(main);
	}

}
