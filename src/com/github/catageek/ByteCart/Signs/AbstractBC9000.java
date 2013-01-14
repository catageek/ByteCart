package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoiderBuilder;
import com.github.catageek.ByteCart.EventManagement.AbstractTriggeredIC;
import com.github.catageek.ByteCart.EventManagement.TriggeredIC;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.HAL.Registry;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.HAL.SubRegistry;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Routing.UpdaterLocal;
import com.github.catageek.ByteCart.Util.MathUtil;


abstract public class AbstractBC9000 extends AbstractTriggeredIC {

	protected int netmask;
	protected CollisionAvoiderBuilder builder;

	public AbstractBC9000(String name, org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.Buildtax = ByteCart.myPlugin.getConfig().getInt("buildtax." + this.Name);
		this.Name = name;
		this.Permission = this.Permission + this.Name;
		builder = new SimpleCollisionAvoiderBuilder((TriggeredIC) this, block.getRelative(this.getCardinal(), 3).getLocation());
		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : SimpleCollisionAvoiderBuilder(" + block.getRelative(this.getCardinal(), 3).getLocation()+")");
		 */	}

	public void trigger() {
		try {

			this.addIO();

			SimpleCollisionAvoider intersection = ByteCart.myPlugin.getCollisionAvoiderManager().<SimpleCollisionAvoider>getCollisionAvoider(builder);

			if (! ByteCart.myPlugin.getUm().isUpdater(this.getVehicle().getEntityId(), this.getLevel())
					&& !ByteCart.myPlugin.getUm().isUpdater(this.getVehicle().getEntityId(), Level.RESET_LOCAL )) {

				// if this is a cart in a train
				if (this.wasTrain(this.getLocation())) {
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(getBlock().getLocation());
					ByteCart.myPlugin.getCollisionAvoiderManager().<SimpleCollisionAvoider>getCollisionAvoider(builder).Book(this.isTrain());
					return;
				}

				// if this is the first car of a train
				// we keep it during 2 s
				if (this.isTrain()) {
					this.setWasTrain(this.getLocation(), true);
				}

				intersection.WishToGo(this.route(), this.isTrain());
				return;
			}

			manageUpdater(intersection);

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

	protected void manageUpdater(SimpleCollisionAvoider intersection) {
		// it's an updater, so let it choosing direction
		Updater updater = new UpdaterLocal(getVehicle(),
				AddressFactory.getAddress(this.getBlock(),3), null, netmask, getLevel());

		// routing
		Side to = intersection.WishToGo(updater.giveSimpleDirection(), false);

		// here we perform routes update
		updater.Update(to);
	}

	protected SimpleCollisionAvoider.Side route() {
		if (this.isAddressMatching())
			return SimpleCollisionAvoider.Side.RIGHT;
		return SimpleCollisionAvoider.Side.LEFT;
	}

	protected final RegistryInput applyNetmask(RegistryInput station) {
		if (this.netmask < station.length())
			return new SubRegistry((Registry) station, this.netmask, 0);
		return station;
	}

	final protected boolean isAddressMatching() {
		return this.getInput(2).getAmount() == this.getInput(5).getAmount()
				&& this.getInput(1).getAmount() == this.getInput(4).getAmount()
				&& this.getInput(0).getAmount() == this.getInput(3).getAmount();
	}


	protected Updater.Level getLevel() {
		return Updater.Level.LOCAL;
	}

	protected void addIO() {
		Address sign = AddressFactory.getAddress(this.getBlock(),3);


		// Output[0] = 2 bits registry representing levers on the left and on the right of the sign
		OutputPin[] lever2 = new OutputPin[2];

		// Left
		lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
		// Right
		lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

		PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

		this.addOutputRegistry(command1);

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

	}
}
