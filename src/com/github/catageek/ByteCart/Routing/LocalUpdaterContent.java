package com.github.catageek.ByteCart.Routing;

import java.io.Serializable;
import java.util.Stack;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

class LocalUpdaterContent extends UpdaterContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3731470585941070388L;

	private Stack<Integer> Start;
	private Stack<Integer> End;


	LocalUpdaterContent(Inventory inv, Updater.Level level, int region, Player player, boolean isfullreset) {
		super(inv, level, region, player, isfullreset);
		setStart(new Stack<Integer>());
		setEnd(new Stack<Integer>());
	}


	/**
	 * @return the start
	 */
	Stack<Integer> getStart() {
		return Start;
	}


	/**
	 * @param start the start to set
	 */
	void setStart(Stack<Integer> start) {
		Start = start;
	}


	/**
	 * @return the end
	 */
	Stack<Integer> getEnd() {
		return End;
	}


	/**
	 * @param end the end to set
	 */
	void setEnd(Stack<Integer> end) {
		End = end;
	}

}
