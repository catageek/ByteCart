package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


public class BC8020 extends AbstractTriggeredIC implements TriggeredIC {


	public BC8020(Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC8020";
		this.FriendlyName = "L2 router";
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
			// reading destination address of the cart
			AddressRouted IPaddress = AddressFactory.getAddress(this.getInventory());

			// reading address written on BC8020 sign
			//Address sign = AddressFactory.getAddress(this.getBlock(),3);

			// Loading inventory of chest above router
			Inventory ChestInventory = ((InventoryHolder) center.getRelative(BlockFace.UP, 5).getState()).getInventory();

			// Converting inventory in routing table
			RoutingTable RoutingTable = RoutingTableFactory.getRoutingTable(ChestInventory);

			// Here begins the triggered action
			Registry direction;

			// Time-to-live management
			
			//loading TTl of cart
			int ttl = IPaddress.getTTL().getAmount();

			// if ttl did not reach end of life ( = 1)
			if (ttl != 1) {
				
				// we update it
				if (ttl != 0)
				{
					IPaddress.updateTTL(ttl-1);
				}
				else {
					IPaddress.updateTTL(ByteCart.myPlugin.getConfig().getInt("TTL.value"));
				}
			}

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : TTL is " + IPaddress.getTTL().getAmount());

			// if TTL reached end of life, then we lookup region 0
			if (IPaddress.getTTL().getAmount() == 1) {
				direction = RoutingTable.getDirection(0);
			} else
			{	// lookup destination region
				direction = RoutingTable.getDirection(IPaddress.getRegion().getAmount());

				/*				if (this.getInventory().getHolder() instanceof Player) {
					((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.GREEN + ByteCart.myPlugin.getConfig().getString("Info.BC8010_track") + " " + IPaddress.getRegion());
				}
				 */			}
			// BC2002 Construction

			// Switch selector IC, from 2 bits value to 4 physical switches

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
