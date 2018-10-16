package com.github.catageek.ByteCart.Signs;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.Storage.ExpirableSet;


/**
 * This class contains the method to instantiate any IC
 */
public class PoweredSignFactory {
	
	// Singleton set, keeps records 40 ticks to prevent duplicate
	private final ExpirableSet<Location> poweredsignsset = new ExpirableSet<Location>(2, false, "PoweredSign");
	private Location loc = null;

	/**
	 * Get an IC at the powered sign
	 *
	 * @param block the sign clicked
	 * @return a Powerable IC, or null
	 */
	public Powerable getIC(Block block) {
		
			
		if(AbstractIC.checkEligibility(block) && ! this.poweredsignsset.contains(block.getLocation(loc))) {
			
			// if there is really a BC sign post
			// we extract its #
			
			final Powerable ps = PoweredSignFactory.getPoweredIC(block, ((Sign) block.getState()).getLine(1));
			// register it as singleton for this location
			if (ps != null) {
				this.poweredsignsset.add(block.getLocation(loc));
			}
			return ps;
		}
		// no BC sign post
		
		return null;
		
	}

	/**
	 * Get an IC with the specific code
	 *
	 * @param block the block where to reference the IC
	 * @param signString the name of the sign as "BCXXXX"
	 * @return a Powerable IC, or null
	 */
	static final public Powerable getPoweredIC(Block block, String signString) {

		if (signString.length() < 7)
			return null;

		int ICnumber = Integer.parseInt(signString.substring(3, 7));
		
		try {

			// then we instantiate accordingly
			switch (ICnumber) {

				case 7000:
				case 7001:
					return (new BC7001(block, null));
				case 7003:
					return (new BC7003(block));
				case 7004:
					return (new BC7004(block, ((Sign) block.getState()).getLine(3),
							((Sign) block.getState()).getLine(2)));
				case 9001:
					return (new BC9001(block, null));
				
		
			}
		}
		
		catch (Exception e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : "+ e.toString());

			// there was no inventory in the cart
			return null;
		}
/*		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: #IC " + ICnumber + " not activated");
*/		
		return null;
		
	}

}
