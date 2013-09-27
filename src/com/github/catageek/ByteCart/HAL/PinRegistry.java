package com.github.catageek.ByteCart.HAL;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.github.catageek.ByteCart.IO.InputPin;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCartAPI.HAL.Registry;
import com.github.catageek.ByteCartAPI.HAL.RegistryInput;
import com.github.catageek.ByteCartAPI.HAL.RegistryOutput;

/**
 * A registry implementation
 * 
 * @param <T> InputPin or OutputPin type
 */
public class PinRegistry<T> implements RegistryInput, RegistryOutput, Registry {

	final protected List<T> PinArray;
	
	/**
	 * @param pins an array of pins
	 */
	public PinRegistry(T[] pins) {
		this.PinArray = Arrays.asList(pins);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : creating PinRegistry with" + this.length() + "pin(s)");
*/
	}
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#length()
	 */
	@Override
	public int length() {
		return PinArray.size();
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.Registry#getAmount()
	 */
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

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryOutput#setBit(int, boolean)
	 */
	@Override
	public void setBit(int index, boolean value) {
		((OutputPin) this.PinArray.get(index)).write(value);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryOutput#setAmount(int)
	 */
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

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.HAL.RegistryInput#getBit(int)
	 */
	@Override
	public boolean getBit(int index) {
		return ((InputPin) this.PinArray.get(index)).read();
	}

	
	

}
