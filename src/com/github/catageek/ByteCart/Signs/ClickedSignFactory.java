package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;


// This class contains the method to instantiate any IC
final public class ClickedSignFactory {
	


	// instantiates the BCXXXX member at specified location.
	// return null if no IC is present.
	static final public ClickedSign getClickedIC(Block block, Player player) {
		
			
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			// we extract its #
			
			return ClickedSignFactory.getClickedIC(block, ((Sign) block.getState()).getLine(1), player);

		
		}
		// no BC sign post
		
		return null;
		
	}


	
	static final public ClickedSign getClickedIC(Block block, String signString, Player player) {

		int ICnumber = Integer.parseInt(signString.substring(3, 7));
/*		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: triggered #IC " + ICnumber + " detected");
*/		
		
		try {

			// then we instantiate accordingly
			switch (ICnumber) {
			
				case 7010:
					return new BC7010(block, player);
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
