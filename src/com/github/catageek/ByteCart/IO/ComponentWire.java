package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.material.RedstoneWire;

import com.github.catageek.ByteCart.HAL.RegistryInput;


public class ComponentWire extends AbstractComponent implements InputPin, RegistryInput {

	public ComponentWire(Block block) {
		super(block);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Redstone wire at " + block.getLocation().toString());
*/	}

	@Override
	public boolean read() {
			if(((RedstoneWire) this.getBlock().getState().getData()).isPowered()) {
/*				if(ByteCart.debug)
					ByteCart.log.info("Redstone wire on at (" + this.getLocation().toString() + ")");
*/				return true;
			}
/*			if(ByteCart.debug)
				ByteCart.log.info("Redstone wire off at (" + this.getLocation().toString() + ")");
*/
		return false;
	}

	@Override
	public boolean getBit(int index) {
			RedstoneWire wire = ((RedstoneWire) this.getBlock().getState().getData());
			return (wire.getData() & 1 << (length() - index)) != 0;
	}

	@Override
	public int getAmount() {
			return ((RedstoneWire) this.getBlock().getState().getData()).getData();
	}

	@Override
	public int length() {
		return 4;
	}

	
}
