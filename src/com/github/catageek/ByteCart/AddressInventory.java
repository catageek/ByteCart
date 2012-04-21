package com.github.catageek.ByteCart;

import org.bukkit.entity.Player;


/* 
 * This class represents an address stored in an inventory
 */
public final class AddressInventory implements AddressRouted {

	private final org.bukkit.inventory.Inventory Inventory;
	private InventoryWriter InventoryWriter;

	private enum Slots {
		// #slot, length (default : 6), pos (default : 0)
		REGION(0),
		TRACK(1),
		STATION(2, 4, 2),
		SERVICE(2, 2, 0),
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
	private void UpdateInventory(org.bukkit.inventory.Inventory inventory) {
		if (this.InventoryWriter.isSuccess())
			this.Inventory.setContents(inventory.getContents());
		else {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : UpdateInventory is not success");
		}
		if (this.getInventory().getHolder() instanceof Player)
			((Player) this.getInventory().getHolder()).updateInventory();
	}

	@Override
	public Registry getService() {
		return new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.SERVICE.getSlot()), Slots.SERVICE.getLength(), Slots.SERVICE.getOffset());
	}

	@Override
	public Address setRegion(int region) {
		this.InventoryWriter.Write(region, Slots.REGION.getSlot());
		this.UpdateInventory(this.InventoryWriter.getInventory());

		return this;
	}

	@Override
	public Address setTrack(int track) {
		this.InventoryWriter.Write(track, Slots.TRACK.getSlot());
		this.UpdateInventory(this.InventoryWriter.getInventory());

		return this;
	}

	@Override
	public Address setStation(int station) {
		this.InventoryWriter.Write(station, Slots.STATION.getSlot());
		this.UpdateInventory(this.InventoryWriter.getInventory());

		return this;
	}

	@Override
	public String getAddress() {
		return "" + this.getRegion().getAmount() + "." + this.getTrack().getAmount() + "." + (this.getStation().getAmount() + this.getService().getAmount());
	}

	@Override
	public Registry getTTL() {
		if (this.getInventory().getHolder() instanceof Player) {
			Registry v = new VirtualRegistry(6);
			v.setAmount(ByteCart.myPlugin.getConfig().getInt("TTL.value"));
			return v;
		}
		else
			return new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.TTL.getSlot()), Slots.TTL.getLength(), Slots.TTL.getOffset());
	}

	@Override
	public Address initializeTTL() {
		if (! (this.getInventory().getHolder() instanceof Player)) {
			this.InventoryWriter.Write(ByteCart.myPlugin.getConfig().getInt("TTL.value"), Slots.TTL.getSlot());
			this.UpdateInventory(this.InventoryWriter.getInventory());
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

	@Override
	public Address setAddress(String s) {
		Address src = AddressFactory.getAddress(s);
		this.InventoryWriter.setWritten(Slots.TTL.getSlot());
		this.InventoryWriter.Write(src.getRegion().getAmount(), Slots.REGION.getSlot());
		this.InventoryWriter.Write(src.getTrack().getAmount(), Slots.TRACK.getSlot());
		this.InventoryWriter.Write(src.getStation().getAmount(), Slots.STATION.getSlot());
		this.UpdateInventory(this.InventoryWriter.getInventory());
		return this;
	}
	
	@Override
	public String toString() {
		return this.getAddress();
		
	}


}
