package com.github.catageek.ByteCart;

import org.bukkit.block.Block;


// reads a slot by its index
public final class BC2002 extends AbstractIC implements TriggeredIC {

	
	public BC2002(Block block) {
		super(block);
	}

	@Override
	public void trigger() {
		// select a output line in function of the address input
		try {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : BC2002 : reading direction "+ (1 << (this.getInput(0).getAmount() % this.getOutput(0).length())));

			this.getOutput(0).setAmount(1 << (this.getInput(0).getAmount() % this.getOutput(0).length()));
		}
		catch (NullPointerException e) {
			
		}

	}

}
