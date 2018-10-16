package com.github.catageek.ByteCart.IO;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * this call represents a thread that powers off a button
 */
class SetButtonOff implements Runnable {

	final private Component component;
	final private Map<Location, Integer> ActivatedButtonMap;

	/**
	 * @param component the component to power off
	 * @param ActivatedButtonMap a map containing the task id of current task
	 */
	SetButtonOff(Component component, Map<Location, Integer> ActivatedButtonMap){
		this.component = component;
		this.ActivatedButtonMap = ActivatedButtonMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		final Block block = component.getBlock();

		if (block.getBlockData() instanceof Switch) {
			final Switch button = (Switch) block.getBlockData();

			button.setPowered(false);
			block.setBlockData(button);

			MathUtil.forceUpdate(block.getRelative(button.getFacing().getOppositeFace()));
		}

		ActivatedButtonMap.remove(block.getLocation());
	}
}
