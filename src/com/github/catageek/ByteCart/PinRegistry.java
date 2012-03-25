package com.github.catageek.ByteCart;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class PinRegistry<T> implements RegistryInput, RegistryOutput {

	final protected List<T> PinArray;
	
	public PinRegistry(T[] pins) {
		this.PinArray = Arrays.asList(pins);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : creating PinRegistry with" + this.length() + "pin(s)");
*/
	}
	
	public int length() {
		return PinArray.size();
	}

	@Override
	public int getAmount() {
		
		int amount = 0;
		int i = 1;
		
		for (ListIterator<T> it = this.PinArray.listIterator(this.length()); it.hasPrevious(); i = i << 1)
		{
			if(it.previous() != null) {
				
				it.next();
								
				if (((InputPin) it.previous()).read()) {
					amount += i;
					
				}
			
			}
		}
		return amount;
	}

	@Override
	public void setBit(int index, boolean value) {
		((OutputPin) this.PinArray.get(index)).write(value);
	}

	@Override
	public void setAmount(int amount) {
		int i = amount;
		
			
		for (ListIterator<T> it = this.PinArray.listIterator(this.length()); it.hasPrevious(); i = i >> 1)
		{
			if(it.previous() != null) {
				
				it.next();
			
				if ( (i & 1) !=0 ) {
					((OutputPin) it.previous()).write(true);

				}
				else {
					((OutputPin) it.previous()).write(false);

				}
			}
		}
		
		
	}

	@Override
	public boolean getBit(int index) {
		return ((InputPin) this.PinArray.get(index)).read();
	}

	
	

}
