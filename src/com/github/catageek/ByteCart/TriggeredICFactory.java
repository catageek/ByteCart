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
				case 1001:
					return new Station(block, TriggeredICFactory.inv);
				case 1002:
					return new Subnet2(block, TriggeredICFactory.inv);
				case 1004:
					return new Subnet4(block, TriggeredICFactory.inv);
				case 1008:
					return new Subnet8(block, TriggeredICFactory.inv);
				case 1016:
					return new Subnet16(block, TriggeredICFactory.inv);
				case 1032:
					return new Subnet32(block, TriggeredICFactory.inv);
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
