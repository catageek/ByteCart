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

		// Centre de l'aiguillage
		Block center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));
		/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : center at "+center.getLocation().toString());
		 */

		try {
			// Input[0] = destination region taken from Inventory, slot #0			


			// reading destination address of the cart
			AddressRouted IPaddress = AddressFactory.getAddress(this.getInventory());

			// reading address written on BC8010 sign
			Address sign = AddressFactory.getAddress(this.getBlock(),3);

			// Loading inventory of chest above router
			Inventory ChestInventory = ((InventoryHolder) center.getRelative(BlockFace.UP, 5).getState()).getInventory();

			// Converting inventory in routing table
			RoutingTable RoutingTable = RoutingTableFactory.getRoutingTable(ChestInventory);

			// Here begins the triggered action

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

			
			DirectionRegistry direction = this.SelectRoute(IPaddress, sign, RoutingTable);
			
			OutputPin[] sortie = new OutputPin[4];

			// South
			sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST,3).getRelative(BlockFace.SOUTH));
			// West
			sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST,3).getRelative(BlockFace.NORTH));
			// North
			sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH,3).getRelative(BlockFace.EAST));
			// East
			sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH,3).getRelative(BlockFace.WEST));

			PinRegistry<OutputPin> last = new PinRegistry<OutputPin>(sortie);

			this.addOutputRegistry(last);

			this.getOutput(0).setAmount(direction.getAmount());

		}
		catch (ClassCastException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : " + e.toString());
			e.printStackTrace();

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
	
	protected DirectionRegistry SelectRoute(AddressRouted IPaddress, Address sign, RoutingTable RoutingTable) {
		// If not in same region, or if TTL reached end of life, then we lookup track 0
		if (IPaddress.getRegion().getAmount() != sign.getRegion().getAmount() || IPaddress.getTTL().getAmount() == 1) {
			return RoutingTable.getDirection(0);
		} else
		{	// same region : lookup destination track
			return RoutingTable.getDirection(IPaddress.getTrack().getAmount());
		}
		
	}





}
