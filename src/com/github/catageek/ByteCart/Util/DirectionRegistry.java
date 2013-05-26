package com.github.catageek.ByteCart.Util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.HAL.Registry;
import com.github.catageek.ByteCart.HAL.RegistryOutput;
import com.github.catageek.ByteCart.HAL.VirtualRegistry;
import com.github.catageek.ByteCart.Storage.Partitionable;

public final class DirectionRegistry implements Partitionable {
	/**
	 * 
	 */
	private VirtualRegistry registry;

	/* 
	 * bit 3 : SOUTH
	 * bit 2 : WEST
	 * bit 1 : NORTH
	 * bit 0 : EASt
	 */

	public DirectionRegistry() {
		this.registry = new VirtualRegistry(4);
	}
	public DirectionRegistry(int value) {
		this();
		this.registry.setAmount(value);
	}

	public DirectionRegistry(BlockFace b) {
		this();
		this.setCardinal(b, true);
	}

	public final void setCardinal(BlockFace face, boolean value) {
		
		switch(face) {
		case EAST:
			this.registry.setBit(0, value);
			break;
		case NORTH:
			this.registry.setBit(1, value);
			break;
		case WEST:
			this.registry.setBit(2, value);
			break;
		case SOUTH:
			this.registry.setBit(3, value);
		default:
			break;
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

	public final void setAmount(int value) {
		this.registry.setAmount(value);
	}
	
	public final int getAmount() {
		return this.registry.getAmount();
	}
	
	public final Registry getRegistry() {
		RegistryOutput reg = new VirtualRegistry(4);
		reg.setAmount(this.getAmount());
		return reg;
	}

	@Override
	public int hashCode() {
		return getAmount();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof DirectionRegistry))
			return false;
		
		DirectionRegistry rhs = (DirectionRegistry) o;
		
		return new EqualsBuilder().append(getAmount(),rhs.getAmount()).isEquals();
	}
}
