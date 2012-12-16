package com.github.catageek.ByteCart;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public interface IC {
	public String getBuildPermission();
	public String getName();
	public String getFriendlyName();
	public int getTriggertax();
	public int getBuildtax();
	public Block getBlock();
	public Location getLocation();
	public BlockFace getCardinal();
}

