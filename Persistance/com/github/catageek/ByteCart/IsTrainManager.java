package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

public final class IsTrainManager {

	private EphemeralBlockMap<Block, Boolean> IsTrain = new EphemeralBlockMap<Block, Boolean>(14);
	
	public EphemeralBlockMap<Block, Boolean> getMap() {
		return IsTrain;
	}
	

}
