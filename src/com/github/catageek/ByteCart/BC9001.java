package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BC9001 extends AbstractTriggeredIC implements TriggeredIC {

	protected int netmask;

	public BC9001(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 4;
		this.Name = "BC9001";
		this.FriendlyName = "Station";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;
	}

	@Override
	public void trigger() {
		try {
			// Input[0] = destination region taken from Inventory, slot #0			

			
			Address IPaddress = AddressFactory.getAddress(this.getInventory());
			
			Registry slot2 = IPaddress.getRegion();
			

			this.addInputRegistry(slot2);

			// Input[1] = destination track taken from cart, slot #1

			RegistryInput slot1 = IPaddress.getTrack();
			

			this.addInputRegistry(slot1);

			// Input[2] = destination station taken from cart, slot #2, 6 bits

			RegistryInput slot0 = IPaddress.getStation();
			
			
			// We keep only the X most significant bits (netmask)

			slot0 = applyNetmask(slot0);
			
			this.addInputRegistry(slot0);
			
			
			// Address is on a sign, line #3
			
			Address sign = AddressFactory.getAddress(this.getBlock(),3);

			RegistryInput region = sign.getRegion();

			this.addInputRegistry(region);
			
			// Input[4] = station track from sign, line #3, 6 bits registry

//			RegistryInput track = new SignRegistry(this.getBlock(), 3, 6);
			RegistryInput track = sign.getTrack();

			// only 5 most significant bits are taken into account

			//track = new SubRegistry(track, 5, 0);

			this.addInputRegistry(track);

			// Input[5] = station number from sign, line #0, 6 bits registry

			//RegistryInput station = new SignRegistry(this.getBlock(), 0, 6);
			RegistryInput station = sign.getStation();

			// We keep only the X most significant bits (netmask)

			station = applyNetmask(station);

			this.addInputRegistry(station);


			// Output[0] = 2 bits registry representing buttons on the left and on the right of the sign
			OutputPin[] button = new OutputPin[2];
			
			// Left
			button[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
			// Right
			button[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

			PinRegistry<OutputPin> power = new PinRegistry<OutputPin>(button);
			
			this.addOutputRegistry(power);
			
			// here is the triggered action
			
/*			if(ByteCart.debug) {
				for (int i=0; i<6; i++)
					ByteCart.log.info("ByteCart : BC1003 input(" + i + ") = " + this.getInput(i).getAmount());
			}
*/			
			
			// test if every destination field matches sign field
			if (this.getInput(2).getAmount() == this.getInput(5).getAmount()
				&& this.getInput(1).getAmount() == this.getInput(4).getAmount()
				&& this.getInput(0).getAmount() == this.getInput(3).getAmount()) {
				
					this.getOutput(0).setAmount(3); // power buttons if matching
					
					if(this.getName().equals("BC9001")) {
						((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.GREEN + ByteCart.myPlugin.getConfig().getString("Info.Destination") + " " + this.getFriendlyName() + " (" + sign.getAddress() + ")");
						}
			}
			
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

			// there was no inventory in the cart
			return;
		}
	}

	protected RegistryInput applyNetmask(RegistryInput station) {
		if (this.netmask < station.length())
			return new SubRegistry((Registry) station, this.netmask, 0);
		return station;
	}


}
