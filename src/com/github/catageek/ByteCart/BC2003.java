package com.github.catageek.ByteCart;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;


// this IC is used in conjunction with BC800x blocks
// it gives a token to a cart and powers the "busy" buttons

public class BC2003 extends AbstractTriggeredIC implements TriggeredIC {

	final static private BlockMap<Integer> TokenManager = new BlockMap<Integer>();
//	final static private BlockMap DelayedThread = new BlockMap();
//	final static private BlockMap State = new BlockMap();

	final private Block center;

	private DirectionRegistry input;

	public BC2003(Block block, Vehicle vehicle) {
		super(block, vehicle);

		this.input = new DirectionRegistry(15);


		// Centre de l'aiguillage
		this.center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));
		
		OutputPin[] sortie = new OutputPin[4];


		// East
		sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH,6));
		// North
		sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST,6));
		// West
		sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH,6));
		// South
		sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST,6));

		PinRegistry<OutputPin> buttons = new PinRegistry<OutputPin>(sortie);

		this.addOutputRegistry(buttons);

	}

	@Override
	public void trigger() {
		
		// if it's a train we close all doors but one
		if (this.isTrain()) {
			this.input.setCardinal(this.getCardinal().getOppositeFace(), false);
		} 


		final BC2003 myBC2003 = this;

		final int s = this.input.getAmount();
		
		// add state to map
		this.setState(center, s);

		/*		if(ByteCart.debug) {
			ByteCart.log.info("ByteCart: BC2003 : Object instantiated" );
		}

		if(ByteCart.debug) {
			ByteCart.log.info("ByteCart: BC2003 : value is " + s );
		}
		 */
		// a little dirty

		// we power all bits after a delay
		ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Runnable() {
			public void run() {

				// we set busy
				myBC2003.getOutput(0).setAmount(s);

/*				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: BC2003 : running delayed thread (set busy line ON)");
*/

				// we set unbusy 2 sec after
				ByteCart.myPlugin.getDelayedThreadManager().createReleaseTask(center, 40, new ReleaseTokenTask(myBC2003));

			}
		}
		, 6);


	}

	public final boolean hasToken(Vehicle v) {
		if (BC2003.TokenManager.hasEntry(center) && v != null && (Integer) BC2003.TokenManager.getValue(center) == v.getEntityId()) {
			return true;
		}
		return false;
	}
	
	public final boolean requestToken() {
		return BC2003.TokenManager.createEntry(center, this.getVehicle().getEntityId());
	}
	
	public final void renewToken() {
		// we push the busy buttons
		this.getOutput(0).setAmount(this.getState(center));
		
		ByteCart.myPlugin.getDelayedThreadManager().renew(center, 40, new ReleaseTokenTask(this));
	}

	private class ReleaseTokenTask implements Runnable {
		
		AbstractIC bc;
		
		ReleaseTokenTask(AbstractIC bc) {
			this.bc = bc;
		}
		
		@Override
		public void run() {

			// we set not busy

			BC2003.TokenManager.deleteEntry(center);
			bc.free(center);
			
			
		}
		
		
	}
	
	public final int getState() {
		return this.getState(center);
	}
	
	/*
	 * tells if there is a token with keypair (block, vehicle)
	 */
	public final boolean hasEntry(Block block, Vehicle v) {
		if (BC2003.TokenManager.hasEntry(block) && v != null && BC2003.TokenManager.getValue(block) == v.getEntityId()) {
			return true;
		}
		return false;
	}


	
}

