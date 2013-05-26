package com.github.catageek.ByteCart.Routing;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.HAL.IC;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

public interface Updater {

	public enum Scope {
		BACKBONE("backbone"),
		REGION("region"),
		LOCAL("local");
		
		public final String name;
		
		private Scope(String name) {
			this.name = name;
		}

	}
	
	public enum Level {
		BACKBONE("backbone", 0, Scope.BACKBONE, "rip"),
		REGION("region", 1, Scope.REGION, "rip"),
		LOCAL("local", 2, Scope.LOCAL, "conf"),
		RESET_BACKBONE("reset_backbone", 8, Scope.BACKBONE, "reset"),
		RESET_REGION("reset_region", 9, Scope.REGION, "reset"),
		RESET_LOCAL("reset_local", 10, Scope.LOCAL, "reset");

		public final int number;
		public final String name;
		public final Scope scope;
		public final String type;

		private Level(String name, int level, Scope scope, String type) {
			this.number = level;
			this.name = name;
			this.scope = scope;
			this.type = type;
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
	public int getWandererRegion();
	public Block getCenter();
	public String getFriendlyName();
	public IC getBcSign();
}
