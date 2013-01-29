package com.github.catageek.ByteCart.HAL;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import com.github.catageek.ByteCart.Routing.Address;


// All ICs must inherit from this class
abstract public class AbstractIC implements IC {
	
	final private Block Block;
	final private org.bukkit.Location Location;
	
	protected String Name = "";
	protected String FriendlyName;
	protected String Permission = "bytecart.";
	
	protected int Buildtax = 0 ;
	protected int Triggertax = 0;
	
	private RegistryInput[] input = new RegistryInput[7];
	private int input_args = 0;
	
	private RegistryOutput[] output = new RegistryOutput[6];
	private int output_args = 0;

	public AbstractIC(Block block) {
		this.Block = block;
		this.Location = block.getLocation();
	}

	public final void addInputRegistry(RegistryInput reg) {
		this.input[this.input_args++] = reg;
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : added 1 input registry");
*/	}
	
	public final void addOutputRegistry(RegistryOutput reg) {
		this.output[this.output_args++] = reg;
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : added 1 output registry");
*/	}

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

	
	public final BlockFace getCardinal() {
		try {
			return ((org.bukkit.material.Sign) this.getBlock().getState().getData()).getFacing().getOppositeFace();
		}
		catch (ClassCastException e) {
			// this is not a sign
			return null;
		}
	}

	public final Block getBlock() {
		return Block;
	}
	
	public final String getBuildPermission() {
		return this.Permission;
	}

	public final String getName() {
		return Name;
	}

	public final String getFriendlyName() {
		return this.FriendlyName;
	}

	public final int getTriggertax() {
		return Triggertax;
	}

	public final int getBuildtax() {
		return Buildtax;
	}

	public org.bukkit.Location getLocation() {
		return Location;
	}
	
	public boolean wasTrain(org.bukkit.Location loc) {
		return false;
	}
	
	public boolean isTrain() {
		return false;
	}
	
	public Address getSignAddress() {
		return null;
	}
}
