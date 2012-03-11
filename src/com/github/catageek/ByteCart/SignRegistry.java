package com.github.catageek.ByteCart;

import org.bukkit.block.Block;

final public class SignRegistry extends AbstractComponent implements RegistryInput {
	
	final private org.bukkit.block.Sign Sign;
	final private int Ligne, Length;

	protected SignRegistry(Block block, int ligne, int length) {
		super(block.getLocation());
		if (block.getState() instanceof org.bukkit.block.Sign) {
			this.Sign = (org.bukkit.block.Sign) block.getState();

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: creating SignRegistry line #" + ligne + " with length " + length + " at " + block.getLocation().toString());
		}
		else {
			ByteCart.log.info("ByteCart: SignRegistry cannot be built");
			this.Sign = null;
		}
		
		this.Length = length;
		this.Ligne = ligne;		
	}

	@Override
	public int length() {
		return this.Length;
	}

	@Override
	public int getAmount() {
		String st = this.getSuffix();
		if (st.matches("\\d+"))
			return Integer.parseInt(st) % ( 1 << this.Length);
		else {
			ByteCart.log.info("ByteCart: SignRegistry : empty line");
			return 0;
		}
	}

	@Override
	public boolean getBit(int index) {
		int temp = this.getAmount() >> index;
		
		if ((temp & 1) == 0)
			return false;
		return true;
	}

	private String getSuffix() {
		String[] st = this.Sign.getLine(this.Ligne).split(":");
		return st[ st.length - 1 ].trim();
	}
	
	private String getPrefix() {
		String[] st = this.Sign.getLine(this.Ligne).split(":");
		return st[0].trim();
	}



}