package com.github.catageek.ByteCart;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.block.BlockFace;

public abstract class AbstractRouter extends AbstractCollisionAvoider implements Router  {

	private BlockFace From;

	protected Map<Side, Side> FromTo = new ConcurrentHashMap<Side, Side>();
	protected Map<Side, Set<Side>> Possibility = new ConcurrentHashMap<Side, Set<Side>>();

	protected int secondpos = 0;
	protected int posmask = 255;

	public AbstractRouter(BlockFace from, org.bukkit.block.Block block) {
		super(block);
		this.setFrom(from);
		this.addIO(from, block);

	}

	public void Add(TriggeredIC t) {
		// TODO Auto-generated method stub

	}

	public void WishToGo(BlockFace from, BlockFace to, boolean isTrain) {
		Side sfrom = getSide(from);
		Side sto = getSide(to);


		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : coming from " + from + " going to " + to);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : coming from " + sfrom + " going to " + sto);

		Router ca = this;

		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : position found  " + ca.getClass().toString());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Recently used ? " + recentlyUsed);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : hasTrain ? " + hasTrain );
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : isTrain ? " + isTrain );

		Side s = getSide(from, to);

		if (this.getPosmask() != 255 || (!recentlyUsed && !hasTrain)) {

			switch(s) {
			case STRAIGHT:
				ca = new StraightRouter(from, getBlock());
				if (  (!recentlyUsed && !hasTrain) || this.ValidatePosition(ca))
					break;
			case RIGHT:
				ca = new RightRouter(from, getBlock());
				if (  (!recentlyUsed && !hasTrain) || this.ValidatePosition(ca))
					break;
			case LEFT:
				ca = new LeftRouter(from, getBlock());
				if (  (!recentlyUsed && !hasTrain) || this.ValidatePosition(ca))
					break;
			case BACK:
				ca = new BackRouter(from, getBlock());
				if (  (!recentlyUsed && !hasTrain) || this.ValidatePosition(ca))
					break;
			default:
				ca = new LeftRouter(from, getBlock());
				if (  (!recentlyUsed && !hasTrain) || this.ValidatePosition(ca))
					break;
				ca = this;
			}

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : position changed to " + ca.getClass().toString());
			// save router in collision avoider map
			ByteCart.myPlugin.getCollisionAvoiderManager().setCollisionAvoider(getBlock(), ca);

			// activate secondary levers
			ca.getOutput(1).setAmount(ca.getSecondpos());
			
			//activate primary levers
			ca.route(from);
		}
		ca.Book(isTrain);
	}


	public void route(BlockFace from) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the from
	 */
	public BlockFace getFrom() {
		return From;
	}

	private boolean ValidatePosition(Router ca) {
		Side side = getSide(this.getFrom(), ca.getFrom());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : pos value befor rotation : " + Integer.toBinaryString(getSecondpos()));
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : rotation of bits         : " + side.Value());
		int value = this.leftRotate8(getSecondpos(), side.Value());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : pos value after rotation : " + Integer.toBinaryString(value));
		int mask = this.leftRotate8(getPosmask(), side.Value());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : mask after rotation      : " + Integer.toBinaryString(mask));
		ca.setSecondpos(value | ca.getSecondpos());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : value after OR           : " + Integer.toBinaryString(ca.getSecondpos()));
		ca.setPosmask(mask | ca.getPosmask());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : mask after OR            : " + Integer.toBinaryString(ca.getPosmask()));
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : compatible ?             : " + (((value ^ ca.getSecondpos()) & mask) == 0));
		return ((value ^ ca.getSecondpos()) & mask) == 0;
	}

	/**
	 * @param from the from to set
	 */
	private void setFrom(BlockFace from) {
		From = from;
	}

	private Side getSide(BlockFace to) {
		return getSide(getFrom(), to);
	}

	private Side getSide(BlockFace from, BlockFace to) {
		BlockFace t = to;
		if (from == t)
			return Side.BACK;
		t = turn(t);
		if (from == t)
			return Side.LEFT;
		t = turn(t);
		if (from == t)
			return Side.STRAIGHT;
		return Side.RIGHT;
	}

	private BlockFace turn(BlockFace b) {
		return MathUtil.anticlockwise(b);
	}

	private void addIO(BlockFace from, org.bukkit.block.Block center) {

		BlockFace f = from;
		BlockFace g = MathUtil.clockwise(from);
		// Main output
		OutputPin[] sortie = new OutputPin[4];
		// South
		sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST,3).getRelative(BlockFace.SOUTH));
		// West
		sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST,3).getRelative(BlockFace.NORTH));
		// North
		sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH,3).getRelative(BlockFace.EAST));
		// East
		sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH,3).getRelative(BlockFace.WEST));

		Registry main = new PinRegistry<OutputPin>(sortie);

		// output[0] is main levers
		this.addOutputRegistry(main);


		// Secondary output to make U-turn
		OutputPin[] secondary = new OutputPin[8];
		// third output to go left
		//OutputPin[] third = new OutputPin[4];

		for (int i=0; i<7; i++) {
			// the first is Back
			secondary[i++] = OutputPinFactory.getOutput(center.getRelative(f, 4).getRelative(g, 2));
			secondary[i] = OutputPinFactory.getOutput(center.getRelative(f, 6));
			f = g;
			g = MathUtil.clockwise(g);			
		}

		RegistryOutput second = new PinRegistry<OutputPin>(secondary);

		// output[1] is second and third levers
		this.addOutputRegistry(second);

	}

	/**
	 * @return the secondpos
	 */
	public int getSecondpos() {
		return secondpos;
	}

	/**
	 * @return the posmask
	 */
	public int getPosmask() {
		return posmask;
	}

	/**
	 * @param posmask the posmask to set
	 */
	public void setPosmask(int posmask) {
		this.posmask = posmask;
	}

	/**
	 * @param secondpos the secondpos to set
	 */
	public void setSecondpos(int secondpos) {
		this.secondpos = secondpos;
	}


	private int leftRotate8(int value, int d) {
		int b = 8 - d;
		return (value >> (b)) | ((value & ((1 << b) - 1)) << d);
	}



}
