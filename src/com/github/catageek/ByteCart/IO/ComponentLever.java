package com.github.catageek.ByteCart.IO;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Lever;

import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.Util.MathUtil;

public class ComponentLever extends AbstractComponent implements OutputPin, InputPin, RegistryInput {

	public ComponentLever(Block block) {
		super(block.getLocation());
	}

	@Override
	public void write(boolean bit) {
		if(this.getLocation().getBlock().getState().getData() instanceof Lever) {
			Lever lever = new Lever(Material.LEVER, this.getLocation().getBlock().getData());
			if(lever.isPowered()^bit) {
				lever.setPowered(bit);
				this.getLocation().getBlock().setData(lever.getData(), true);
			}
			/*			
			if(ByteCart.debug)
				ByteCart.log.info("Lever at (" + this.getLocation().toString() + ") : " + bit);
			 */

			MathUtil.forceUpdate(this.getLocation().getBlock().getRelative(lever.getAttachedFace()));
		}
	}

	@Override
	public boolean read() {
		if(this.getLocation().getBlock().getState().getData() instanceof Lever) {
			final Lever lever = new Lever(Material.LEVER, this.getLocation().getBlock().getData());
			return lever.isPowered();

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
