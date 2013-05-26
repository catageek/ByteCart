package com.github.catageek.ByteCart.Routing;

import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Routing.Updater.Scope;
import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

/**
 * 
 * This class represents a Wanderer. All wanderers must extends this class.
 * Wanderers implementors would prefer to override an existing default Wanderer implementation
 * such as DefaultRouterWanderer.
 *
 */
abstract class AbstractWanderer {

	public abstract BlockFace giveRouterDirection();
	public abstract Side giveSimpleDirection();
	public abstract void doAction(BlockFace To);
	public abstract void doAction(Side To);

	private final BCSign bcsign;
	private final Address SignAddress;
	private final DirectionRegistry From;
	private final boolean IsTrackNumberProvider;
	private final int Region;
	private final RoutingTable RoutingTable;

	protected AbstractWanderer(BCSign bc, int region) {
		bcsign = bc;
		SignAddress = bc.getSignAddress();
		Region = region;

		if (bc instanceof BC8010) {
			BC8010 ic = (BC8010) bc;
			From = new DirectionRegistry(ic.getFrom());
			IsTrackNumberProvider = ic.isTrackNumberProvider();
			RoutingTable = ic.getRoutingTable();
		}
		else {
			From = null;
			IsTrackNumberProvider = false;
			RoutingTable = null;
		}
	}

	protected final boolean isAtBorder() {

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Updater : cart region " + getWandererRegion() + ", router level " + this.getSignLevel().number);

		if (this.getWandererRegion() == 0 ^ this.getSignLevel().scope.equals(Scope.BACKBONE))	{
			// if we are at the border of the region
			// going back

			return true;
		}

		return false;
	}

	public final BlockFace manageBorder() {
		if ((isAtBorder())) {
			DirectionRegistry dir;
			if ((dir = this.getRoutingTable().getDirection(this.getWandererRegion())) != null)
				return dir.getBlockFace();
			return getFrom().getBlockFace();
		}
		return null;
	}

	public static final BlockFace getRandomBlockFace(RoutingTable routingtable, BlockFace from) {

		// selecting a random destination avoiding ring 0 or where we come from
		DirectionRegistry direction = new DirectionRegistry(1 << (new Random()).nextInt(4));

		while (direction.getBlockFace() == from || routingtable.isDirectlyConnected(0, direction)) {
			direction.setAmount(1 << (new Random()).nextInt(4));
		}

		return direction.getBlockFace();
	}

	protected final RoutingTable getRoutingTable() {
		return RoutingTable;
	}

	protected final Address getSignAddress() {
		return SignAddress;
	}

	public final DirectionRegistry getFrom() {
		return From;
	}

	public final Level getSignLevel() {
		return this.getBcSign().getLevel();
	}

	public final Vehicle getVehicle() {
		return this.getBcSign().getVehicle();
	}

	protected final boolean isTrackNumberProvider() {
		return IsTrackNumberProvider;
	}

	protected final boolean isSameTrack(BlockFace To) {
		return getFrom().getBlockFace().equals(To);
	}

	public final int getWandererRegion() {
		return Region;
	}

	public final Block getCenter() {
		return this.getBcSign().getCenter();
	}

	public final String getFriendlyName() {
		return this.getBcSign().getFriendlyName();
	}
	/**
	 * @return the bcsign
	 */
	public BCSign getBcSign() {
		return bcsign;
	}


}