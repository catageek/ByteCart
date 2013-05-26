package com.github.catageek.ByteCart.Signs;

import com.github.catageek.ByteCart.AddressLayer.AddressFactory;



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
abstract class AbstractBC9037 extends AbstractBC9000 implements Subnet, Triggable {

	AbstractBC9037(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 8;
	}

	/**
	 *
	 * @return True if the sign uses the negated result of @{@link #isAddressInRange()}.
	 */
	protected abstract boolean negated();

	@Override
	protected void addIO() {
		// add input [0] to [5] from vehicle and 4th line
		super.addIO();
		// add input [6], [7] and [8] from 3th line
		this.addAddressAsInputs(AddressFactory.getAddress(getBlock(), 2));
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
	@Override
	protected boolean isAddressMatching() {
		try {
			int startRegion = getInput(6).getAmount();
			int region = getInput(0).getAmount();
			int endRegion = getInput(3).getAmount();

			int startTrack = getInput(7).getAmount();
			int track = getInput(1).getAmount();
			int endTrack = getInput(4).getAmount();

			int startStation = getInput(8).getAmount();
			int station = getInput(2).getAmount();
			int endStation = getInput(5).getAmount();

			boolean value =	in(startRegion, region, endRegion) &&
					in(startTrack, track, endTrack) &&
					in(startStation, station, endStation);

			if(negated())
				return !value;
			return value;
		} catch (NullPointerException e) {
			// There is no address on sign
		}
		return false;
	}
}
