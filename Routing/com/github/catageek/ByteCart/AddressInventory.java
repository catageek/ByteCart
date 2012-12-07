package com.github.catageek.ByteCart;

import org.bukkit.entity.Player;



/* 
 * This class represents an address stored in an inventory
 */
public final class AddressInventory extends AbstractAddress implements AddressRouted {

	private final org.bukkit.inventory.Inventory Inventory;
	private InventoryWriter InventoryWriter;

	private enum Slots {
		// #slot, length (default : 6), pos (default : 0)
		REGION(0),
		TRACK(1),
		STATION(2, 4, 2),
		ISTRAIN(2, 1, 0),
		UNUSED(2, 1, 1),
		TTL(3);

		private final int Slot, Length, Offset;

		private Slots(int slot) {
			Slot = slot;
			Length = 6;
			Offset = 0;
		}

		private Slots(int slot, int length, int offset) {
			Slot = slot;
			Length = length;
			Offset = offset;			
		}


		/**
		 * @return the slot
		 */
		public int getSlot() {
			return Slot;
		}


		/**
		 * @return the length
		 */
		public int getLength() {
			return Length;
		}


		/**
		 * @return the offset
		 */
		public int getOffset() {
			return Offset;
		}


	}

	public AddressInventory(org.bukkit.inventory.Inventory inv) {
		this.Inventory = inv;
		this.InventoryWriter = new InventoryWriter(this.getInventory());
	}

	@Override
	public Registry getRegion() {
		return new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.REGION.getSlot()), Slots.REGION.getLength(), Slots.REGION.getOffset());
	}

	@Override
	public Registry getTrack() {
		return new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.TRACK.getSlot()), Slots.TRACK.getLength(), Slots.TRACK.getOffset());
	}

	@Override
	public Registry getStation() {
		return new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.STATION.getSlot()), Slots.STATION.getLength(), Slots.STATION.getOffset());
	}

	@Override
	public boolean isTrain() {
		return (new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.ISTRAIN.getSlot()), Slots.ISTRAIN.getLength(), Slots.ISTRAIN.getOffset())).getBit(0);
	}

	/**
	 * @return the inventory
	 */
	private org.bukkit.inventory.Inventory getInventory() {
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
	
	public boolean UpdateAddress() {
		return this.UpdateInventory(this.InventoryWriter.getInventory());
	}


	@Override
	public void setRegion(int region) {
		this.InventoryWriter.Write(region, Slots.REGION.getSlot());
	}

	@Override
	public void setTrack(int track) {
		this.InventoryWriter.Write(track, Slots.TRACK.getSlot());
	}

	@Override
	public void setStation(int station) {
		this.InventoryWriter.Write(station, Slots.STATION.getSlot());
	}

	@Override
	protected void setIsTrain(boolean isTrain) {
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : setIsTrain() : Station slot : " + new SubRegistry((Registry) new InventorySlot(this.InventoryWriter.getInventory(), Slots.STATION.getSlot()), Slots.STATION.getLength(), Slots.STATION.getOffset()).getAmount());
		
		boolean written = this.InventoryWriter.setUnwritten(Slots.STATION.getSlot());
		Registry station = new SubRegistry((Registry) new InventorySlot(this.InventoryWriter.getInventory(), Slots.STATION.getSlot()), Slots.STATION.getLength(), Slots.STATION.getOffset());
		Registry tmp = new SuperRegistry(new VirtualRegistry(2), station);
		tmp.setBit(0, isTrain);
		this.InventoryWriter.Write(tmp.getAmount(), Slots.STATION.getSlot());
		if (written)
			this.InventoryWriter.setWritten(Slots.STATION.getSlot());
	}

	@Override
	public int getTTL() {
		if (this.getInventory().getHolder() instanceof Player) {
			return ByteCart.myPlugin.getConfig().getInt("TTL.value");
		}
		else
			return (new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.TTL.getSlot()), Slots.TTL.getLength(), Slots.TTL.getOffset())).getAmount();
	}

	@Override
	public Address initializeTTL() {
		if (! (this.getInventory().getHolder() instanceof Player)) {
			this.InventoryWriter.Write(ByteCart.myPlugin.getConfig().getInt("TTL.value"), Slots.TTL.getSlot(), false);
			this.Inventory.setContents(this.InventoryWriter.getInventory().getContents());
		}
		return this;
	}

	@Override
	public void updateTTL(int i) {
		if (! (this.getInventory().getHolder() instanceof Player)) {
			this.InventoryWriter.setWritten(Slots.REGION.getSlot()).setWritten(Slots.TRACK.getSlot()).setWritten(Slots.STATION.getSlot());
			this.InventoryWriter.Write(i, Slots.TTL.getSlot());
			this.UpdateInventory(this.InventoryWriter.getInventory());
		}
	}

}
