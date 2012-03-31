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

		VirtualRegistry virtual1 = new VirtualRegistry(6); // 6 bits registry
		VirtualRegistry virtual2 = new VirtualRegistry(6); // 6 bits registry

		TriggeredIC bc2001, bc2002;
		
		// Centre de l'aiguillage
		Block center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));
/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : center at "+center.getLocation().toString());
*/
		
		try {
			// Input[0] = destination region taken from Inventory, slot #0			

			
			Address IPaddress = AddressFactory.getAddress(this.getInventory());
			
			Registry slot2 = IPaddress.getRegion();
			

			this.addInputRegistry(slot2);

			// Input[1] = destination track taken from cart, slot #1

			RegistryInput slot1 = IPaddress.getTrack();
			

			this.addInputRegistry(slot1);

			// Input[2] = Region on the router sign, line #3
			
			Address sign = AddressFactory.getAddress(this.getBlock(),3);

			RegistryInput region = sign.getRegion();

			this.addInputRegistry(region);
			

			// Output[0] = virtual registry #1 (used as input of BC1001), 6 bits
			this.addOutputRegistry(virtual1);


			// BC2001 construction
			
			// routing table lookup IC, outputs direction on 2 bits
			Inventory routingtable = ((InventoryHolder) center.getRelative(BlockFace.UP, 4).getState()).getInventory();

			bc2001 = new BC2001(center.getRelative(BlockFace.UP, 4), routingtable);

			// bc2001.Input[0] = virtual registry #1, 6 bits
			bc2001.addInputRegistry(virtual1);
			
			// bc2001.Output[0] = virtual registry #2, 6 bits
			bc2001.addOutputRegistry(virtual2);

			
			// BC2002 Construction
			
			// Switch selector IC, from 2 bits value to 4 lines
			
			bc2002 = new BC2002(center);
			
			// bc2002.Input[0] = 2 most significant bits of virtual #2
			RegistryInput direction = new SubRegistry(virtual2,2,0);
			
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

			
			// Here begins the triggered action
	
			// If not in same region, then we lookup track 0
			if (this.getInput(0).getAmount() != this.getInput(2).getAmount())
				this.getOutput(0).setAmount(0);
			else
				// same region : lookup destination track
				this.getOutput(0).setAmount(this.getInput(1).getAmount());
			
			// update of bc1001 output
			bc2001.trigger();
			
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
