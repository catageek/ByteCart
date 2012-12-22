package com.github.catageek.ByteCart;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public abstract class AbstractRouter extends AbstractCollisionAvoider implements Router {

	private static final ExpirableMap<Location, Boolean> recentlyUsedMap = new ExpirableMap<Location, Boolean>(40, false, "recentlyUsedRouter");
	private static final ExpirableMap<Location, Boolean> hasTrainMap = new ExpirableMap<Location, Boolean>(14, false, "hasTrainRouter");

	private BlockFace From;

	protected Map<Side, Side> FromTo = new ConcurrentHashMap<Side, Side>();
	protected Map<Side, Set<Side>> Possibility = new ConcurrentHashMap<Side, Set<Side>>();
	private int secondpos = 0;
	private int posmask = 255;

	public AbstractRouter(BlockFace from, org.bukkit.Location loc) {
		super(loc);
		this.setFrom(from);
		this.addIO(from, loc.getBlock());

	}

	public void Add(TriggeredIC t) {
		// TODO Auto-generated method stub

	}

	public final BlockFace WishToGo(BlockFace from, BlockFace to, boolean isTrain) {
//		Side sfrom = getSide(from);
		Side sto = getSide(to);


		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Router : coming from " + from + " going to " + to);
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : Router : going to " + sto);

		Router ca = this;
/*
		if(ByteCart.debug) {
			ByteCart.log.info("ByteCart : position found  " + ca.getClass().toString());
			ByteCart.log.info("ByteCart : Recently used ? " + recentlyUsed);
			ByteCart.log.info("ByteCart : hasTrain ? " + hasTrain );
			ByteCart.log.info("ByteCart : isTrain ? " + isTrain );
		}
*/
		Side s = getSide(from, to);
		
		boolean cond = !this.getRecentlyUsed() && !this.getHasTrain();

		if (this.getPosmask() != 255 || cond) {

			switch(s) {
			case STRAIGHT:
				ca = new StraightRouter(from, getLocation());
				if (  (cond) || this.ValidatePosition(ca))
					break;
			case RIGHT:
				ca = new RightRouter(from, getLocation());
				if (  (cond) || this.ValidatePosition(ca))
					break;
			case LEFT:
				ca = new LeftRouter(from, getLocation());
				if (  (cond) || this.ValidatePosition(ca))
					break;
			case BACK:
				ca = new BackRouter(from, getLocation());
				if (  (cond) || this.ValidatePosition(ca))
					break;
			default:
				ca = new LeftRouter(from, getLocation());
				if (  (cond) || this.ValidatePosition(ca))
					break;
				ca = this;
			}
/*
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : Router : position changed to " + ca.getClass().toString());
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : Router : really going to " + ca.getTo());
*/			// save router in collision avoider map
			ByteCart.myPlugin.getCollisionAvoiderManager().setCollisionAvoider(this.getLocation(), ca);

			// activate secondary levers
			ca.getOutput(1).setAmount(ca.getSecondpos());
			
			//activate primary levers
			ca.route(from);
		}
		ca.Book(isTrain);
		
		return ca.getTo();
	}


	public void route(BlockFace from) {
		return;
	}
	
	public abstract BlockFace getTo();

	/**
	 * @return the from
	 */
	public final BlockFace getFrom() {
		return From;
	}

	private final boolean ValidatePosition(Router ca) {
		Side side = getSide(this.getFrom(), ca.getFrom());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : pos value befor rotation : " + Integer.toBinaryString(getSecondpos()));
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : rotation of bits         : " + side.Value());
		int value = AbstractRouter.leftRotate8(getSecondpos(), side.Value());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : pos value after rotation : " + Integer.toBinaryString(value));
		int mask = AbstractRouter.leftRotate8(getPosmask(), side.Value());
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
	private final void setFrom(BlockFace from) {
		From = from;
	}

	private final Side getSide(BlockFace to) {
		return getSide(getFrom(), to);
	}

	private final static Side getSide(BlockFace from, BlockFace to) {
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

	private final static BlockFace turn(BlockFace b) {
		return MathUtil.anticlockwise(b);
	}

	private final void addIO(BlockFace from, org.bukkit.block.Block center) {

		BlockFace f = from;
		BlockFace g = MathUtil.clockwise(from);
		// Main output
		OutputPin[] sortie = new OutputPin[4];
		// East
		sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST,3).getRelative(BlockFace.SOUTH));
		// North
		sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST,3).getRelative(BlockFace.NORTH));
		// South
		sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH,3).getRelative(BlockFace.EAST));
		// West
		sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH,3).getRelative(BlockFace.WEST));

		Registry main = new PinRegistry<OutputPin>(sortie);

		// output[0] is main levers
		this.addOutputRegistry(main);


		// Secondary output to make U-turn
		OutputPin[] secondary = new OutputPin[8];

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
	public final int getSecondpos() {
		return secondpos;
	}

	/**
	 * @return the posmask
	 */
	public final int getPosmask() {
		return posmask;
	}

	/**
	 * @param posmask the posmask to set
	 */
	public final void setPosmask(int posmask) {
		this.posmask = posmask;
	}

	/**
	 * @param secondpos the secondpos to set
	 */
	public final void setSecondpos(int secondpos) {
		this.secondpos = secondpos;
	}


	private final static int leftRotate8(int value, int d) {
		int b = 8 - d;
		return (value >> (b)) | ((value & ((1 << b) - 1)) << d);
	}

	protected ExpirableMap<Location, Boolean> getRecentlyUsedMap() {
		return recentlyUsedMap;
	}

	protected ExpirableMap<Location, Boolean> getHasTrainMap() {
		return hasTrainMap;
	}



}
