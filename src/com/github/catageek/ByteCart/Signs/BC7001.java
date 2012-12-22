package com.github.catageek.ByteCart.Signs;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.EventManagement.AbstractTriggeredIC;
import com.github.catageek.ByteCart.EventManagement.PoweredIC;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;
import com.github.catageek.ByteCart.EventManagement.TriggeredICFactory;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.InputPin;
import com.github.catageek.ByteCart.IO.InputPinFactory;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCart.Util.MathUtil;


// this IC represents a stop/start block
// it is commanded by a wire (like FB 'station' block)
// wire on => start or no velocity change
// wire off => stop
// it provides a busy bit with a lever on the block above the sign
// lever off = block occupied and not powered
// lever on = block free OR powered

public class BC7001 extends AbstractTriggeredIC implements TriggeredIC, PoweredIC {


	// Constructor : !! vehicle can be null !!

	public BC7001(org.bukkit.block.Block block, Vehicle vehicle) {
		super(block, vehicle);
		this.Name = "BC7001";
		this.FriendlyName = "Stop/Start";
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Permission = this.Permission + this.Name;
	}

	@Override
	public void trigger() {

		// add input command = redstone

		InputPin[] wire = new InputPin[2];

		// Right
		wire[0] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal())));
		// left
		wire[1] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal())));

		// InputRegistry[0] = start/stop command
		this.addInputRegistry(new PinRegistry<InputPin>(wire));

		// add output occupied line = lever

		OutputPin[] lever = new OutputPin[1];

		// Right
		lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal().getOppositeFace()));

		// OutputRegistry[0] = occupied signal
		this.addOutputRegistry(new PinRegistry<OutputPin>(lever));

		// here starts the action

		// is there a minecart above ?
		if (this.getVehicle() != null) {

			// if the wire is on
			if(this.getInput(0).getAmount() > 0) {
				if (this.wasTrain(this.getLocation()))
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(this.getLocation());
/*				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: "+ this.getName() + " at " + this.getLocation() + " : " + this.getVehicle() + " : isTrain() = " + this.isTrain());
*/
				if (this.isTrain()) {
					this.setWasTrain(this.getLocation(), true);
				}

				// the lever is on too
				//this.getOutput(0).setAmount(1);
				final BC7001 myBC7001 = this;

				ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Runnable() {
					public void run() {

						// we set busy
						myBC7001.getOutput(0).setAmount(1);

						/*						if(ByteCart.debug)
							ByteCart.log.info("ByteCart: BC7001 : running delayed thread (set switch ON)");
						 */
					}
				}
				, 6);


				// if the cart is stopped, start it
				if (this.getVehicle().getVelocity().equals(new Vector(0,0,0))) {

					this.getVehicle().setVelocity((new Vector(this.getCardinal().getModX(), this.getCardinal().getModY(), this.getCardinal().getModZ())).multiply(ByteCart.myPlugin.getConfig().getDouble("BC7001.startvelocity")));
				}
			}

			// if the wire is off
			else {

				// stop the cart if this is not a train and tells to the previous block that we are stopped
				if(!this.wasTrain(getLocation())) { 
					// the lever is off
					this.getOutput(0).setAmount(0);
					this.getVehicle().setVelocity(new Vector(0,0,0));
					ByteCart.myPlugin.getIsTrainManager().getMap().remove(getBlock().getRelative(getCardinal().getOppositeFace(), 2).getLocation());
				}
				else
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(this.getLocation());

				/*
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: BC7001 : cart on stop at " + this.Vehicle.getLocation().toString());
				 */
			}
			// if this is the first car of a train
			// we keep it during 2 s
		}

		// there is no minecart above
		else {
			// the lever is on
			this.getOutput(0).setAmount(1);
		}

	}

	@Override
	public void power() {
		// power update

		TriggeredIC bc = this;

		// We need to find if a cart is stopped and set the member variable Vehicle
		Location loc = this.getBlock().getRelative(BlockFace.UP, 2).getLocation();

		List<Entity> ent = Arrays.asList(this.getBlock().getChunk().getEntities());
		/*
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: BC7001 : loading " + ent.size() + " entities.");
		 */
		for (ListIterator<Entity> it = ent.listIterator(); it.hasNext();) {
			/*			
			if(ByteCart.debug) {
				ByteCart.log.info("ByteCart: BC7001 : examining entity at " + it.next().getLocation().toString());
				it.previous();
			}
			 */
			if (it.next() instanceof Minecart) {
				it.previous();

				Location cartloc = ((Minecart) it.next()).getLocation();

				if ( cartloc.getBlockX() == loc.getBlockX() && cartloc.getBlockZ() == loc.getBlockZ()) {
					it.previous();

					// found ! we instantiate a new IC with the vehicle we found
					//bc7001 = new BC7001(this.getBlock(), (Vehicle) it.next());
					bc = TriggeredICFactory.getTriggeredIC(this.getBlock(), (Vehicle) it.next());
					/*
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart: BC7001 : cart on stop");
					 */					
					break;

				}
			}
		}

		bc.trigger();


	}

}
