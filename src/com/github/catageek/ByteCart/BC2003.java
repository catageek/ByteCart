package com.github.catageek.ByteCart;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;


// this IC is used in conjunction with BC800x blocks
// it gives a token to a cart and powers the 4 "busy" buttons

public class BC2003 extends AbstractIC implements TriggeredIC {
	
	final static private ConcurrentHashMap<org.bukkit.block.Block,Vehicle> BusyMap = new ConcurrentHashMap<org.bukkit.block.Block,Vehicle>();

	final private Vehicle vehicle;
	
	final private Block center;

	public BC2003(Block block, Vehicle vehicle) {
		super(block);
		
		this.vehicle = vehicle;
		
		// Centre de l'aiguillage
		this.center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));
	}

	@Override
	public void trigger() {
		
		
		OutputPin[] sortie = new OutputPin[4];

		if ( ! BC2003.BusyMap.containsKey(center) )
			BC2003.BusyMap.put(center, vehicle);
		else
			return;

		
		// East
		sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH,6));
		// North
		sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH,6));
		// West
		sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST,6));
		// South
		sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST,6));

		PinRegistry<OutputPin> buttons = new PinRegistry<OutputPin>(sortie);
		
		this.addOutputRegistry(buttons);
		
		InputPin[] entree = new InputPin[4];

		// East
		entree[0] = InputPinFactory.getInput(center.getRelative(BlockFace.NORTH,6));
		// North
		entree[1] = InputPinFactory.getInput(center.getRelative(BlockFace.SOUTH,6));
		// West
		entree[2] = InputPinFactory.getInput(center.getRelative(BlockFace.WEST,6));
		// South
		entree[3] = InputPinFactory.getInput(center.getRelative(BlockFace.EAST,6));

		this.addInputRegistry(new PinRegistry<InputPin>(entree));
		
		final BC2003 myBC2003 = this;
		
		if(ByteCart.debug) {
			ByteCart.log.info("ByteCart: BC2003 : Object instantiated" );
		}


		// a little dirty
		
			// we power all bits after a delay
			ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Runnable() {
				public void run() {
					
					// we set busy
						myBC2003.getOutput(0).setAmount(15);
						
						if(ByteCart.debug)
							ByteCart.log.info("ByteCart: BC2003 : running delayed thread (set busy line ON)");
				
						
						// we set unbusy 2 sec after
						ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Runnable() {
							public void run() {

								
								if(ByteCart.debug)
									ByteCart.log.info("ByteCart: BC2003 : running delayed thread (set busy line OFF)");
								
								// we set not busy

								BC2003.BusyMap.remove(center);
										

								}
						}
						, 40);					

					}
			}
			, 2);
		

	}
	
	public boolean hasToken(Vehicle v) {
		if (BC2003.BusyMap.containsKey(center) && BC2003.BusyMap.get(center) == v)
			return true;
		return false;
	}
	

}
