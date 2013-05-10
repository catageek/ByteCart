package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.HAL.IC;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

public interface Updater {

	public enum Level {
		BACKBONE("backbone", 0),
		REGION("region", 1),
		LOCAL("local", 2),
		RESET_BACKBONE("reset_backbone", 8),
		RESET_REGION("reset_region", 9),
		RESET_LOCAL("reset_local", 10);

		public final int number;
		public final String name;

		private Level(String name, int level) {
			this.number = level;
			this.name = name;
		}

		static public boolean isMember(String aName) {
			Level[] aLevels = Level.values();
			for (Level aLevel : aLevels)
				if (aLevel.name.equals(aName))
					return true;
			return false;
		}
	}

	void doAction(Side to);
	void doAction(BlockFace to);
	BlockFace giveRouterDirection();
	Side giveSimpleDirection();

	public int getTrackNumber();
	public DirectionRegistry getFrom();
	public Level getLevel();
	public Vehicle getVehicle();
	public int getRegion();
	public Block getCenter();
	public String getFriendlyName();
	public IC getBcSign();
}
