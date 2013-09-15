package com.github.catageek.ByteCart.Signs;

/**
 * Match IP ranges and negate the result.
 *
 * 1. Empty
 * 2. [BC9137]
 * 3. AA.BB.CC
 * 4. XX.YY.ZZ
 *
 * onState <=> !(AA.BB.CC <= IP <= XX.YY.ZZ)
 */
final class BC9137 extends AbstractBC9037 {

	BC9137(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	@Override
	protected boolean negated() {
		return true;
	}

	@Override
	public final String getName() {
		return "BC9137";
	}

	@Override
	public final String getFriendlyName() {
		return "Negated range matcher";
	}

	@Override
	public boolean isLeverReversed() {
		return true;
	}
}
