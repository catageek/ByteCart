package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.SimpleCollisionAvoider.Side;

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

			this.addIO(sign);
			
			triggerBC7003();

			// if this is a cart in a train
			if (this.wasTrain(this.getBlock())) {
				this.getOutput(0).setAmount(this.getInput(6).getAmount());
				ByteCart.myPlugin.getDelayedThreadManager().renew(getBlock(), 40, new ReleaseTask(this));
				return;
			}

			// if this is the first car of a train
			// we keep it during 2 s
			if (this.isTrain()) {
				this.setWasTrain(this.getBlock(), true);
				ByteCart.myPlugin.getDelayedThreadManager().createReleaseTask(getBlock(), 40, new ReleaseTask(this));
			}

			this.route();

			if(this.isAddressMatching() && this.getName().equals("BC9001") && this.getInventory().getHolder() instanceof Player) {
				((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.GREEN + ByteCart.myPlugin.getConfig().getString("Info.Destination") + " " + this.getFriendlyName() + " (" + sign.getAddress() + ")");
			}


			// here is the triggered action

			/*						if(ByteCart.debug) {
				for (int i=0; i<6; i++)
					ByteCart.log.info("ByteCart : 9001 input(" + i + ") = " + this.getInput(i).getAmount());
			}
			 */			 			


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



	protected void addIO(Address sign) {

		super.addIO();

		// input[6] : 2 buttons on the left and on the right of the sign
		InputPin[] button2 = new InputPin[2];

		// Left
		button2[0] = InputPinFactory.getInput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
		// Right
		button2[1] = InputPinFactory.getInput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

		PinRegistry<InputPin> power2 = new PinRegistry<InputPin>(button2);

		this.addInputRegistry(power2);


	}



	

	protected void triggerBC7003() {
		(new BC7003(this.getBlock())).trigger();
	}

	protected void powerBC7003() {
		(new BC7003(this.getBlock())).power();
	}


	protected Side route() {
		// test if every destination field matches sign field
		if (this.isAddressMatching()) {
			this.getOutput(0).setAmount(3); // power buttons if matching
		}
		return null;
	}

}
