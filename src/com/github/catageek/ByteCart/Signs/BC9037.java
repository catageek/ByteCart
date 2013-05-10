package com.github.catageek.ByteCart.Signs;

/**
 * Match IP ranges.
 *
 * 1. Empty
 * 2. [BC9037]
 * 3. AA.BB.CC
 * 4. XX.YY.ZZ
 *
 * onState <=> AA.BB.CC <= IP <= XX.YY.ZZ
 */
public class BC9037 extends AbstractBC9037 {

	public BC9037(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	protected boolean negated() {
		return false;
	}

	public final String getName() {
		return "BC9037";
	}

	public final String getFriendlyName() {
		return "Range matcher";
	}

}
