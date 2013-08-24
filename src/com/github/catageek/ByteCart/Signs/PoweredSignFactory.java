package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;


/**
 * This class contains the method to instantiate any IC
 */
public class PoweredSignFactory {
	
	/**
	 * Get an IC at the powered sign
	 *
	 * @param block the sign clicked
	 * @return a Powerable IC, or null
	 */
	public Powerable getIC(Block block) {
		
			
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			// we extract its #
			
			return PoweredSignFactory.getPoweredIC(block, ((Sign) block.getState()).getLine(1));

		
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
/*		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Powered #IC " + ICnumber + " detected");
*/		
		
		try {

			// then we instantiate accordingly
			switch (ICnumber) {

				case 7001:
					return (Powerable)(new BC7001(block, null));
				case 7003:
					return (Powerable)(new BC7003(block));
				case 7004:
					return (Powerable)(new BC7004(block, ((Sign) block.getState()).getLine(3)));
				case 9001:
					return (Powerable)(new BC9001(block, null));
				
		
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
