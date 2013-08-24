package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;

import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.Util.MathUtil;

/**
 * A lever
 */
public class ComponentLever extends AbstractComponent implements OutputPin, InputPin, RegistryInput {

	/**
	 * @param block the block containing the component
	 */
	public ComponentLever(Block block) {
		super(block);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.IO.OutputPin#write(boolean)
	 */
	@Override
	public void write(boolean bit) {
		BlockState block = this.getBlock().getState();
		Lever lever = (Lever) block.getData();
		if(lever.isPowered()^bit) {
			lever.setPowered(bit);
			block.setData(lever);
			block.update(false, true);
			MathUtil.forceUpdate(this.getBlock().getRelative(lever.getAttachedFace()));
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.IO.InputPin#read()
	 */
	@Override
	public boolean read() {
		MaterialData md = this.getBlock().getState().getData();
		if(md instanceof Lever) {
			return ((Lever) md).isPowered();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryInput#getBit(int)
	 */
	@Override
	public boolean getBit(int index) {
		return read();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#getAmount()
	 */
	@Override
	public int getAmount() {
		return (read() ? 15 : 0);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#length()
	 */
	@Override
	public int length() {
		return 4;
	}


}
