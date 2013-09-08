/**
 * 
 */
package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.entity.Minecart;

import com.github.catageek.ByteCart.Util.MathUtil;

/**
 * An unbooster
 */
final class BC7007 extends AbstractTriggeredSign implements Triggable {

	/**
	 * @param block
	 * @param vehicle
	 */
	public BC7007(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.Triggable#trigger()
	 */
	@Override
	public void trigger() throws ClassNotFoundException, IOException {
		org.bukkit.entity.Vehicle vehicle = this.getVehicle();
		Minecart cart = (Minecart) vehicle;
		if (cart.getMaxSpeed() > 0.4D)
			cart.setMaxSpeed(0.4D);
		
		MathUtil.setSpeed(cart, 0.4D);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	public String getName() {
		return "BC7007";
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getFriendlyName()
	 */
	public String getFriendlyName() {
		return "Unbooster";
	}
}
