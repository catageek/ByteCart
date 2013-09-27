package com.github.catageek.ByteCart.CollisionManagement;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.HAL.RegistryOutput;

/**
 * A router representation in the collision management layer
 */
public interface Router extends CollisionAvoider {
	/**
	 * Ask for a direction, requesting a possible transition
	 *
	 * @param from the direction from where comes the cart
	 * @param to the direction where the cart goes to
	 * @param isTrain true if it is a train
	 * @return the direction actually taken
	 */
	public <T extends Router> BlockFace WishToGo(BlockFace from, BlockFace to, boolean isTrain);
	/**
	 * Book the router, i.e mark it as currently in use
	 *
	 * @param b true if this is a train
	 */
	public void Book(boolean b);
	
	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.CollisionManagement.CollisionAvoider#getSecondpos()
	 */
	public int getSecondpos() ;
	
	/**
	 * Get the mask of the current usage of the router, starting from the origin axis.
	 * 
	 * <p>a bit to 1 means the lever is blocked, a bit to 0 means the lever is not blocked</p>
	 *
	 * @return the mask
	 */
	public int getPosmask() ;
	/**
	 * Set the mask of the current usage of the router, starting from the origin axis.
	 *
	 * <p>a bit to 1 means the lever is blocked, a bit to 0 means the lever is not blocked</p>
	 *
	 * @param posmask the mask to set
	 */
	public void setPosmask(int posmask) ;
	/**
	 * Set the value of the position of the 8 exterior levers, starting from the origin axis.
	 *
	 * <p>a bit to 1 means the lever is on, a bit to 0 means the lever is off</p>
	 *
	 * @param secondpos the value to set
	 */
	public void setSecondpos(int secondpos) ;
	/**
	 * Get the direction from where the cart is coming.
	 * 
	 * <p>This direction is also the origin axis of the registries</p>
	 *
	 *
	 * @return the direction
	 */
	public BlockFace getFrom();
	/**
	 * Activate levers according to registry
	 *
	 *
	 * @param from the origin direction
	 */
	public void route(BlockFace from);
	/**
	 * Get the lever registry
	 *
	 * @param i 0 for main levers, 1 for secondary levers
	 * @return the value of the registry
	 */
	public RegistryOutput getOutput(int i);
	/**
	 * Get the direction where the cart eventually go
	 *
	 *
	 * @return the direction
	 */
	public BlockFace getTo();
}
