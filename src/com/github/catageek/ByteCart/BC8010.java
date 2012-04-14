package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


public class BC8010 extends AbstractTriggeredIC implements TriggeredIC {


	public BC8010(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC8010";
		this.FriendlyName = "L1 router";
		this.Triggertax = ByteCart.myPlugin.getConfig().getInt("usetax." + this.Name);
		this.Permission = this.Permission + this.Name;

	}

	@Override
	public void trigger() {

		TriggeredIC bc2002;

		// Centre de l'aiguillage
		Block center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));
		/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : center at "+center.getLocation().toString());
		 */

		try {
			// Input[0] = destination region taken from Inventory, slot #0			


			AddressRouted IPaddress = AddressFactory.getAddress(this.getInventory());

			Address sign = AddressFactory.getAddress(this.getBlock(),3);

			Inventory ChestInventory = ((InventoryHolder) center.getRelative(BlockFace.UP, 5).getState()).getInventory();

			RoutingTable RoutingTable = RoutingTableFactory.getRoutingTable(ChestInventory);

			// Here begins the triggered action
			Registry direction;
			
			if (IPaddress.getTTL().getAmount() != 0)
			{
				IPaddress.updateTTL(IPaddress.getTTL().getAmount()-1);
			}

			// If not in same region, then we lookup track 0
			if (IPaddress.getRegion().getAmount() != sign.getRegion().getAmount() || IPaddress.getTTL().getAmount() == 0) {
				direction = RoutingTable.getDirection(0);
				
/*			if (this.getInventory().getHolder() instanceof Player) {
					((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.GREEN + ByteCart.myPlugin.getConfig().getString("Info.BC8010_region") + " " + IPaddress.getRegion());
				}
*/
			} else
			{	// same region : lookup destination track
				direction = RoutingTable.getDirection(IPaddress.getTrack().getAmount());
				
/*				if (this.getInventory().getHolder() instanceof Player) {
					((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.GREEN + ByteCart.myPlugin.getConfig().getString("Info.BC8010_track") + " " + IPaddress.getTrack());
				}
*/			}
			// BC2002 Construction

			// Switch selector IC, from 2 bits value to 4 lines

			bc2002 = new BC2002(center);

			bc2002.addInputRegistry(direction);

			// BC2002 Output[0] : levers to command track switch
			OutputPin[] sortie = new OutputPin[4];

			// East
			sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST,3).getRelative(BlockFace.SOUTH));
			// North
			sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST,3).getRelative(BlockFace.NORTH));
			// West
			sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH,3).getRelative(BlockFace.EAST));
			// South
			sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH,3).getRelative(BlockFace.WEST));

			PinRegistry<OutputPin> last = new PinRegistry<OutputPin>(sortie);

			bc2002.addOutputRegistry(last);

			// update of bc1001 output
			//			bc2001.trigger();

			// update of bc1002 output
			bc2002.trigger();

		}
		catch (ClassCastException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : " + e.toString());

			// Not the good blocks to build the signs
			return;
		}
		catch (NullPointerException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : "+ e.toString());

			e.printStackTrace();

			// there was no inventory in the cart
			return;
		}




	}






}
