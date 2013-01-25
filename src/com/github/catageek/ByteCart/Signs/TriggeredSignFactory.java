package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;


// This class contains the method to instantiate any IC
final public class TriggeredSignFactory {



	// instantiates the BCXXXX member at specified location.
	// return null if no IC is present.
	static final public TriggeredSign getTriggeredIC(Block block, Vehicle vehicle) {


		if(AbstractIC.checkEligibility(block)) {

			// if there is really a BC sign post
			// we extract its #

			return TriggeredSignFactory.getTriggeredIC(block, ((Sign) block.getState()).getLine(1), vehicle);


		}
		// no BC sign post

		return null;

	}



	static final public TriggeredSign getTriggeredIC(Block block, String signString, Vehicle vehicle) {

		int ICnumber = Integer.parseInt(signString.substring(3, 7));
		/*		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: triggered #IC " + ICnumber + " detected");
		 */		

		try {

			// then we instantiate accordingly
			switch (ICnumber) {

			case 7000:
			case 7001:
				return (TriggeredSign)(new BC7001(block, vehicle));
			case 7002:
				return (TriggeredSign)(new BC7002(block, vehicle));
			case 7003:
				return (TriggeredSign)(new BC7003(block));
			case 7010:
				return (TriggeredSign)(new BC7010(block, vehicle));
			case 7011:
				return (TriggeredSign)(new BC7011(block, vehicle));
			case 7012:
				return (TriggeredSign)(new BC7012(block, vehicle));
			case 7013:
				return (TriggeredSign)(new BC7013(block, vehicle));
			case 7014:
				return (TriggeredSign)(new BC7014(block, vehicle));
			case 7020:
				return (TriggeredSign)(new BC7020(block, vehicle));
			case 7021:
				return (TriggeredSign)(new BC7021(block, vehicle));

			case 8010:
				return (TriggeredSign)(new BC8010(block, vehicle));

			case 8020:
				return (TriggeredSign)(new BC8020(block, vehicle));

			case 9000:
				return (TriggeredSign)(new BC9000(block, vehicle));
			case 9001:
				return (TriggeredSign)(new BC9001(block, vehicle));
			case 9002:
				return (TriggeredSign)(new BC9002(block, vehicle));
			case 9004:
				return (TriggeredSign)(new BC9004(block, vehicle));
			case 9008:
				return (TriggeredSign)(new BC9008(block, vehicle));
			case 9016:
				return (TriggeredSign)(new BC9016(block, vehicle));
			}
		}
		catch (Exception e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : "+ e.toString());
			e.printStackTrace();

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
