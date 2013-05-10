package com.github.catageek.ByteCart.HAL;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import com.github.catageek.ByteCart.ByteCart;


// All ICs must inherit from this class
abstract public class AbstractIC implements IC {
	
	final private Block Block;
	final private org.bukkit.Location Location;
	
	@Override
	abstract public String getName();
	
	@Override
	public String getFriendlyName() {
		return ((Sign) this.getBlock().getState()).getLine(2);
	}
	
	protected int Triggertax = 0;
	
	private RegistryInput[] input = new RegistryInput[9];
	private int input_args = 0;
	
	private RegistryOutput[] output = new RegistryOutput[6];
	private int output_args = 0;

	public AbstractIC(Block block) {
		this.Block = block;
		this.Location = block.getLocation();
	}

	public final void addInputRegistry(RegistryInput reg) {
		this.input[this.input_args++] = reg;
	}
	
	public final void addOutputRegistry(RegistryOutput reg) {
		this.output[this.output_args++] = reg;
	}

	public final RegistryInput getInput(int index) {
		return input[index];
	}

	public final RegistryOutput getOutput(int index) {
		return output[index];
	}

	
	// This function checks if we have a ByteCart sign at this location
	static public final boolean checkEligibility(Block b){

		if(b.getType() != Material.SIGN_POST && b.getType() != Material.WALL_SIGN) {
			return false;
		}
		
		return AbstractIC.checkEligibility(((Sign) b.getState()).getLine(1));
		
	}

	static public final boolean checkEligibility(String s){
		
		if(! (s.matches("^\\[BC[0-9]{4,4}\\]$"))) {
			return false;
		}
		
		return true;
		
	}

	
	@Override
	public final BlockFace getCardinal() {
		try {
			BlockFace f = ((org.bukkit.material.Sign) this.getBlock().getState().getData()).getFacing().getOppositeFace();
			return f;
		}
		catch (ClassCastException e) {
			// this is not a sign
			return null;
		}
	}

	@Override
	public final Block getBlock() {
		return Block;
	}
	
	@Override
	public final String getBuildPermission() {
		return "bytecart." + getName();
	}

	@Override
	public final int getTriggertax() {
		return ByteCart.myPlugin.getConfig().getInt("usetax." + this.getName());
	}

	@Override
	public final int getBuildtax() {
		return ByteCart.myPlugin.getConfig().getInt("buildtax." + this.getName());
	}

	@Override
	public org.bukkit.Location getLocation() {
		return Location;
	}
}
