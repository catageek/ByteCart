package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoiderBuilder;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCart.Routing.UpdaterContentFactory;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.HAL.RegistryBoth;
import com.github.catageek.ByteCartAPI.HAL.RegistryInput;
import com.github.catageek.ByteCartAPI.Routing.Updater;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * An abstract class for T-intersection signs
 */
abstract class AbstractSimpleCrossroad extends AbstractTriggeredSign implements BCSign {

	protected CollisionAvoiderBuilder builder;
	private AddressRouted destination;


	AbstractSimpleCrossroad(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		builder = new SimpleCollisionAvoiderBuilder((Triggable) this, block.getRelative(this.getCardinal(), 3).getLocation());
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.AbstractIC#getName()
	 */
	@Override
	abstract public String getName();

	/**
	 * Register the inputs and outputs
	 *
	 */
	protected void addIO() {
		// Output[0] = 2 bits registry representing levers on the left and on the right of the sign
		OutputPin[] lever2 = new OutputPin[2];

		// Left
		lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
		// Right
		lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

		PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

		this.addOutputRegistry(command1);
	}
	
	protected final void addIOInv() {
		// Input[0] = destination region taken from Inventory, slot #0


		Address IPaddress = getDestinationAddress();

		if (IPaddress == null)
			return;

		RegistryInput slot2 = IPaddress.getRegion();


		this.addInputRegistry(slot2);

		// Input[1] = destination track taken from cart, slot #1

		RegistryInput slot1 = IPaddress.getTrack();


		this.addInputRegistry(slot1);

		// Input[2] = destination station taken from cart, slot #2

		RegistryBoth slot0 = IPaddress.getStation();

		this.addInputRegistry(slot0);
	}


	protected void manageUpdater(SimpleCollisionAvoider intersection) {
		// routing
		intersection.WishToGo(route(), false);
	}

	protected Side route() {
		return Side.LEVER_OFF;
	}
	
	public void trigger() {
		try {

			this.addIO();

			SimpleCollisionAvoider intersection = ByteCart.myPlugin.getCollisionAvoiderManager().<SimpleCollisionAvoider>getCollisionAvoider(builder);

			if (! UpdaterContentFactory.isRoutingTableExchange(getInventory())) {

				boolean isTrain = AbstractTriggeredSign.isTrain(getDestinationAddress());

				// if this is a cart in a train
				if (this.wasTrain(this.getLocation())) {
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(getBlock().getLocation());
					intersection.Book(isTrain);
					return;
				}

				// if this is the first car of a train
				// we keep it during 2 s
				if (isTrain) {
					this.setWasTrain(this.getLocation(), true);
				}

				intersection.WishToGo(this.route(), isTrain);
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

	protected final AddressRouted getDestinationAddress() {
		if (destination != null)
			return destination;
		return destination = AddressFactory.getAddress(this.getInventory());
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getLevel()
	 */
	@Override
	public Updater.Level getLevel() {
		return Updater.Level.LOCAL;
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getSignAddress()
	 */
	@Override
	public final Address getSignAddress() {
		return AddressFactory.getAddress(getBlock(), 3);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getCenter()
	 */
	@Override
	public final org.bukkit.block.Block getCenter() {
		return this.getBlock();
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BCSign#getDestinationIP()
	 */
	@Override
	public final String getDestinationIP() {
		Address ip;
		if ((ip = getDestinationAddress()) != null)
			return ip.toString();
		return "";
	}
}
