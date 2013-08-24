package com.github.catageek.ByteCart.Signs;

/**
 * Power the lever on the train head but not on wagons
 */
final class BC7021 extends BC7020 implements Triggable {

	BC7021(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7020#actionWagon()
	 */
	@Override
	protected void actionWagon() {
		this.getOutput(0).setAmount(0);	// deactivate levers
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7020#getName()
	 */
	@Override
	public final String getName() {
		return "BC7021";
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC7020#getFriendlyName()
	 */
	@Override
	public final String getFriendlyName() {
		return "Train head";
	}

}
