package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Vehicle;

// This class contains the method to instantiate any IC
final public class TriggeredICFactory {
	


	// instantiates the BCXXXX member at specified location.
	// return null if no IC is present.
	static final public TriggeredIC getTriggeredIC(Block block, Vehicle vehicle) {
		
			
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			// we extract its #
			
			return TriggeredICFactory.getTriggeredIC(block, ((Sign) block.getState()).getLine(1), vehicle);

		
		}
		// no BC sign post
		
		return null;
		
	}


	
	static final public TriggeredIC getTriggeredIC(Block block, String signString, Vehicle vehicle) {

		int ICnumber = Integer.parseInt(signString.substring(3, 7));
/*		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: triggered #IC " + ICnumber + " detected");
*/		
		
		try {

			// then we instantiate accordingly
			switch (ICnumber) {
			
				case 7001:
				{
					return (new BC7001(block, vehicle));
				}
		
				case 8010:
				{
					// first we power the busy bit and requests the token
					BC2003 bc2003 = new BC2003(block, vehicle);
					try {
						bc2003.trigger();
					}
					catch (NullPointerException e) {
						
					}
					// if we have the token, we trigger it
					if (bc2003.hasToken(vehicle))
						return new BC8010(block, vehicle);
					else // or we die
						return null;
				}
				case 9001:
					return new BC9001(block, vehicle);
				case 9002:
					return new BC9002(block, vehicle);
				case 9004:
					return new BC9004(block, vehicle);
				case 9008:
					return new BC9008(block, vehicle);
				case 9016:
					return new BC9016(block, vehicle);
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
