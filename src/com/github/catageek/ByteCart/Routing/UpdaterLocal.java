package com.github.catageek.ByteCart.Routing;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Event.UpdaterEnterSubnetEvent;
import com.github.catageek.ByteCart.Event.UpdaterLeaveSubnetEvent;
import com.github.catageek.ByteCart.Event.UpdaterPassStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterSetStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterSetSubnetEvent;
import com.github.catageek.ByteCart.HAL.CounterInventory;
import com.github.catageek.ByteCart.HAL.StackInventory;
import com.github.catageek.ByteCart.Signs.BC8010;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Signs.HasNetmask;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

public class UpdaterLocal implements Updater {

	private final BCSign bcsign;
	private final RoutingTableExchange Routes;
	private final CounterInventory Counter;
	private final StackInventory Start;
	private final StackInventory End;
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

	protected UpdaterLocal(BCSign bc) {
		bcsign = bc;
		SignAddress = bc.getSignAddress();
		Counter = new CounterInventory(((StorageMinecart) this.getVehicle()).getInventory(), 18);
		Start = new StackInventory(((StorageMinecart) this.getVehicle()).getInventory(), 18, 5);
		End = new StackInventory(((StorageMinecart) this.getVehicle()).getInventory(), 23, 5);
		Routes = ByteCart.myPlugin.getUm().getMapRoutes().get(getVehicle().getEntityId());

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
		if(this.getRoutes().getCurrent() == -2) {
			this.getStart().push(1);
			this.getRoutes().setCurrent(0);
		}

	}

	@Override
	public void doAction(BlockFace to) {

		int signring = this.getSignAddress().getTrack().getAmount();
		// the route where we went the lesser
		int preferredroute = this.getRoutes().getMinDistance(RoutingTable, this.getFrom());


		// if we are not in the good region or on ring 0, skip update
		if (this.getSignAddress().getRegion().getAmount() != Routes.getRegion()
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
				return;
			}

		// leave subnet, resetting start and end stacks (and removing cookie A)
		this.getStart().clear();
		this.getEnd().clear();


		// incrementing ring counter in the RoutingTableExchange map
		int ring = getCurrent();
		if (this.getRoutes().hasRouteTo(ring))
			this.getRoutes().setRoute(ring, this.getRoutes().getDistance(ring) + 1);
		else
			this.getRoutes().setRoute(ring, 1);

		// updating region and counter data from sign
		this.getCounter().setCount(counterSlot.REGION.slot, this.getSignAddress().getRegion().getAmount());
		this.getCounter().setCount(counterSlot.RING.slot, this.getSignAddress().getTrack().getAmount());

		// mark station 0 as taken
		this.getCounter().incrementCount(0, 32);

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : UpdaterLocal : updating cart from sign\n" + this.getCounter());

	}

	@Override
	public void doAction(Side to) {

		if (this.getNetmask() == 4) {
			//it's a station, launch event
			UpdaterPassStationEvent event = new UpdaterPassStationEvent(this, this.getSignAddress());
			Bukkit.getServer().getPluginManager().callEvent(event);
		}

		// cookie still there or we did not enter the subnet
		if (this.getStart().empty() ^ this.getEnd().empty()
				|| (to.Value() != Side.RIGHT.Value() && this.getNetmask() < 4))
			return;

		int length = 16 >> this.getNetmask();

				// if sign is not consistent, rewrite it
				if (! getSignAddress().isValid() || this.needUpdate()) {
					Address old = this.getSignAddress();
					int start;
					if ((start = this.getFreeSubnet(getNetmask())) != -1) {
						String address = buildAddress(start);
						this.getSignAddress().setAddress(address);

						// reload sign
						Address reloadAddress = AddressFactory.getAddress(address);
						this.setSignAddress(reloadAddress);

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


				if (getSignAddress().isValid()) {
					int stationfield = this.getSignAddress().getStation().getAmount();
					if (length != 1) {
						// general case
						// if we go out from the current subnet and possibly entering a new one
						if (! this.isInSubnet(stationfield, this.getNetmask()))
							leaveSubnet();

						// register new subnet start and mask
						this.getStart().push(stationfield);
						this.getEnd().push(stationfield + length);
						// launch event
						UpdaterEnterSubnetEvent event = new UpdaterEnterSubnetEvent(this, getSignAddress(), length);
						Bukkit.getServer().getPluginManager().callEvent(event);
					}
					else
						// case of stations
						this.getCounter().incrementCount(stationfield, 64);
				}
	}

	@Override
	public Side giveSimpleDirection() {


		// turn if it's not a station, and the ring is initialized or the address is invalid 
		if (this.getNetmask() < 4
				&& (! (this.getStart().empty() ^ this.getEnd().empty())))
			return Side.RIGHT;

		return Side.LEFT;
	}

	@Override
	public BlockFace giveRouterDirection() {
		// check if we are in the good region
		if (this.getSignAddress().isValid()
				&& this.getSignAddress().getRegion().getAmount() != Routes.getRegion()) {
			// case this is not the right region
			try {
				return RoutingTable.getDirection(0).getBlockFace();
			} catch (NullPointerException e) {
				// no route to 0
				return this.getFrom().getBlockFace();
			}
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
				if (this.getRoutes().hasRouteTo(ring))
					this.getRoutes().setRoute(ring, this.getRoutes().getDistance(ring) + 1);
				else
					this.getRoutes().setRoute(ring, 1);

				// reset counters
				this.getCounter().resetAll();
				//clear stacks and set cookie B
				this.getStart().clear();
				this.getEnd().clear();
				this.getStart().push(0);

				// the route where we went the lesser
				int preferredroute = this.getRoutes().getMinDistance(RoutingTable, getFrom());

				try {
					return RoutingTable.getDirection(preferredroute).getBlockFace();
				} catch (NullPointerException e) {
					// no route to ring
					return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
				}

			}
		}
		return this.getFrom().getBlockFace();
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
		int end = (this.getEnd().empty()) ? 16 : this.getEnd().peek();
		int step = 16 >> netmask;
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


	private final RoutingTableExchange getRoutes() {
		return Routes;
	}


	private final CounterInventory getCounter() {
		return Counter;
	}


	protected final int getNetmask() {
		return SignNetmask;
	}

	private final int getNext() {
		if (this.getEnd().empty())
			return 16;
		return this.getEnd().peek();
	}

	private final int getCurrentSubnet() {
		if (this.getStart().empty())
			return 0;
		return this.getStart().peek();
	}

	private final boolean isInSubnet(int address, int netmask) {
		return (address >= this.getCurrentSubnet() && (address | (15 >> netmask))  < this.getNext());
	}

	private final boolean needUpdate() {
		return getSignAddress().getRegion().getAmount() != this.getCounter().getCount(counterSlot.REGION.slot)
				|| getSignAddress().getTrack().getAmount() != this.getCounter().getCount(counterSlot.RING.slot)
				|| ! isInSubnet(getSignAddress().getStation().getAmount(), this.getNetmask());
	}


	public final DirectionRegistry getFrom() {
		return From;
	}


	private final StackInventory getStart() {
		return Start;
	}


	private final StackInventory getEnd() {
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
	public final int getRegion() {
		return this.getSignAddress().getRegion().getAmount();
	}

	@Override
	public int getTrackNumber() {
		return this.getSignAddress().getTrack().getAmount();
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

}
