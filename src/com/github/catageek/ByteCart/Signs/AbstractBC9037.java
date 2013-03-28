package com.github.catageek.ByteCart.Signs;

import org.bukkit.Bukkit;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Event.SignPostStationEvent;
import com.github.catageek.ByteCart.Event.SignPreStationEvent;
import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;



/**
 * Match IP ranges.
 *
 * Example sign content:
 * 1. Empty
 * 2. [BCxxxx]
 * 3. AA.BB.CC
 * 4. XX.YY.ZZ
 *
 * Line 3 and 4 name the start and end of the range respectively.
 * There are two possible implementations: normal and negated.
 *
 * - Example on-state with normal implementation and configuration from above:
 *   onState <=> AA.BB.CC <= IP <= XX.YY.ZZ
 *
 * - Example on-state with negated implementation and configuration from above:
 *   onState <=> !(AA.BB.CC <= IP <= XX.YY.ZZ)
 */
public abstract class AbstractBC9037 extends AbstractBC9000 implements Subnet, Triggable {

	public AbstractBC9037(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 4;
	}

	/**
	 *
	 * @return True if the sign uses the negated result of @{@link #isAddressInRange()}.
	 */
	protected abstract boolean negated();

	/**
	 *
	 * @return Range start address from the third line.
	 */
	protected Address getRangeStartAddress() {
		return AddressFactory.getAddress(getBlock(), 2);
	}

	/**
	 * @return Range end address from the fourth line.
	 */
	protected Address getRangeEndAddress() {
		return AddressFactory.getAddress(getBlock(), 3);
	}

	protected Address getVehicleAddress() {
		return AddressFactory.getAddress(this.getInventory());
	}

	protected void addAddressAsInputs(Address addr) {
		if(addr.isValid()) {
			RegistryInput region = addr.getRegion();
			this.addInputRegistry(region);

			RegistryInput track = addr.getTrack();
			this.addInputRegistry(track);

			RegistryBoth station = addr.getStation();
			this.addInputRegistry(station);
		}
	}

	@Override
	public void trigger() {
		try {
			this.addIO();

			if (! ByteCart.myPlugin.getUm().isUpdater(this.getVehicle().getEntityId())) {

				// if this is a cart in a train
				if (this.wasTrain(this.getLocation())) {
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
					return;
				}

				// if this is the first car of a train
				// we keep the state during 2 s
				if (this.isTrain()) {
					this.setWasTrain(this.getLocation(), true);
				}

				this.route();

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

	/**
	 * Utility method to check whether a integer is between lower bound l and upper bound u.
	 */
	private boolean in(int l, int v, int u) {
		return l <= v && v <= u;
	}

	/**
	 * Check if the vehicle IP is in the configured range.
	 *
	 * The return value depends on the return value of @{@link #negated()}.
	 * The result is negated if said method returns true.
	 *
	 */
	protected boolean isAddressInRange() {
		Address rangeStart = getRangeStartAddress();
		Address rangeEnd = getRangeEndAddress();
		Address ip = getVehicleAddress();

		int startRegion = rangeStart.getRegion().getAmount();
		int region = ip.getRegion().getAmount();
		int endRegion = rangeEnd.getRegion().getAmount();

		int startTrack = rangeStart.getTrack().getAmount();
		int track = ip.getTrack().getAmount();
		int endTrack = rangeEnd.getTrack().getAmount();

		int startStation = rangeStart.getStation().getAmount();
		int station = ip.getStation().getAmount();
		int endStation = rangeEnd.getStation().getAmount();

		boolean value =	in(startRegion, region, endRegion) &&
				in(startTrack, track, endTrack) &&
				in(startStation, station, endStation);

		if(negated())
			return !value;
		return value;
	}

	protected SimpleCollisionAvoider.Side route() {
		SignPreStationEvent event;
		SignPostStationEvent event1;

		// test if every destination field matches sign field
		if (this.isAddressInRange())
			event = new SignPreStationEvent(this, Side.RIGHT); // power levers if matching
		else
			event = new SignPreStationEvent(this, Side.LEFT); // unpower levers if not matching
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (event.getSide().equals(Side.RIGHT)) {
			this.getOutput(0).setAmount(3); // power levers if matching
			event1 = new SignPostStationEvent(this, Side.RIGHT);
		} else {
			this.getOutput(0).setAmount(0); // unpower levers if not matching
			event1 = new SignPostStationEvent(this, Side.RIGHT);
		}
		Bukkit.getServer().getPluginManager().callEvent(event1);

		return null;
	}
}
