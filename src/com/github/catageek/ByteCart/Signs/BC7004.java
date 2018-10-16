/**
 * 
 */
package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressString;
import com.github.catageek.ByteCart.AddressLayer.TicketFactory;
import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.InputFactory;
import com.github.catageek.ByteCart.IO.InputPin;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A cart spawner
 */
final class BC7004 extends AbstractIC implements Powerable {

	private final String type;
	private final String address;

	public BC7004(org.bukkit.block.Block block, String type, String address) {
		super(block);
		this.type = type;
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Powerable#power()
	 */
	@Override
	public void power() {
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
			if ((rail.getBlockData() instanceof Rail) && MathUtil.getVehicleByLocation(loc) == null) {
				Entity entity = block.getWorld().spawnEntity(loc, getType());
				// put a ticket in the inventory if necessary
				if (entity instanceof InventoryHolder && AddressString.isResolvableAddressOrName(address)) {
					Inventory inv = ((InventoryHolder) entity).getInventory();
					TicketFactory.getOrCreateTicket(inv);
					Address dst = AddressFactory.getAddress(inv);
					dst.setAddress(address);
					dst.finalizeAddress();
				}
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
