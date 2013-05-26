package com.github.catageek.ByteCart.AddressLayer;



abstract class AbstractAddress implements Address {
	
	protected boolean isValid = true;
	
	@Override
	public final boolean isValid() {
		return isValid;
	}
	
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

	
	private boolean setAddress(Address a) {
		this.setStation(a.getStation().getAmount());
		this.setIsTrain(a.isTrain());
		this.setTrack(a.getTrack().getAmount());
		this.setRegion(a.getRegion().getAmount());
		return this.UpdateAddress();

	}

	@Override
	public boolean setAddress(Address a, String name) {
		return this.setAddress(a);
	}
	
	@Override
	public boolean setAddress(String s) {
		return setAddress(AddressFactory.getAddress(s));
	}
	
	@Override
	public boolean setAddress(String s, String name) {
		return setAddress(s);
	}
	
	@Override
	public final boolean setTrain(boolean istrain) {
		this.setIsTrain(istrain);
		return this.UpdateAddress();
	}
	
	@Override
	public String toString() {
		return "" + this.getRegion().getAmount() + "." + this.getTrack().getAmount() + "." + (this.getStation().getAmount());
	}


	protected boolean UpdateAddress() {
		finalizeAddress();
		return true;
	}
	
	@Override
	public void finalizeAddress() {
	}
	
	abstract protected void setRegion(int region);
	abstract protected void setTrack(int track);
	abstract protected void setStation(int station);
	abstract protected void setIsTrain(boolean isTrain);
}
