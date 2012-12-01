package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public final class DirectionRegistry {
	private VirtualRegistry registry;

	/* 
	 * bit 3 : SOUTH
	 * bit 2 : WEST
	 * bit 1 : NORTH
	 * bit 0 : EASt
	 */

	public DirectionRegistry(int value) {
		this.registry = new VirtualRegistry(4);
		this.registry.setAmount(value);
	}

	public DirectionRegistry(BlockFace b) {
		this.registry = new VirtualRegistry(4);
		this.setCardinal(b, true);
	}

	public final void setCardinal(BlockFace face, boolean value) {
		
		// here we correct the bukkit API error on BlockFace values
		switch(face) {
		case SOUTH:
			this.registry.setBit(0, value);
			break;
		case EAST:
			this.registry.setBit(1, value);
			break;
		case NORTH:
			this.registry.setBit(2, value);
			break;
		case WEST:
			this.registry.setBit(3, value);
		}

	}
	
	public final BlockFace ToString() {
		switch(this.getAmount()) {
		case 1:
			return BlockFace.SOUTH;
		case 2:
			return BlockFace.WEST;
		case 4:
			return BlockFace.NORTH;
		case 8:
			return BlockFace.EAST;
		}
		return BlockFace.SELF;
	}

	public final BlockFace getBlockFace() {
		switch(this.getAmount()) {
		case 1:
			return BlockFace.WEST;
		case 2:
			return BlockFace.NORTH;
		case 4:
			return BlockFace.EAST;
		case 8:
			return BlockFace.SOUTH;
		}
		return BlockFace.SELF;
	}

	public final void setAmount(int value) {
		this.registry.setAmount(value);
	}
	
	public final Integer getAmount() {
		return this.registry.getAmount();
	}
	
	public final Registry getRegistry() {
		Registry reg = new VirtualRegistry(4);
		reg.setAmount(this.getAmount());
		return reg;
	}

}
