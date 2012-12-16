package com.github.catageek.ByteCart;

import org.bukkit.block.Block;



// powers the lever in function of the direction selected
public final class BC2002 extends AbstractIC implements TriggeredIC {

	
	public BC2002(Block block) {
		super(block);
	}

	@Override
	public void trigger() {
		// select a output line in function of the address input
		try {

			this.getOutput(0).setAmount(1 << (this.getInput(0).getAmount() % this.getOutput(0).length()));
		}
		catch (NullPointerException e) {
			
		}

	}

}
