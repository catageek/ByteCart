package com.github.catageek.ByteCart;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;


// All ICs must inherit from this class
abstract public class AbstractIC implements IC {
	
	final private Block Block;
	final static private BlockMap DelayedThread = new BlockMap();
	final static private BlockMap State = new BlockMap();
	
	protected String Name = "";
	protected String FriendlyName = "";
	protected String Permission = "bytecart.";
	
	protected int Buildtax = 0 ;
	protected int Triggertax = 0;
	
	private RegistryInput[] input = new RegistryInput[6];
	private int input_args = 0;
	
	private RegistryOutput[] output = new RegistryOutput[6];
	private int output_args = 0;
	
	public AbstractIC(Block block) {
		this.Block = block;
	}

	public void addInputRegistry(RegistryInput reg) {
		this.input[this.input_args++] = reg;
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : added 1 input registry");
*/	}
	
	public void addOutputRegistry(RegistryOutput reg) {
		this.output[this.output_args++] = reg;
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : added 1 output registry");
*/	}

	public RegistryInput getInput(int index) {
		return input[index];
	}

	public RegistryOutput getOutput(int index) {
		return output[index];
	}

	
	// This function checks if we have a ByteCart sign at this location
	static public boolean checkEligibility(Block b){

		if(b.getTypeId() != Material.SIGN_POST.getId() && b.getTypeId() != Material.WALL_SIGN.getId()) {
			return false;
		}
		
		return AbstractIC.checkEligibility(((Sign) b.getState()).getLine(1));
		
	}

	static public boolean checkEligibility(String s){
		
		if(! (s.matches("\\[BC[0-9]{4,4}\\]"))) {
			return false;
		}
		
		return true;
		
	}

	/*
	 * create a release task
	 */
	protected final void createReleaseTask(Block block, int duration, Runnable ReleaseTask) {
		int id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, ReleaseTask
		, duration);
		
		// the id of the thread is stored in a static map
		AbstractIC.DelayedThread.createEntry(block, id);
	}
	
	protected final boolean hasReleaseTask(Block block) {
		return AbstractIC.DelayedThread.hasEntry(block);
	}

	
	/*
	 * Renew the timer of release task
	 */
	protected final void renew(Block block, int duration, Runnable ReleaseTask) {
		// we cancel the release task
		ByteCart.myPlugin.getServer().getScheduler().cancelTask(AbstractIC.DelayedThread.getValue(block));
		// we schedule a new one
		int id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, ReleaseTask, duration);
		// we update the hashmap
		AbstractIC.DelayedThread.updateValue(block, id);
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

	public String getName() {
		return Name;
	}

	public String getFriendlyName() {
		if (this.getBlock().getState() instanceof org.bukkit.block.Sign)
			return ((Sign) this.getBlock().getState()).getLine(2);
		return FriendlyName;
	}

	public int getTriggertax() {
		return Triggertax;
	}

	public int getBuildtax() {
		return Buildtax;
	}
	
	protected int getState(Block block) {
		return AbstractIC.State.getValue(block);
	}
	
	protected void setState(Block block, int state) {
		AbstractIC.State.createEntry(Block, state);
	}
	
	protected void free(Block block) {
		AbstractIC.State.deleteEntry(block);
		AbstractIC.DelayedThread.deleteEntry(block);
	}
	
	protected void freeThread(Block block) {
		AbstractIC.DelayedThread.deleteEntry(block);
	}

}
