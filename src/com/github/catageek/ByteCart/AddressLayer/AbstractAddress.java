package com.github.catageek.ByteCart.AddressLayer;



/**
 * Abstract class implementing basic operations on address
 * 
 *  All address subclass must extend this class
 */
abstract class AbstractAddress implements Address {
	
	/**
	 * A flag to tell to the world if the address should be considered as valid or not
	 */
	protected boolean isValid = true;
	
	@Override
	public final boolean isValid() {
		return isValid;
	}
	
	/**
	 * Length (in bits) for various fields of address
	 * 
	 * position is deprecated
	 */
	protected enum Offsets {
		// length (default : 6), pos (default : 0)
		REGION(11, 0),
		TRACK(11, 0),
		STATION(8, 0),
		ISTRAIN(1, 0),
		ISRETURNABLE(1, 1),
		TTL(7, 0);

		private final int Length, Offset;

		private Offsets() {
			Length = 6;
			Offset = 0;
		}

		private Offsets(int length, int offset) {
			Length = length;
			Offset = offset;			
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

	/**
	 * Copy the fields of the address object
	 *
	 * @param a the source address to copy
	 * @return true if the address was copied
	 */
	private boolean setAddress(Address a) {
		this.setStation(a.getStation().getAmount());
		this.setIsTrain(a.isTrain());
		this.setTrack(a.getTrack().getAmount());
		this.setRegion(a.getRegion().getAmount());
		return this.UpdateAddress();

	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(com.github.catageek.ByteCart.AddressLayer.Address, java.lang.String)
	 */
	@Override
	public boolean setAddress(Address a, String name) {
		return this.setAddress(a);
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String)
	 */
	@Override
	public boolean setAddress(String s) {
		return setAddress(AddressFactory.getAddress(s));
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setAddress(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setAddress(String s, String name) {
		return setAddress(s);
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#setTrain(boolean)
	 */
	@Override
	public final boolean setTrain(boolean istrain) {
		this.setIsTrain(istrain);
		return this.UpdateAddress();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + this.getRegion().getAmount() + "." + this.getTrack().getAmount() + "." + (this.getStation().getAmount());
	}

	/**
	 * flush the address to its support
	 *
	 *
	 * @return always true
	 */
	protected boolean UpdateAddress() {
		finalizeAddress();
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.AddressLayer.Address#finalizeAddress()
	 */
	@Override
	public void finalizeAddress() {
	}
	
	/**
	 * Set the region field
	 *
	 *
	 * @param region the region number to set
	 */
	abstract protected void setRegion(int region);
	
	/**
	 * Set the ring field
	 *
	 *
	 * @param track the ring number to set
	 */
	abstract protected void setTrack(int track);
	
	/**
	 * Set the station field
	 *
	 *
	 * @param station the station number to set
	 */
	abstract protected void setStation(int station);
	
	/**
	 * Set the train flag
	 *
	 *
	 * @param isTrain true if the flag must be set
	 */
	abstract protected void setIsTrain(boolean isTrain);
}
