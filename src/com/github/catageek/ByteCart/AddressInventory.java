package com.github.catageek.ByteCart;


/* 
 * This class represents an address stored in an inventory
 */
public final class AddressInventory implements Address {
	
	final private org.bukkit.inventory.Inventory Inventory;
	
	private enum Slots {
		// #slot, length (default : 6), pos (default : 0)
		REGION((byte) 0),
		TRACK((byte) 1),
		STATION((byte) 2, (byte) 4, (byte) 2),
		SERVICE((byte) 2, (byte) 2, (byte) 0);

		private final byte Slot, Length, Offset;

		private Slots(byte slot) {
			Slot = slot;
			Length = 6;
			Offset = 0;
		}
		
		private Slots(byte slot, byte length, byte offset) {
			Slot = slot;
			Length = length;
			Offset = offset;			
		}
		

		/**
		 * @return the slot
		 */
		public byte getSlot() {
			return Slot;
		}


		/**
		 * @return the length
		 */
		public byte getLength() {
			return Length;
		}


		/**
		 * @return the offset
		 */
		public byte getOffset() {
			return Offset;
		}


	}
	
	public AddressInventory(org.bukkit.inventory.Inventory inv) {
		this.Inventory = inv;
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

	@Override
	public Registry getService() {
		return new SubRegistry((Registry) new InventorySlot(this.getInventory(), Slots.SERVICE.getSlot()), Slots.SERVICE.getLength(), Slots.SERVICE.getOffset());
	}

}
