package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;


public class PoweredSignFactory {
	
	// instantiates the BCXXXX member at specified location.
	// return null if no IC is present.
	public Powerable getIC(Block block) {
		
			
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			// we extract its #
			
			return this.getPoweredIC(block, ((Sign) block.getState()).getLine(1));

		
		}
		// no BC sign post
		
		return null;
		
	}

	private Powerable getPoweredIC(Block block, String signString) {

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
