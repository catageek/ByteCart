package com.github.catageek.ByteCart;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

// All ICs must inherit from this class
abstract public class AbstractIC implements IC {
	
	final private Block Block;
	
	protected String Permission = "";
	
	private RegistryInput[] input = new RegistryInput[6];
	private int input_args = 0;
	
	private RegistryOutput[] output = new RegistryOutput[6];
	private int output_args = 0;
	
	public AbstractIC(Block block) {
		this.Block = block;
	}

	public void addInputRegistry(RegistryInput reg) {
		this.input[this.input_args++] = reg;
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : added 1 input registry");
	}
	
	public void addOutputRegistry(RegistryOutput reg) {
		this.output[this.output_args++] = reg;
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : added 1 output registry");
	}

	public void create() {
		
	}

	public void delete() {
		
	}
	
	public RegistryInput getInput(int index) {
		return input[index];
	}

	public RegistryOutput getOutput(int index) {
		return output[index];
	}

	
	// This function checks if we have a ByteCart sign at this location
	static public boolean checkEligibility(Block b){
				
		if(b.getTypeId() != Material.SIGN_POST.getId()) {
			return false;
		}
		
		return AbstractIC.checkEligibility(((Sign) b.getState()).getLine(1));
		
	}

	static public boolean checkEligibility(String s){
		
		if(! (s.matches("\\[BC[0-9]{4,4}\\]"))) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : this is not a BC : " + s);
			return false;
		}
		
		return true;
		
	}
	
	public BlockFace getCardinal() {
		try {
			return ((org.bukkit.material.Sign) this.getBlock().getState().getData()).getFacing().getOppositeFace();
		}
		catch (ClassCastException e) {
			// this is not a sign
			return null;
		}
	}

	public Block getBlock() {
		return Block;
	}
	
	public String getBuildPermission() {
		return this.Permission;
	}

}
