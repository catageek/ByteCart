package com.github.catageek.ByteCart.Routing;


public abstract class AbstractAddress implements Address {
	
	protected boolean isValid = true;
	
	public final boolean isValid() {
		return isValid;
	}
	
	protected enum Offsets {
		// length (default : 6), pos (default : 0)
		REGION(),
		TRACK(),
		STATION(4, 2),
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

	
	public boolean setAddress(Address a) {
		this.setStation(a.getStation().getAmount());
		this.setIsTrain(a.isTrain());
		this.setTrack(a.getTrack().getAmount());
		this.setRegion(a.getRegion().getAmount());
		return this.UpdateAddress();

	}
	
	public boolean setAddress(String s) {
		return setAddress(AddressFactory.getAddress(s));
	}
	
	public final boolean setTrain(boolean istrain) {
		this.setIsTrain(istrain);
		return this.UpdateAddress();
	}
	
	public String toString() {
		return "" + this.getRegion().getAmount() + "." + this.getTrack().getAmount() + "." + (this.getStation().getAmount());
	}


	protected boolean UpdateAddress() {
		return true;
	}
	
	abstract protected void setRegion(int region);
	abstract protected void setTrack(int track);
	abstract protected void setStation(int station);
	abstract protected void setIsTrain(boolean isTrain);
}
