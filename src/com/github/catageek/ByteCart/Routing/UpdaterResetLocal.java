package com.github.catageek.ByteCart.Routing;

import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.Event.UpdaterClearStationEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterClearSubnetEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterSignInvalidateEvent;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;

final class UpdaterResetLocal extends UpdaterLocal implements Wanderer {

	UpdaterResetLocal(BCSign bc, UpdaterContent rte) {
		super(bc, rte);
	}

	@Override
	public void doAction(BlockFace to) {
		int ring;
		if ((ring = this.getTrackNumber()) != -1) {
			incrementRingCounter(ring);
		}
		this.getEnd().clear();
		// save the region number
		this.getContent().setCurrent(this.getSignAddress().getRegion().getAmount());
		save();
	}

	@Override
	public void doAction(Side to) {
		Address address = this.getSignAddress();

		// Keep track on the subring level we are in
		int mask = this.getNetmask();
		if (mask < 8) {
			Stack<Integer> end = this.getEnd();
			if (to.equals(Side.LEVER_ON) && (end.isEmpty() || mask > end.peek())) {
				end.push(mask);
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : pushing mask " + mask + " on stack");
			}
			else
				if (to.equals(Side.LEVER_OFF) && ! end.isEmpty()) {
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : popping mask " + end.peek() + " from stack");

					end.pop();
				}
		}
		save();

		// if we are not in the good region, skip update
		if (getContent().getCurrent() != getContent().getRegion())
			return;

		if (address.isValid() && this.getContent().isFullreset()) {
			if (this.getNetmask() == 8) {
				UpdaterClearStationEvent event = new UpdaterClearStationEvent(this, address);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
			else {
				UpdaterClearSubnetEvent event = new UpdaterClearSubnetEvent(this, address, 256 >> this.getNetmask());
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}
		else {
			UpdaterSignInvalidateEvent event = new UpdaterSignInvalidateEvent(this);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (! this.getContent().isFullreset())
				return;
		}
		address.remove();
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: removing address");

	}

	@Override
	public IntersectionSide.Side giveSimpleDirection() {
		int mask = this.getNetmask();
		Stack<Integer> end = this.getEnd();
		if ( mask < 8 && (end.isEmpty() || mask > end.peek()))
			return IntersectionSide.Side.LEVER_ON;
		return IntersectionSide.Side.LEVER_OFF;
	}

	@Override
	public BlockFace giveRouterDirection() {
		// check if we are in the good region
		if (this.getSignAddress().isValid()
				&& this.getSignAddress().getRegion().getAmount() != getWandererRegion()) {
			// case this is not the right region
			DirectionRegistry dir = RoutingTable.getDirection(getWandererRegion());
			if (dir != null)
				return dir.getBlockFace();
			return this.getFrom().getBlockFace();
		}
		// the route where we went the lesser
		int preferredroute = this.getContent().getMinDistanceRing(RoutingTable, getFrom());
		DirectionRegistry dir;
		if ((dir = RoutingTable.getDirection(preferredroute)) != null)
			return dir.getBlockFace();
		return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
	}
}
