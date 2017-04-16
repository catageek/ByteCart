package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Vehicle;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Rails;

import com.github.catageek.ByteCart.HAL.AbstractIC;


/**
 * This class contains the method to instantiate any IC
 */
final public class TriggeredSignFactory {

	/**
	 * Check the sign and instantiate the IC object, or null
	 * 
	 * @param block the sign block
	 * @param vehicle the vehicle that triggered the sign
	 * @return a Triggable representing the IC
	 * @throws ClassNotFoundException
	 * @throws IndexOutOfBoundsException
	 * @throws IOException
	 */
	static final public Triggable getTriggeredIC(Block block, Vehicle vehicle) throws ClassNotFoundException, IndexOutOfBoundsException, IOException {

		if(AbstractIC.checkEligibility(block)) {

			// if there is really a BC sign post
			// we extract its #

			return TriggeredSignFactory.getTriggeredIC(block, ((Sign) block.getState()).getLine(1), vehicle);
		}

		// Maybe the rail is in slope
		Block block2 = block.getRelative(BlockFace.DOWN);
		if (AbstractIC.checkEligibility(block2)) {
			MaterialData rail = block.getRelative(BlockFace.UP).getState().getData();
			if (rail instanceof Rails && ((Rails) rail).isOnSlope())
				return TriggeredSignFactory.getTriggeredIC(block2, ((Sign) block2.getState()).getLine(1), vehicle);
		}
		// no BC sign post

		return null;
	}

	/**
	 * Read the string and instantiate the IC object, or null
	 * 
	 * The string must be checked before calling this method
	 *
	 * @param block the sign block
	 * @param signString the string containing the IC number
	 * @param vehicle the vehicle triggering the sign
	 * @return a Triggable representing the IC
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	static final public Triggable getTriggeredIC(Block block, String signString, Vehicle vehicle) throws ClassNotFoundException, IOException {

		if (signString.length() < 7)
			return null;

		int ICnumber;
		try {
			ICnumber = Integer.parseInt(signString.substring(3, 7));

			// then we instantiate accordingly
			switch (ICnumber) {

			case 7000:
			case 7001:
				return (new BC7001(block, vehicle));
			case 7002:
				return (new BC7002(block, vehicle));
			case 7003:
				return (new BC7003(block));
			case 7005:
				return (new BC7005(block, vehicle));
			case 7006:
				return (new BC7006(block, vehicle));
			case 7007:
				return (new BC7007(block, vehicle));
			case 7008:
				return (new BC7008(block, vehicle));
			case 7009:
				return (new BC7009(block, vehicle));
			case 7010:
				return (new BC7010(block, vehicle));
			case 7011:
				return (new BC7011(block, vehicle));
			case 7012:
				return (new BC7012(block, vehicle));
			case 7013:
				return (new BC7013(block, vehicle));
			case 7014:
				return (new BC7014(block, vehicle));
			case 7015:
				return (new BC7015(block, vehicle));
			case 7016:
				return (new BC7016(block, vehicle));
			case 7017:
				return (new BC7017(block, vehicle));
			case 7018:
				return (new BC7018(block, vehicle));
			case 7019:
				return (new BC7019(block, vehicle));
			case 7020:
				return (new BC7020(block, vehicle));
			case 7021:
				return (new BC7021(block, vehicle));

			case 8010:
				return (new BC8010(block, vehicle));
			case 8011:
				return (new BC8011(block, vehicle));

			case 8020:
				return (new BC8020(block, vehicle));
			case 8021:
				return (new BC8021(block, vehicle));

			case 9000:
				return (new BC9000(block, vehicle));
			case 9001:
				return (new BC9001(block, vehicle));
			case 9002:
				return (new BC9002(block, vehicle));
			case 9004:
				return (new BC9004(block, vehicle));
			case 9008:
				return (new BC9008(block, vehicle));
			case 9016:
				return (new BC9016(block, vehicle));
			case 9032:
				return (new BC9032(block, vehicle));
			case 9064:
				return (new BC9064(block, vehicle));
			case 9128:
				return (new BC9128(block, vehicle));
			case 9037:
				return (new BC9037(block, vehicle));
			case 9137:
				return (new BC9137(block, vehicle));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
}
