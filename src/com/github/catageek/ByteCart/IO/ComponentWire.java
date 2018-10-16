package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;

import com.github.catageek.ByteCartAPI.HAL.RegistryInput;


/**
 * A Redstone wire
 */
class ComponentWire extends AbstractComponent implements InputPin, RegistryInput {

	/**
	 * @param block the block containing the wire
	 */
	ComponentWire(Block block) {
		super(block);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Redstone wire at " + block.getLocation().toString());
*/	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.IO.InputPin#read()
	 */
	@Override
	public boolean read() {
		if(((AnaloguePowerable) this.getBlock().getBlockData()).getPower() != 0) {
//			if(ByteCart.debug)
//				ByteCart.log.info("Redstone wire on at (" + this.getBlock().getLocation().toString() + ")");
			return true;
		}
//		if(ByteCart.debug)
//			ByteCart.log.info("Redstone wire off at (" + this.getBlock().getLocation().toString() + ")");

		return false;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryInput#getBit(int)
	 */
	@Override
	public boolean getBit(int index) {
		final AnaloguePowerable wire = ((AnaloguePowerable) this.getBlock().getBlockData());
		return (wire.getPower() & 1 << (length() - index)) != 0;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#getAmount()
	 */
	@Override
	public int getAmount() {
		return ((AnaloguePowerable) this.getBlock().getBlockData()).getPower();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#length()
	 */
	@Override
	public int length() {
		return 4;
	}

	
}
