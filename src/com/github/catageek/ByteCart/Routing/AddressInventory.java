package com.github.catageek.ByteCart.Routing;



/* 
 * This class represents an address stored in an inventory
 */
public final class AddressInventory extends AbstractAddressInventory implements AddressRouted {

	public AddressInventory(org.bukkit.inventory.Inventory inv) {
		super(inv);
	}

	@Override
	protected int getRegionSlot() {
		return Slots.REGION.getSlot();
	}

	@Override
	protected int getTrackSlot() {
		return Slots.TRACK.getSlot();
	}

	@Override
	protected int getStationSlot() {
		return Slots.STATION.getSlot();
	}

	@Override
	protected int getIsTrainSlot() {
		return Slots.ISTRAIN.getSlot();
	}

	@Override
	protected void setRegion(int region) {
		protectReturnAddress();
		super.setRegion(region);
	}

	@Override
	protected void setTrack(int track) {
		protectReturnAddress();
		super.setTrack(track);
	}

	@Override
	protected void setStation(int station) {
		protectReturnAddress();
		super.setStation(station);
	}


}
