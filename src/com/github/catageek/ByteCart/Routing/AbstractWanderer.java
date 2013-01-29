package com.github.catageek.ByteCart.Routing;

import java.util.Random;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

/**
 * @author catageek
 * 
 * This class represents a Wanderer. All wanderers must extends this class.
 * Wanderers implementors would prefer to override an existing default Wanderer implementation
 * such as DefaultRouterWanderer.
 *
 */
public abstract class AbstractWanderer {

	public abstract BlockFace giveRouterDirection();
	public abstract Side giveSimpleDirection();
	public abstract void doAction(BlockFace To);
	public abstract void doAction(Side To);

	private final Vehicle Vehicle;
	private final Address SignAddress;
	private final DirectionRegistry From;
	private final Level Level;
	private final boolean IsTrackNumberProvider;
	private final int Region;
	private final RoutingTable RoutingTable;

	protected AbstractWanderer(BCSign bc) {
		Vehicle = bc.getVehicle();
		SignAddress = bc.getSignAddress();
		Level = bc.getLevel();
		Region = ByteCart.myPlugin.getWm().getRegion(Vehicle.getEntityId());
		
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
			ByteCart.log.info("ByteCart : Updater : cart level " + getRegion() + ", router level " + this.getLevel().number);
	
		if ((this.getRegion() == 0) ^ (this.getLevel().number == com.github.catageek.ByteCart.Routing.Updater.Level.BACKBONE.number))	{
			// if we are at the border of the region
			// going back
	
			return true;
		}
	
		return false;
	}
	
	public static final BlockFace getRandomBlockFace(RoutingTable routingtable, BlockFace from) {
		
		// selecting a random destination avoiding ring 0 or where we come from
		boolean zerodistance = routingtable.getDistance(0) == 0;
		int zerodirection = (routingtable.getDirection(0) != null ? routingtable.getDirection(0).getAmount() : -1);
	
		DirectionRegistry direction = new DirectionRegistry(1 << (new Random()).nextInt(4));
	
		while (direction.getBlockFace() == from || (zerodistance && (zerodirection == direction.getAmount()))) {
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

	protected final DirectionRegistry getFrom() {
		return From;
	}

	protected final Level getLevel() {
		return Level;
	}

	protected final Vehicle getVehicle() {
		return Vehicle;
	}

	protected final boolean isTrackNumberProvider() {
		return IsTrackNumberProvider;
	}

	protected final boolean isSameTrack(BlockFace To) {
		return getFrom().getBlockFace().equals(To);
	}

	protected final int getRegion() {
		return Region;
	}

}