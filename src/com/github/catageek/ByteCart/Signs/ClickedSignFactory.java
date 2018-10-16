package com.github.catageek.ByteCart.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * This class contains the method to instantiate any IC
 */
final public class ClickedSignFactory {



	/**
	 * Get an IC at the clicked sign
	 *
	 * @param block the sign clicked
	 * @param player the player who clicked the sign
	 * @return a Clickable IC, or null
	 */
	static final public Clickable getClickedIC(Block block, Player player) {


		if(AbstractIC.checkEligibility(block)) {

			// if there is really a BC sign post
			// we extract its #

			return ClickedSignFactory.getClickedIC(block, ((Sign) block.getState()).getLine(1), player);


		}
		// no BC sign post

		return null;

	}

	/**
	 * Get an IC with a code declared 2 blocks behind the clicked sign
	 *
	 * @param block the sign clicked
	 * @param player the player who clicked the sign
	 * @return a Clickable IC, or null
	 */
	static final public Clickable getBackwardClickedIC(Block block, Player player) {
		final BlockData type = block.getState().getBlockData();
		BlockFace f = null;
		if (type instanceof Directional) {
			f = ((Directional) type).getFacing().getOppositeFace();
		}
		else if(type instanceof Rotatable) {
			f = ((Rotatable) type).getRotation().getOppositeFace();
			f = MathUtil.straightUp(f);
		}
		else {
			return null;
		}
		final Block relative = block.getRelative(f, 2);
		if (AbstractIC.checkEligibility(relative)) {
			return ClickedSignFactory.getClickedIC(relative, ((Sign) relative.getState()).getLine(1), player);
		}
		return null;
	}



	/**
	 * Get an IC with the specific code
	 *
	 * @param block the block where to reference the IC
	 * @param signString the name of the sign as "BCXXXX"
	 * @param player the player who clicked the sign
	 * @return a Clickable IC, or null
	 */
	static final public Clickable getClickedIC(Block block, String signString, Player player) {

		if (signString.length() < 7)
			return null;

		int ICnumber = Integer.parseInt(signString.substring(3, 7));

		// then we instantiate accordingly
		switch (ICnumber) {

		case 7010:
			return new BC7010(block, player);
		case 7018:
			return new BC7018(block, player);
		}

		return null;

	}
}
