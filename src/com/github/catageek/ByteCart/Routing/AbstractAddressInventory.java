package com.github.catageek.ByteCart.Routing;

import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.HAL.RegistryOutput;
import com.github.catageek.ByteCart.HAL.SubRegistry;
import com.github.catageek.ByteCart.HAL.SuperRegistry;
import com.github.catageek.ByteCart.HAL.VirtualRegistry;
import com.github.catageek.ByteCart.IO.InventorySlot;
import com.github.catageek.ByteCart.IO.InventorySlotWriter;

public abstract class AbstractAddressInventory extends AbstractAddress {

	protected final org.bukkit.inventory.Inventory Inventory;
	protected InventorySlotWriter InventoryWriter;

	protected abstract int getRegionSlot();
	protected abstract int getTrackSlot();
	protected abstract int getStationSlot();
	protected abstract int getIsTrainSlot();

	protected enum Slots {
		// #slot
		REGION(0),
		TRACK(1),
		STATION(2),
		ISTRAIN(2),
		ISRETURNABLE(2),
		TTL(3),
		RETURN_REGION(4),
		RETURN_TRACK(5),
		RETURN_STATION(6),
		RETURN_ISTRAIN(6);

		private final int Slot;

		private Slots(int slot) {
			Slot = slot;
		}

		/**
		 * @return the slot
		 */
		public int getSlot() {
			return Slot;
		}
	}

	public AbstractAddressInventory(org.bukkit.inventory.Inventory inv) {
		super();
		this.Inventory = inv;
		this.InventoryWriter = new InventorySlotWriter(this.getInventory());
	}

	/**
	 * @return the inventory
	 */
	protected org.bukkit.inventory.Inventory getInventory() {
		return Inventory;
	}

	/**
	 * @param inventory the inventory to set
	 */
	@SuppressWarnings("deprecation")
	private boolean UpdateInventory(org.bukkit.inventory.Inventory inventory) {
		if (this.InventoryWriter.isSuccess()) {
			this.Inventory.setContents(inventory.getContents());
			if (this.getInventory().getHolder() instanceof Player)
				((Player) this.getInventory().getHolder()).updateInventory();
			return true;
		}

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : UpdateInventory is not success");
		return false;
	}

	protected boolean UpdateAddress() {
		return this.UpdateInventory(this.InventoryWriter.getInventory());
	}

	@Override
	public void remove() {
		this.setRegion(0);
		this.setTrack(0);
		this.setStation(0);
	}

	public int getTTL() {
		if (this.getInventory().getHolder() instanceof Player) {
			return ByteCart.myPlugin.getConfig().getInt("TTL.value");
		}
		else
			return (new SubRegistry<RegistryBoth>(new InventorySlot(this.getInventory(), Slots.TTL.getSlot(), Offsets.TTL.getLength()), Offsets.TTL.getLength(), Offsets.TTL.getOffset())).getAmount();
	}

	public Address initializeTTL() {
		if (! (this.getInventory().getHolder() instanceof Player)) {
			this.InventoryWriter.Write(ByteCart.myPlugin.getConfig().getInt("TTL.value"), Slots.TTL.getSlot(), false);
			this.Inventory.setContents(this.InventoryWriter.getInventory().getContents());
		}
		return this;
	}

	public void updateTTL(int i) {
		if (! (this.getInventory().getHolder() instanceof Player)) {
			this.protectDestinationAddress();
			this.protectReturnAddress();
			this.InventoryWriter.Write(i, Slots.TTL.getSlot());
			this.UpdateInventory(this.InventoryWriter.getInventory());
		}
	}

	@Override
	protected void setIsTrain(boolean isTrain) {
		boolean written = this.InventoryWriter.setUnwritten(getIsTrainSlot());
		RegistryOutput station = new SubRegistry<RegistryBoth>(new InventorySlot(this.InventoryWriter.getInventory(), getIsTrainSlot()), Offsets.STATION.getLength(), Offsets.STATION.getOffset());
		RegistryOutput tmp = new SuperRegistry<RegistryOutput>(new VirtualRegistry(2), station);
		tmp.setBit(0, isTrain);
		this.InventoryWriter.Write(tmp.getAmount(), getIsTrainSlot());
		if (written)
			this.InventoryWriter.setWritten(getIsTrainSlot());
	}

	protected void setIsReturnable(boolean isreturnable) {
		boolean written = this.InventoryWriter.setUnwritten(getIsReturnableSlot());
		RegistryOutput station = new SubRegistry<RegistryBoth>(new InventorySlot(this.InventoryWriter.getInventory(), getIsReturnableSlot()), Offsets.STATION.getLength(), Offsets.STATION.getOffset());
		RegistryOutput tmp = new SuperRegistry<RegistryOutput>(new VirtualRegistry(2), station);
		tmp.setBit(1, isreturnable);
		this.InventoryWriter.Write(tmp.getAmount(), getIsReturnableSlot());
		if (written)
			this.InventoryWriter.setWritten(getIsReturnableSlot());
	}

	@Override
	public RegistryBoth getRegion() {
		return new SubRegistry<RegistryBoth>( new InventorySlot(this.getInventory(), getRegionSlot()), Offsets.REGION.getLength(), Offsets.REGION.getOffset());
	}

	@Override
	public RegistryBoth getTrack() {
		return new SubRegistry<RegistryBoth>( new InventorySlot(this.getInventory(), getTrackSlot()), Offsets.TRACK.getLength(), Offsets.TRACK.getOffset());
	}

	@Override
	public RegistryBoth getStation() {
		return new SubRegistry<RegistryBoth>( new InventorySlot(this.getInventory(), getStationSlot()), Offsets.STATION.getLength(), Offsets.STATION.getOffset());
	}

	@Override
	public boolean isTrain() {
		return (new SubRegistry<RegistryBoth>(new InventorySlot(this.getInventory(), getIsTrainSlot()), Offsets.ISTRAIN.getLength(), Offsets.ISTRAIN.getOffset())).getBit(0);
	}

	public boolean isReturnable() {
		return (new SubRegistry<RegistryBoth>(new InventorySlot(this.getInventory(), getIsReturnableSlot()), Offsets.ISRETURNABLE.getLength(), Offsets.ISRETURNABLE.getOffset())).getBit(0);
	}

	@Override
	protected void setRegion(int region) {
		this.InventoryWriter.Write(region, getRegionSlot());
	}

	@Override
	protected void setTrack(int track) {
		this.InventoryWriter.Write(track, getTrackSlot());
	}

	@Override
	protected void setStation(int station) {
		this.InventoryWriter.Write(station, getStationSlot());
	}

	private int getIsReturnableSlot() {
		return Slots.ISRETURNABLE.getSlot();
	}

	protected final void protectReturnAddress() {
		if(this.isReturnable())
			this.InventoryWriter.setWritten(Slots.RETURN_REGION.getSlot()).setWritten(Slots.RETURN_TRACK.getSlot()).setWritten(Slots.RETURN_STATION.getSlot());
	}
	
	protected final void protectDestinationAddress() {
			this.InventoryWriter.setWritten(Slots.REGION.getSlot()).setWritten(Slots.TRACK.getSlot()).setWritten(Slots.STATION.getSlot());
	}
}