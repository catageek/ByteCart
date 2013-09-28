/**
 * 
 */
package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.InputFactory;
import com.github.catageek.ByteCart.IO.InputPin;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A cart spawner
 */
final class BC7004 extends AbstractIC implements Powerable {

	private final String type;

	/**
	 * @param block
	 */
	public BC7004(org.bukkit.block.Block block, String type) {
		super(block);
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Powerable#power()
	 */
	@Override
	public void power() throws ClassNotFoundException, IOException {
		org.bukkit.block.Block block = this.getBlock();
		// check if we are really powered
		if (! block.getRelative(MathUtil.clockwise(getCardinal())).isBlockPowered() && ! block.getRelative(MathUtil.anticlockwise(getCardinal())).isBlockPowered()) {
			return;
		}

		// add input command = redstone

		InputPin[] wire = new InputPin[2];

		// Right
		wire[0] = InputFactory.getInput(block.getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal())));
		// left
		wire[1] = InputFactory.getInput(block.getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal())));

		// InputRegistry[0] = wire
		this.addInputRegistry(new PinRegistry<InputPin>(wire));

		// if wire is on, we spawn a cart
		if (this.getInput(0).getAmount() != 0) {
			org.bukkit.block.Block rail = block.getRelative(BlockFace.UP, 2);
			org.bukkit.Location loc = rail.getLocation();
			// check that it is a track, and no cart is there
			if (rail.getType().equals(Material.RAILS) && MathUtil.getVehicleByLocation(loc) == null) {
				block.getWorld().spawnEntity(loc, getType());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	public String getName() {
		return "BC7004";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "Cart spawner";
	}
	
	/**
	 * Get the type of cart to spawn
	 *
	 * @return the type
	 */
	private EntityType getType() {
		if(type.equalsIgnoreCase("storage")) {
			return EntityType.MINECART_CHEST;
		}
		if(type.equalsIgnoreCase("furnace")) {
			return EntityType.MINECART_FURNACE;
		}
		if(type.equalsIgnoreCase("hopper")) {
			return EntityType.MINECART_HOPPER;
		}
		
		return EntityType.MINECART;
	}
}
