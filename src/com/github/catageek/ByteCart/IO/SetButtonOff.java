package com.github.catageek.ByteCart.IO;

import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Button;

import com.github.catageek.ByteCart.Util.MathUtil;

// this call represents a thread that powers off a button

public class SetButtonOff implements Runnable {

	final private Component component;
	final private Map<Block, Integer> ActivatedButtonMap;

	public SetButtonOff(Component component, Map<Block, Integer> ActivatedButtonMap){
		this.component = component;
		this.ActivatedButtonMap = ActivatedButtonMap;
	}

	@Override
	public void run() {

		BlockState block = component.getLocation().getBlock().getState();

		if (block.getData() instanceof Button) {
			Button button = (Button) block.getData();

			button.setPowered(false);
			block.setData(button);

			block.update(false, true);
			MathUtil.forceUpdate(component.getLocation().getBlock().getRelative(button.getAttachedFace()));
		}

		ActivatedButtonMap.remove(block);
	}
}
