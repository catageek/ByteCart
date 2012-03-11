package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.Inventory;

// This class contains the method to instantiate any IC
final public class TriggeredICFactory {
	
	private static Inventory inv = null;

	// instantiates the BCXXXX member at specified location.
	// return null if no IC is present.
	static public TriggeredIC getTriggeredIC(Block block) {
		
			
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			// we extract its #
			
			return TriggeredICFactory.getTriggeredIC(block, ((Sign) block.getState()).getLine(1));

		
		}
		// no BC sign post
		
		return null;
		
	}

	public static TriggeredIC getTriggeredIC(Block block,Vehicle vehicle) {
		
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			// we load inventory of cart or player
			
			if (vehicle instanceof StorageMinecart)
				TriggeredICFactory.inv = ((StorageMinecart) vehicle).getInventory();
			else if ((vehicle instanceof Minecart)
					&& (! vehicle.isEmpty())
					&& ((Minecart) vehicle).getPassenger() instanceof Player) {
				TriggeredICFactory.inv = ((Player) vehicle.getPassenger()).getInventory();
						if(ByteCart.debug)
							ByteCart.log.info("ByteCart: loading player inventory :" + ((Player) vehicle.getPassenger()).getDisplayName());
			}
			
			return TriggeredICFactory.getTriggeredIC(block);

		}
		return null;
	}
	
	public static TriggeredIC getTriggeredIC(Block block, String signString) {

		int ICnumber = Integer.parseInt(signString.substring(3, 7));
		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: #IC " + ICnumber + " detected");
		
		
		try {

			// then we instantiate accordingly
			switch (ICnumber) {
		
				case 9000:
					return new BC9000(block, TriggeredICFactory.inv);
				case 9001:
					return new BC9001(block, TriggeredICFactory.inv);
				case 9002:
					return new BC9002(block, TriggeredICFactory.inv);
				case 9004:
					return new BC9004(block, TriggeredICFactory.inv);
				case 9008:
					return new BC9008(block, TriggeredICFactory.inv);
				case 9016:
					return new BC9016(block, TriggeredICFactory.inv);
				case 9032:
					return new BC9032(block, TriggeredICFactory.inv);
			}
		}
		catch (Exception e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : "+ e.toString());

			// there was no inventory in the cart
			return null;
		}
		
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: #IC " + ICnumber + " not activated");
		
		return null;
		
	}
	

}
