package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.github.catageek.ByteCart.ByteCart;

public final class ComponentSign extends AbstractComponent {

	public ComponentSign(Block block) {
		super(block.getLocation());
	}

	public void setLine(int line, String s) {
		BlockState blockstate = this.getLocation().getBlock().getState();

		if (blockstate instanceof org.bukkit.block.Sign) {
			((org.bukkit.block.Sign) blockstate).setLine(line, s);
			blockstate.update();
		}
	}

	public String getLine(int line) {
		BlockState blockstate = this.getLocation().getBlock().getState();
		if (blockstate instanceof org.bukkit.block.Sign)
			return ((org.bukkit.block.Sign) blockstate).getLine(line);
		else {
			ByteCart.log.info("ByteCart: AddressSign cannot be built");
			throw new IllegalArgumentException();
		}

	}
}
