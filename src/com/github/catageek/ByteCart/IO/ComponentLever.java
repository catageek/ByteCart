package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;

import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.Util.MathUtil;

public class ComponentLever extends AbstractComponent implements OutputPin, InputPin, RegistryInput {

	public ComponentLever(Block block) {
		super(block.getLocation());
	}

	@Override
	public void write(boolean bit) {
		BlockState block = this.getLocation().getBlock().getState();
		if(block.getData() instanceof Lever) {
			Lever lever = (Lever) block.getData();
			if(lever.isPowered()^bit) {
				lever.setPowered(bit);
				block.setData(lever);
				block.update(false, true);
				MathUtil.forceUpdate(this.getLocation().getBlock().getRelative(lever.getAttachedFace()));
			}
		}
	}

	@Override
	public boolean read() {
		MaterialData md = this.getLocation().getBlock().getState().getData();
		if(md instanceof Lever) {
			return ((Lever) md).isPowered();
		}
		return false;
	}

	@Override
	public boolean getBit(int index) {
		return read();
	}

	@Override
	public int getAmount() {
		return (read() ? 15 : 0);
	}

	@Override
	public int length() {
		return 4;
	}


}
