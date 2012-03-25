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
	

	private Inventory inv = null;
	private Vehicle Vehicle = null;

	// instantiates the BCXXXX member at specified location.
	// return null if no IC is present.
	public TriggeredIC getTriggeredIC(Block block) {
		
			
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			// we extract its #
			
			return this.getTriggeredIC(block, ((Sign) block.getState()).getLine(1));

		
		}
		// no BC sign post
		
		return null;
		
	}

	public TriggeredIC getTriggeredIC(Block block,Vehicle vehicle) {
		
		if(AbstractIC.checkEligibility(block)) {
			
			// if there is really a BC sign post
			
			// we load vehicle as member variable
			if (vehicle instanceof Minecart)
				this.Vehicle = vehicle;
			
			// we load inventory of cart or player
			
			if (vehicle instanceof StorageMinecart)
				this.inv = ((StorageMinecart) vehicle).getInventory();
			else if ((vehicle instanceof Minecart)
					&& (! vehicle.isEmpty())
					&& ((Minecart) vehicle).getPassenger() instanceof Player) {
				this.inv = ((Player) vehicle.getPassenger()).getInventory();
						if(ByteCart.debug)
							ByteCart.log.info("ByteCart: loading player inventory :" + ((Player) vehicle.getPassenger()).getDisplayName());
			}
			
			return this.getTriggeredIC(block);

		}
		return null;
	}
	
	public TriggeredIC getTriggeredIC(Block block, String signString) {

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
					return (new BC7001(block)).AddVehicle(this.Vehicle);
				}
		
				case 8010:
				{
					// first we power the busy bit and requests the token
					BC2003 bc2003 = new BC2003(block, this.Vehicle);
					try {
						bc2003.trigger();
					}
					catch (NullPointerException e) {
						
					}
					// if we have the token, we trigger it
					if (bc2003.hasToken(this.Vehicle))
						return new BC8010(block, this.inv);
					else // or we die
						return null;
				}
				case 9001:
					return new BC9001(block, this.inv);
				case 9002:
					return new BC9002(block, this.inv);
				case 9004:
					return new BC9004(block, this.inv);
				case 9008:
					return new BC9008(block, this.inv);
				case 9016:
					return new BC9016(block, this.inv);
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
