package com.github.catageek.ByteCart.Routing;

import java.util.Stack;

import org.bukkit.Bukkit;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.Util.LogUtil;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.Event.UpdaterEnterSubnetEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterLeaveSubnetEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterPassStationEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterSetStationEvent;
import com.github.catageek.ByteCartAPI.Event.UpdaterSetSubnetEvent;
import com.github.catageek.ByteCartAPI.Routing.Updater;
import com.github.catageek.ByteCartAPI.Signs.BCSign;

public class UpdaterLocal extends DefaultLocalWanderer implements Updater {

	protected UpdaterLocal(BCSign bc, UpdaterContent rte) {
		super(bc, rte);
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
		if(to.Value() != Side.LEVER_ON.Value() && this.getNetmask() < 8) {
			// if we have the same sign as when entering the subnet, close the subnet
			if (this.isExactSubnet((start = this.getFirstStationNumber()), this.getNetmask())) {
				this.getSignAddress().setAddress(buildAddress(start));
				this.getSignAddress().finalizeAddress();
				this.getContent().updateTimestamp();
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
				Stack<Integer> startstack = this.getStart();
				Stack<Integer> endstack = this.getEnd();
				int oldstart = getFirstStationNumber();
				int oldend = getLastStationNumber();
				startstack.push(stationfield);
				endstack.push(stationfield + length);
				// launch event
				UpdaterEnterSubnetEvent event = new UpdaterEnterSubnetEvent(this, getSignAddress(), length,
						AddressFactory.getAddress(buildAddress(oldstart)), oldend - oldstart);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}
		else
			// case of stations
			if (stationfield != -1)
				this.getCounter().incrementCount(stationfield, 64);

		save();
	}

	public final void leaveSubnet() {
		super.leaveSubnet();
		if(!this.getStart().empty() && ! this.getEnd().empty()) {
			Stack<Integer> startstack = this.getStart();
			Stack<Integer> endstack = this.getEnd();
			int start = startstack.pop();
			int end = endstack.pop();
			int newstart = getFirstStationNumber();
			int newend = getLastStationNumber();
			// launch event
			UpdaterLeaveSubnetEvent event = new UpdaterLeaveSubnetEvent(this, AddressFactory.getAddress(buildAddress(start)), end - start
					, AddressFactory.getAddress(buildAddress(newstart)), newend - newstart);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	private final int getFreeSubnet(int netmask) {
		boolean free;
		int start = getFirstStationNumber();
		int end = getLastStationNumber();
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

	private String buildAddress(int start) {
		String address = "" + this.getCounter().getCount(counterSlot.REGION.slot)
				+ "." + getCurrent()
				+ "." + start;
		return address;
	}


	private final boolean isInSubnet(int address, int netmask) {
		return (address >= this.getFirstStationNumber() && (address | (255 >> netmask))  < this.getLastStationNumber());
	}

	private final boolean needUpdate() {
		return getSignAddress().getRegion().getAmount() != this.getCounter().getCount(counterSlot.REGION.slot)
				|| getSignAddress().getTrack().getAmount() != this.getCounter().getCount(counterSlot.RING.slot)
				|| ! isInSubnet(getSignAddress().getStation().getAmount(), this.getNetmask());
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
}
