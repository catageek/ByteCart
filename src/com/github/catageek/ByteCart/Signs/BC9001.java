package com.github.catageek.ByteCart.Signs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Event.SignPostStationEvent;
import com.github.catageek.ByteCart.Event.SignPreStationEvent;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.InputPin;
import com.github.catageek.ByteCart.IO.InputFactory;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;
import com.github.catageek.ByteCart.Util.MathUtil;


public class BC9001 extends AbstractBC9000 implements Subnet, Powerable, Triggable {


	public BC9001(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 4;
	}
	@Override
	public void trigger() {
		try {

			Address sign = AddressFactory.getAddress(this.getBlock(),3);

			this.addIO();

			// input[6] = redstone for "full station" signal

			InputPin[] wire = new InputPin[2];

			// Right
			wire[0] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2).getRelative(MathUtil.clockwise(getCardinal())));
			// left
			wire[1] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2).getRelative(MathUtil.anticlockwise(getCardinal())));

			// InputRegistry[0] = start/stop command
			this.addInputRegistry(new PinRegistry<InputPin>(wire));

			triggerBC7003();

			if (! ByteCart.myPlugin.getUm().isUpdater(this.getVehicle().getEntityId())) {

				// if this is a cart in a train
				if (this.wasTrain(this.getLocation())) {
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
					//				this.getOutput(0).setAmount(3);	// push buttons
					return;
				}

				// if this is the first car of a train
				// we keep the state during 2 s
				if (this.isTrain()) {
					this.setWasTrain(this.getLocation(), true);
				}

				this.route();

				if(this.isAddressMatching() && this.getName().equals("BC9001") && this.getInventory().getHolder() instanceof Player) {
					((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.GREEN + ByteCart.myPlugin.getConfig().getString("Info.Destination") + " " + this.getFriendlyName() + " (" + sign + ")");
	
				}
				return;
			}
			
			// it's an updater, so let it choosing direction
			Updater updater = UpdaterFactory.getUpdater(this);

			// routing
			this.getOutput(0).setAmount(0); // unpower levers

			// here we perform routes update
			updater.doAction(Side.LEFT);



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

	public void power() {
		this.powerBC7003();
	}


	protected void triggerBC7003() {
		(new BC7003(this.getBlock())).trigger();
	}

	protected void powerBC7003() {
		(new BC7003(this.getBlock())).power();
	}


	protected SimpleCollisionAvoider.Side route() {
		SignPreStationEvent event;
		SignPostStationEvent event1;
		// test if every destination field matches sign field
		if (this.isAddressMatching()  && this.getInput(6).getAmount() == 0)
			event = new SignPreStationEvent(this, Side.RIGHT); // power levers if matching
		else
			event = new SignPreStationEvent(this, Side.LEFT); // unpower levers if not matching
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if (event.getSide().equals(Side.RIGHT)  && this.getInput(6).getAmount() == 0) {
			this.getOutput(0).setAmount(3); // power levers if matching
			event1 = new SignPostStationEvent(this, Side.RIGHT);
		}
		else
		{
			this.getOutput(0).setAmount(0); // unpower levers if not matching
			event1 = new SignPostStationEvent(this, Side.RIGHT);
		}
		Bukkit.getServer().getPluginManager().callEvent(event1);
		return null;
	}

	@Override
	public final String getName() {
		return "BC9001";
	}

	@Override
	public final String getFriendlyName() {
		return "Station";
	}

}
