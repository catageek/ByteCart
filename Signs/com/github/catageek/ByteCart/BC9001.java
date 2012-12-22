package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


public class BC9001 extends AbstractBC9000 implements TriggeredIC, PoweredIC {


	public BC9001(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 4;
		this.Name = "BC9001";
		this.FriendlyName = "Station";
	}
	@Override
	public void trigger() {
		try {

			Address sign = AddressFactory.getAddress(this.getBlock(),3);

			this.addIO();

			// input[6] = redstone for "full station" signal

			InputPin[] wire = new InputPin[2];

			// Right
			wire[0] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2).getRelative(MathUtil.clockwise(getCardinal())));
			// left
			wire[1] = InputPinFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2).getRelative(MathUtil.anticlockwise(getCardinal())));

			// InputRegistry[0] = start/stop command
			this.addInputRegistry(new PinRegistry<InputPin>(wire));
			
			triggerBC7003();

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
		// test if every destination field matches sign field
		if (this.isAddressMatching()  && this.getInput(6).getAmount() == 0) {
			this.getOutput(0).setAmount(3); // power levers if matching
		}
		else
		{
			this.getOutput(0).setAmount(0); // unpower levers if not matching
		}
		return null;
	}

}
