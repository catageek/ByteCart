package com.github.catageek.ByteCart;


// This class represents a canonical address like xx.xx.xx
public class AddressString implements Address {
	
	final private String String; // address as displayed
	
	public AddressString(String s) {
		if (AddressString.isAddress(s))
			this.String = s;
		else
			throw new IllegalArgumentException();
	}
	
	static public boolean isAddress(String s) {
		if(! (s.matches("([0-9]{1,2}\\.){2,2}[0-9]{1,2}"))) {
			return false;
		}
		
		return true;

	}
	
	public VirtualRegistry getRegion() {
/*		VirtualRegistry ret = new VirtualRegistry(5);
		ret.setAmount( this.getField(0) >> 1 );
*/
		VirtualRegistry ret = new VirtualRegistry(6);
		ret.setAmount( this.getField(0));
		return ret;
	}

	public VirtualRegistry getTrack() {
/*		VirtualRegistry ret = new VirtualRegistry(5);
		ret.setAmount( this.getField(1) >> 1 );
*/
		VirtualRegistry ret = new VirtualRegistry(6);
		ret.setAmount( this.getField(1));
		
		return ret;
	}

	public VirtualRegistry getStation() {
/*		VirtualRegistry ret = new VirtualRegistry(6);
		ret.setAmount( this.getField(2) );
*/
		VirtualRegistry ret = new VirtualRegistry(4);
		ret.setAmount( this.getField(2));
	
		return ret;
	}


	private int getField(int index) {
		String[] st = this.String.split("\\.");
		return Integer.parseInt(st[ index ].trim());
	}

	@Override
	public Registry getService() {
		VirtualRegistry ret = new VirtualRegistry(4);
		ret.setAmount( this.getField(2) >> 4);
	
		return ret;
	}

	@Override
	public Address setRegion(int region) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address setTrack(int track) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address setStation(int station) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.lang.String getAddress() {
		return this.String;
	}

}
