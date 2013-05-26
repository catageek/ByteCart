package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.material.RedstoneWire;

import com.github.catageek.ByteCart.HAL.RegistryInput;


public class ComponentWire extends AbstractComponent implements InputPin, RegistryInput {

	public ComponentWire(Block block) {
		super(block.getLocation());
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Redstone wire at " + block.getLocation().toString());
*/	}

	@Override
	public boolean read() {
		if (this.getLocation().getBlock().getState().getData() instanceof RedstoneWire) {
			if(((RedstoneWire) this.getLocation().getBlock().getState().getData()).isPowered()) {
/*				if(ByteCart.debug)
					ByteCart.log.info("Redstone wire on at (" + this.getLocation().toString() + ")");
*/				return true;
			}
/*			if(ByteCart.debug)
				ByteCart.log.info("Redstone wire off at (" + this.getLocation().toString() + ")");
*/		}
		return false;
	}

	@Override
	public boolean getBit(int index) {
		if (this.getLocation().getBlock().getState().getData() instanceof RedstoneWire) {
			RedstoneWire wire = ((RedstoneWire) this.getLocation().getBlock().getState().getData());
			return (wire.getData() & 1 << (length() - index)) != 0;
		}
		return false;
	}

	@Override
	public int getAmount() {
		if (this.getLocation().getBlock().getState().getData() instanceof RedstoneWire) {
			return ((RedstoneWire) this.getLocation().getBlock().getState().getData()).getData();
		}
		return 0;
	}

	@Override
	public int length() {
		return 4;
	}

	
}
