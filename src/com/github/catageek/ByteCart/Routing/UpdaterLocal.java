package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.util.Random;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Event.UpdaterEnterSubnetEvent;
import com.github.catageek.ByteCart.Event.UpdaterLeaveSubnetEvent;
import com.github.catageek.ByteCart.Event.UpdaterPassStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterSetStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterSetSubnetEvent;
import com.github.catageek.ByteCart.HAL.Counter;
import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Signs.HasNetmask;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.LogUtil;

public class UpdaterLocal implements Updater {

	private final BCSign bcsign;
	private final LocalUpdaterContent content;
	private final Counter Counter;
	private final Stack<Integer> Start;
	private final Stack<Integer> End;
	private final int SignNetmask;
	private DirectionRegistry From = null;
	protected RoutingTable RoutingTable = null;
	private Address SignAddress;

	private enum counterSlot {
		REGION(16),
		RING(17);

		private final int slot;

		private counterSlot(int i) {
			this.slot = i;
		}
	}

	protected UpdaterLocal(BCSign bc, UpdaterContent rte) {
		bcsign = bc;
		content = (LocalUpdaterContent) rte;
		SignAddress = bc.getSignAddress();
		Counter = content.getCounter();
		//		Start = new StackInventory(((StorageMinecart) this.getVehicle()).getInventory(), 18, 5);
		//		End = new StackInventory(((StorageMinecart) this.getVehicle()).getInventory(), 23, 5);
		Start = content.getStart();
		End = content.getEnd();


		if (bc instanceof BC8010) {
			BC8010 ic = (BC8010) bc;
			From = new DirectionRegistry(ic.getFrom());
			RoutingTable = ic.getRoutingTable();
		}

		if (bc instanceof HasNetmask)
			SignNetmask = ((HasNetmask)bc).getNetmask();
		else
			SignNetmask = 0;

		// set cookie A to wait a router
		if(this.getContent().getCurrent() == -2) {
			this.getStart().push(1);
			this.getContent().setCurrent(0);
			save();
		}

	}

	public void save() {
		try {
			UpdaterContentFactory.<LocalUpdaterContent>saveRoutingTableExchange(getContent());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doAction(BlockFace to) {

		int signring = this.getSignAddress().getTrack().getAmount();
		// the route where we went the lesser
		int preferredroute = this.getContent().getMinDistanceRing(RoutingTable, this.getFrom());


		// if we are not in the good region or on ring 0, skip update
		if (this.getSignAddress().getRegion().getAmount() != getContent().getRegion()
				|| signring == 0)
			return;

		// if this is cookie A, do nothing if this is not the route where we want to go
		if (this.getEnd().empty() ^ this.getStart().empty()
				&& this.getStart().peek() != 0) {
			preferredroute = this.getStart().peek();
			if (preferredroute != signring)
				return;
		}

		// mark all stations of subnets in stack as taken
		while (! this.getEnd().empty())
			this.leaveSubnet();

		//if cookie B is present, leave cookie A and return
		if (this.getEnd().empty() ^ this.getStart().empty())
			if (this.getStart().pop() == 0) {
				// Pushing the route where we want to go
				this.getStart().push(preferredroute);
				save();
				return;
			}

		// leave subnet, resetting start and end stacks (and removing cookie A)
		this.getStart().clear();
		this.getEnd().clear();

		// updating region and counter data from sign
		this.getCounter().setCount(counterSlot.REGION.slot, this.getSignAddress().getRegion().getAmount());
		this.getCounter().setCount(counterSlot.RING.slot, this.getSignAddress().getTrack().getAmount());

		// mark station 0 as taken
		this.getCounter().incrementCount(0, 32);

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : UpdaterLocal : updating cart from sign\n" + this.getCounter());

		save();

	}

	@Override
	public void doAction(Side to) {

		if (this.getNetmask() == 8) {
			//it's a station, launch event
			UpdaterPassStationEvent event = new UpdaterPassStationEvent(this, this.getSignAddress());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}

		// cookie still there
		if (this.getStart().empty() ^ this.getEnd().empty())
			return;

		// we did not enter the subnet
		int start;
		if(to.Value() != Side.RIGHT.Value() && this.getNetmask() < 8) {
			// if we have the same sign as when entering the subnet, close the subnet
			if (this.isExactSubnet((start = this.getCurrentSubnet()), this.getNetmask())) {
				if (! this.getSignAddress().isValid()) {
					this.getSignAddress().setAddress(buildAddress(start));
					this.getSignAddress().finalizeAddress();
					this.getContent().updateTimestamp();
				}
				this.leaveSubnet();
				this.save();
			}
			return;
		}

		int length = (256 >> this.getNetmask());
		// if sign is not consistent, rewrite it
		if (! getSignAddress().isValid() || this.needUpdate()) {
			Address old = this.getSignAddress();
			if ((start = this.getFreeSubnet(getNetmask())) != -1) {
				String address = buildAddress(start);
				this.getSignAddress().setAddress(address);
				this.getSignAddress().finalizeAddress();

				// reload sign
				Address reloadAddress = AddressFactory.getAddress(address);
				this.setSignAddress(reloadAddress);

				this.getContent().updateTimestamp();

				// launch event
				if (length > 1) {
					UpdaterSetSubnetEvent event = new UpdaterSetSubnetEvent(this, old, reloadAddress, length);
					Bukkit.getServer().getPluginManager().callEvent(event);
				}
				else {
					UpdaterSetStationEvent event = new UpdaterSetStationEvent(this, old, reloadAddress);
					Bukkit.getServer().getPluginManager().callEvent(event);
				}

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : UpdaterLocal : Update() : rewrite sign to " + address + "(" + this.getSignAddress().toString() + ")");
			}
		}

		int stationfield = -1;
		if (getSignAddress().isValid())
			stationfield = this.getSignAddress().getStation().getAmount();

		if (length != 1) {
			// general case
			// if we go out from the current subnet and possibly entering a new one
			if (! this.isInSubnet(stationfield, this.getNetmask()))
				leaveSubnet();

			if (stationfield != -1) {
				// register new subnet start and mask
				this.getStart().push(stationfield);
				this.getEnd().push(stationfield + length);
				// launch event
				UpdaterEnterSubnetEvent event = new UpdaterEnterSubnetEvent(this, getSignAddress(), length);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}
		else
			// case of stations
			if (stationfield != -1)
				this.getCounter().incrementCount(stationfield, 64);

		save();
	}

	@Override
	public Side giveSimpleDirection() {


		// turn if it's not a station, and the ring is initialized or the address is invalid
		// and the subnet is contained in the current borders
		if (this.getNetmask() < 8
				&& (! (this.getStart().empty() ^ this.getEnd().empty()))
				&& ! this.isExactSubnet(this.getCurrentSubnet(), this.getNetmask()))
			return Side.RIGHT;

		return Side.LEFT;
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

		// there is a cookie (so it is cookie A) or it's a reset cart
		if (this.getStart().empty() ^ this.getEnd().empty()) {
			int signring = this.getSignAddress().getTrack().getAmount();
			int preferredroute = this.getStart().peek();

			// if we are not arrived yet or in ring 0, we continue
			if (signring == 0 || signring != preferredroute)
				try {
					if(ByteCart.debug)
						ByteCart.log.info("not arrived, signaddress = " + signring);
					return RoutingTable.getDirection(preferredroute).getBlockFace();
				} catch (NullPointerException e) {
					// no route to ring
					return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
				}
		}
		else {
			// no cookie
			// check counter
			if (this.getCounter().isAllFull(0, 15)) {
				// we configured all stations
				// incrementing ring counter in the RoutingTableExchange map
				int ring = this.getCounter().getCount(counterSlot.RING.slot);
				incrementRingCounter(ring);

				// reset counters
				this.getCounter().resetAll();
				//clear stacks and set cookie B
				this.getStart().clear();
				this.getEnd().clear();
				this.getStart().push(0);

				save();

				// the route where we went the lesser
				int preferredroute = this.getContent().getMinDistanceRing(RoutingTable, getFrom());
				DirectionRegistry dir;
				if ((dir = RoutingTable.getDirection(preferredroute)) != null)
					return dir.getBlockFace();
				return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
			}
		}
		return this.getFrom().getBlockFace();
	}

	protected void incrementRingCounter(int ring) {
		if (this.getContent().hasRouteTo(ring))
			this.getContent().setRoute(ring
					, new Metric(this.getContent().getMetric(ring) + (new Random()).nextInt(3) + 1));
		else
			this.getContent().setRoute(ring, new Metric(1));
	}

	public final void leaveSubnet() {
		if(!this.getStart().empty() && ! this.getEnd().empty()) {
			this.fillSubnet();
			int start = this.getStart().pop();
			int end = this.getEnd().pop();
			// launch event
			UpdaterLeaveSubnetEvent event = new UpdaterLeaveSubnetEvent(this, AddressFactory.getAddress(buildAddress(start)), end - start);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	private final int getFreeSubnet(int netmask) {
		boolean free;
		int start = (this.getStart().empty() ? 0 : this.getStart().peek());
		int end = (this.getEnd().empty()) ? 256 : this.getEnd().peek();
		int step = 256 >> netmask;
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : getFreeSubnet() : start = "
					+ start + " end " + end + " step = " + step + "\n" + this.getCounter().toString());
		for (int i = start; i < end; i += step) {
			free = true;
			for (int j = i; j < i + step; j++) {
				free &= (this.getCounter().getCount(j) == 0);
			}
			if (free) {
				if(ByteCart.debug)
					ByteCart.log.info("ByteCart : getFreeSubnet() : testing : " + i + " : " + free);
				return i;
			}
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : getFreeSubnet() : testing : " + i + " : " + free);
		}
		LogUtil.sendError(this.getContent().getPlayer(), "Sign at "  + this.getCenter().getLocation().toString()
				+ "could not get an address because address pool is empty." +
				" Maximum station numbers is reached on the " + step + "-station subnet " + this.buildAddress(start));
		return -1;
	}

	private final void fillSubnet() {
		int start = this.getCurrentSubnet();
		int end = this.getNext();
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : UpdaterLocal : fill start " + start + " end " + end);
		for (int i = start; i < end; i++)
			this.getCounter().incrementCount(i, 64);
	}

	private String buildAddress(int start) {
		String address = "" + this.getCounter().getCount(counterSlot.REGION.slot)
				+ "." + getCurrent()
				+ "." + start;
		return address;
	}

	public final Vehicle getVehicle() {
		return this.getBcSign().getVehicle();
	}

	protected final Address getSignAddress() {
		return SignAddress;
	}


	protected final LocalUpdaterContent getContent() {
		return content;
	}


	private final Counter getCounter() {
		return Counter;
	}


	protected final int getNetmask() {
		return SignNetmask;
	}

	private final int getNext() {
		if (this.getEnd().empty())
			return 256;
		return this.getEnd().peek();
	}

	private final int getCurrentSubnet() {
		if (this.getStart().empty())
			return 0;
		return this.getStart().peek();
	}

	private final boolean isInSubnet(int address, int netmask) {
		return (address >= this.getCurrentSubnet() && (address | (255 >> netmask))  < this.getNext());
	}

	private final boolean isExactSubnet(int address, int netmask) {
		return (address == this.getCurrentSubnet() && (address | (255 >> netmask))  == (this.getNext() - 1));
	}

	private final boolean needUpdate() {
		return getSignAddress().getRegion().getAmount() != this.getCounter().getCount(counterSlot.REGION.slot)
				|| getSignAddress().getTrack().getAmount() != this.getCounter().getCount(counterSlot.RING.slot)
				|| ! isInSubnet(getSignAddress().getStation().getAmount(), this.getNetmask());
	}


	public final DirectionRegistry getFrom() {
		return From;
	}


	private final Stack<Integer> getStart() {
		return Start;
	}


	private final Stack<Integer> getEnd() {
		return End;
	}

	private final void setSignAddress(Address signAddress) {
		SignAddress = signAddress;
	}

	private int getCurrent() {
		return this.getCounter().getCount(counterSlot.RING.slot);
	}

	@Override
	public final Level getLevel() {
		return Level.LOCAL;
	}

	@Override
	public int getTrackNumber() {
		Address address;
		if ((address = this.getSignAddress()).isValid())
			return address.getTrack().getAmount();
		return -1;
	}

	@Override
	public final Block getCenter() {
		return this.getBcSign().getCenter();
	}

	/**
	 * @return the bcsign
	 */
	public final BCSign getBcSign() {
		return bcsign;
	}

	public final String getFriendlyName() {
		return this.getBcSign().getFriendlyName();
	}

	@Override
	public int getWandererRegion() {
		return this.getContent().getRegion();
	}

}
