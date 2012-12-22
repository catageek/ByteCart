package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.Signs.BC7001;
import com.github.catageek.ByteCart.Signs.BC7002;
import com.github.catageek.ByteCart.Signs.BC7003;
import com.github.catageek.ByteCart.Signs.BC7010;
import com.github.catageek.ByteCart.Signs.BC7011;
import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCart.Signs.BC8020;
import com.github.catageek.ByteCart.Signs.BC9000;
import com.github.catageek.ByteCart.Signs.BC9001;
import com.github.catageek.ByteCart.Signs.BC9002;
import com.github.catageek.ByteCart.Signs.BC9004;
import com.github.catageek.ByteCart.Signs.BC9008;
import com.github.catageek.ByteCart.Signs.BC9016;


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
				return (TriggeredIC)(new BC7001(block, vehicle));
			case 7002:
				return (TriggeredIC)(new BC7002(block, vehicle));
			case 7003:
				return (TriggeredIC)(new BC7003(block));
			case 7010:
				return (TriggeredIC)(new BC7010(block, vehicle));
			case 7011:
				return (TriggeredIC)(new BC7011(block, vehicle));

			case 8010:
				return (TriggeredIC)(new BC8010(block, vehicle));

			case 8020:
				return (TriggeredIC)(new BC8020(block, vehicle));

			case 9000:
				return (TriggeredIC)(new BC9000(block, vehicle));
			case 9001:
				return (TriggeredIC)(new BC9001(block, vehicle));
			case 9002:
				return (TriggeredIC)(new BC9002(block, vehicle));
			case 9004:
				return (TriggeredIC)(new BC9004(block, vehicle));
			case 9008:
				return (TriggeredIC)(new BC9008(block, vehicle));
			case 9016:
				return (TriggeredIC)(new BC9016(block, vehicle));
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
