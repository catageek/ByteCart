package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.material.RedstoneWire;

public class ComponentWire extends AbstractComponent implements InputPin {

	protected ComponentWire(Block block) {
		super(block.getLocation());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Redstone wire at " + block.getLocation().toString());
	}

	@Override
	public boolean read() {
		if (this.getLocation().getBlock().getState().getData() instanceof RedstoneWire) {
			if(((RedstoneWire) this.getLocation().getBlock().getState().getData()).isPowered()) {
				if(ByteCart.debug)
					ByteCart.log.info("Redstone wire on at (" + this.getLocation().toString() + ")");
				return true;
			}
			if(ByteCart.debug)
				ByteCart.log.info("Redstone wire off at (" + this.getLocation().toString() + ")");
		}
		return false;
	}

	
}
