package com.github.catageek.ByteCart.Wanderer;

import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.Routing.Updater.Level;
import com.github.catageek.ByteCartAPI.Routing.Updater.Scope;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

/**
 * 
 * This class represents a Wanderer. All wanderers must extends this class.
 * Wanderers implementors would prefer to override an existing default Wanderer implementation
 * such as DefaultRouterWanderer.
 *
 */
public abstract class AbstractWanderer {

	/**
	 * Method that must return the direction to take on a BC8XXX sign
	 * 
	 * @return the direction that the cart should take
	 */
	public abstract BlockFace giveRouterDirection();

	/**
	 * Method that must return the position of the lever
	 * 
	 * @return the position of the lever
	 */
	public abstract Side giveSimpleDirection();

	/**
	 * Method called when an updater meets a BC8XXX sign
	 * 
	 * @param To the direction where the cart goes
	 */
	public abstract void doAction(BlockFace To);

	/**
	 * Method called when an updater meets a BC9XXX sign
	 * 
	 * @param To the position of the lever
	 */
	public abstract void doAction(Side To);

	private final BCSign bcsign;
	private Address SignAddress;
	private final DirectionRegistry From;
	private final int Region;

	/**
	 * @param bc the ic that triggers this wanderer
	 * @param region the region where this wanderer is attached to
	 */
	protected AbstractWanderer(BCSign bc, int region) {
		bcsign = bc;
		SignAddress = bc.getSignAddress();
		Region = region;

		if (bc instanceof BC8010) {
			BC8010 ic = (BC8010) bc;
			From = new DirectionRegistry(ic.getFrom());
		}
		else {
			From = null;
		}
	}

	/**
	 * Tells if we are at the border of a region
	 * 
	 * @return true if we just met a backbone router, or leave the backbone
	 */
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


	/**
	 * Get a random route that is not from where we are coming
	 * 
	 * @param routingtable the routing table where to pick up a route
	 * @param from the direction from where we are coming
	 * @return the direction
	 */
	public static final BlockFace getRandomBlockFace(RoutingTable routingtable, BlockFace from) {

		// selecting a random destination avoiding ring 0 or where we come from
		DirectionRegistry direction = new DirectionRegistry(1 << (new Random()).nextInt(4));

		while (direction.getBlockFace() == from || routingtable.isDirectlyConnected(0, direction)) {
			direction.setAmount(1 << (new Random()).nextInt(4));
		}

		return direction.getBlockFace();
	}

	/**
	 * @return the address on the sign
	 */
	protected final Address getSignAddress() {
		return SignAddress;
	}
	
	/**
	 * Set the member variable SignAddress
	 * 
	 * @param signAddress
	 */
	protected final void setSignAddress(Address signAddress) {
		SignAddress = signAddress;
	}

	/**
	 * @return the direction from where we are coming
	 */
	public final DirectionRegistry getFrom() {
		return From;
	}

	/**
	 * @return the level of the sign
	 */
	public final Level getSignLevel() {
		return this.getBcSign().getLevel();
	}

	/**
	 * @return the Vehicle
	 */
	public final Vehicle getVehicle() {
		return this.getBcSign().getVehicle();
	}

	/**
	 * Tells if we are about to make a U-turn
	 * 
	 * @param To the direction we want to go
	 * @return true if we make a U-turn
	 */
	protected final boolean isSameTrack(BlockFace To) {
		return getFrom().getBlockFace().equals(To);
	}

	/**
	 * Get the region where this wanderer is attached to
	 * 
	 * @return the region number
	 */
	public final int getWandererRegion() {
		return Region;
	}

	/**
	 * Get the center of the IC that triggers this wanderer
	 * 
	 * @return the center
	 */
	public final Block getCenter() {
		return this.getBcSign().getCenter();
	}

	/**
	 * Get the name of the sign
	 * 
	 * @return the name
	 */
	public final String getFriendlyName() {
		return this.getBcSign().getFriendlyName();
	}
	/**
	 * @return the IC
	 */
	public final BCSign getBcSign() {
		return bcsign;
	}


}