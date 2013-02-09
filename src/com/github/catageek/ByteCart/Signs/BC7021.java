package com.github.catageek.ByteCart.Signs;

public final class BC7021 extends BC7020 implements Triggable {

	public BC7021(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}
	
	protected void actionWagon() {
		this.getOutput(0).setAmount(0);	// deactivate levers
	}

	@Override
	public final String getName() {
		return "BC7021";
	}

	@Override
	public final String getFriendlyName() {
		return "Train head";
	}

}
