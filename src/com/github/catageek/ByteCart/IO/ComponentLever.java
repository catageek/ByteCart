package com.github.catageek.ByteCart.IO;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.Switch.Face;

import com.github.catageek.ByteCartAPI.HAL.RegistryInput;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A lever
 */
class ComponentLever extends AbstractComponent implements OutputPin, InputPin, RegistryInput {

	/**
	 * @param block the block containing the component
	 */
	ComponentLever(Block block) {
		super(block);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.IO.OutputPin#write(boolean)
	 */
	@Override
	public void write(boolean bit) {
		final Block block = this.getBlock();
		final BlockData md = block.getBlockData();
		if(md instanceof Switch) {
			Switch pw = (Switch) md;
			pw.setPowered(bit);
			block.setBlockData(pw);
			Face face = pw.getFace();
			switch (face) {
				case CEILING:
					MathUtil.forceUpdate(block.getRelative(BlockFace.UP,2));
					break;
				case WALL:
					MathUtil.forceUpdate(block.getRelative(pw.getFacing().getOppositeFace()));
					break;
				default:
					MathUtil.forceUpdate(block.getRelative(BlockFace.DOWN));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.IO.InputPin#read()
	 */
	@Override
	public boolean read() {
		BlockData md = this.getBlock().getBlockData();
		if(md instanceof Powerable) {
			return ((Powerable) md).isPowered();
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
