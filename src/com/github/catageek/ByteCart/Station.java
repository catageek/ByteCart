package com.github.catageek.ByteCart;

public class Station extends AbstractIC implements TriggeredIC {

	protected final org.bukkit.inventory.Inventory Inventory;
	protected int netmask;

	public Station(org.bukkit.block.Block block, org.bukkit.inventory.Inventory inv) {
		super(block);
		this.Inventory = inv;
		this.netmask = 6;
	}

	@Override
	public void trigger() {
		try {
			// Input[0] = destination region taken from Inventory, slot #2			

			RegistryInput slot2 = new InventorySlot(this.Inventory, 2);
			
			// only 5 most significant bits are taken into account
			
			slot2 = new SubRegistry(slot2, 5, 0);

			this.addInputRegistry(slot2);

			// Input[1] = destination track taken from cart, slot #1

			RegistryInput slot1 = new InventorySlot(this.Inventory, 1);
			
			// only 5 most significant bits are taken into account
			
			slot1 = new SubRegistry(slot1, 5, 0);		

			this.addInputRegistry(slot1);

			// Input[2] = destination station taken from cart, slot #0, 6 bits

			RegistryInput slot0 = new InventorySlot(this.Inventory, 0);
			
			// We keep only the X most significant bits (netmask)

			slot0 = applyNetmask(slot0);
			
			this.addInputRegistry(slot0);

			// Input[3] = region from sign, line #2, 6 bits registry

			RegistryInput region = new SignRegistry(this.getBlock(), 2, 6);

			// only 5 most significant bits are taken into account

			region = new SubRegistry(region, 5, 0);

			this.addInputRegistry(region);
			
			// Input[4] = station track from sign, line #3, 6 bits registry

			RegistryInput track = new SignRegistry(this.getBlock(), 3, 6);

			// only 5 most significant bits are taken into account

			track = new SubRegistry(track, 5, 0);

			this.addInputRegistry(track);

			// Input[5] = station number from sign, line #0, 6 bits registry

			RegistryInput station = new SignRegistry(this.getBlock(), 0, 6);

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
			
			if(ByteCart.debug) {
				for (int i=0; i<6; i++)
					ByteCart.log.info("ByteCart : BC1003 input(" + i + ") = " + this.getInput(i).getAmount());
			}
			
			
			// test if every destination field matches sign field
			if (this.getInput(2).getAmount() == this.getInput(5).getAmount()
				&& this.getInput(1).getAmount() == this.getInput(4).getAmount()
				&& this.getInput(0).getAmount() == this.getInput(3).getAmount())
				
					this.getOutput(0).setAmount(3); // power buttons if matching
			

			
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

	protected RegistryInput applyNetmask(RegistryInput address) {
		if (this.netmask < address.length())
			return new SubRegistry(address, this.netmask, 0);
		return address;
	}


}
