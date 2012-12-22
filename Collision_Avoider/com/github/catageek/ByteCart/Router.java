package com.github.catageek.ByteCart;

import org.bukkit.block.BlockFace;

public interface Router extends CollisionAvoider {
	public <T extends Router> BlockFace WishToGo(BlockFace from, BlockFace to, boolean isTrain);
	public void Book(boolean b);
	public int getSecondpos() ;
	public int getPosmask() ;
	public void setPosmask(int posmask) ;
	public void setSecondpos(int secondpos) ;
	public BlockFace getFrom();
	public void route(BlockFace from);
	public RegistryOutput getOutput(int i);
	public BlockFace getTo();
}
