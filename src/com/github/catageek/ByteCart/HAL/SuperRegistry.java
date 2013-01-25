package com.github.catageek.ByteCart.HAL;

// This class represents 2 registries merged
public class SuperRegistry<T extends Registry> implements RegistryBoth {

	// Registry1 est le registre de poids fort
	
	private final Registry Registry1, Registry2;
	
	public SuperRegistry(T reg1, T reg2) {
		this.Registry1 = reg1;
		this.Registry2 = reg2;
	}
	
	@Override
	public void setBit(int index, boolean value) {
		if (index < this.Registry1.length())
			( (RegistryOutput) this.Registry1).setBit(index, value);
		else
			((RegistryOutput) this.Registry2).setBit(index-this.Registry1.length(), value);
		
	}

	@Override
	public boolean getBit(int index) {
		
		if (index < this.Registry1.length())
			return ((RegistryInput) this.Registry1).getBit(index);
		return ((RegistryInput) this.Registry2).getBit(index-this.Registry1.length());
	}

	@Override
	public int length() {
		return this.Registry1.length() + this.Registry2.length();
	}

	@Override
	public int getAmount() {
		return (this.Registry1.getAmount() << this.Registry2.length()) + this.Registry2.getAmount();
	}

	@Override
	public void setAmount(int amount) {
		((RegistryOutput) this.Registry1).setAmount(amount >> (this.Registry2.length()) % ( 1 << this.Registry1.length()));
		((RegistryOutput) this.Registry2).setAmount(amount & (( 1 << this.Registry2.length() ) - 1));
		
	}

}
