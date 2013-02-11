package com.github.catageek.ByteCart.Routing;

import com.github.catageek.ByteCart.ByteCart;

final class ReturnAddressInventory extends AbstractAddressInventory implements
Returnable {

	public ReturnAddressInventory(org.bukkit.inventory.Inventory inv) {
		super(inv);
	}

	@Override
	public void setRegion(int region) {
		initWrite();
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Writing region " + region);
		super.setRegion(region);
	}


	@Override
	public void setTrack(int track) {
		initWrite();
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Writing track " + track);
		super.setTrack(track);
	}


	@Override
	public void setStation(int station) {
		initWrite();
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Writing station " + station);
		super.setStation(station);
	}

	private void initWrite() {
		this.protectDestinationAddress();
		this.setIsReturnable(true);
//		this.UpdateAddress();
	}

	@Override
	protected int getRegionSlot() {
		return Slots.RETURN_REGION.getSlot();
	}

	@Override
	protected int getTrackSlot() {
		return Slots.RETURN_TRACK.getSlot();
	}

	@Override
	protected int getStationSlot() {
		return Slots.RETURN_STATION.getSlot();
	}

	@Override
	protected int getIsTrainSlot() {
		return Slots.RETURN_ISTRAIN.getSlot();
	}

	@Override
	public void remove() {
		super.remove();
		this.setIsReturnable(false);
		this.UpdateAddress();
	}
}
