/**
 * 
 */
package com.github.catageek.ByteCart.Signs;

import org.bukkit.entity.Minecart;

import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A booster
 */
final class BC7006 extends AbstractTriggeredSign implements Triggable {

	/**
	 * @param block
	 * @param vehicle
	 */
	public BC7006(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Triggable#trigger()
	 */
	@Override
	public void trigger() {
		org.bukkit.entity.Vehicle vehicle = this.getVehicle();
		Minecart cart = (Minecart) vehicle;
		if (cart.getMaxSpeed() <= 0.4D)
			cart.setMaxSpeed(0.68D);
		
		MathUtil.setSpeed(cart, 0.68D);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	public String getName() {
		return "BC7006";
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	@Override
	public String getFriendlyName() {
		return "Booster";
	}
}
