package com.github.catageek.ByteCart;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Button;

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
		
		Block block = component.getLocation().getBlock();
				
		Button button = new Button(Material.STONE_BUTTON, block.getData());

		button.setPowered(false);
		block.setData(button.getData(), true);
/*
		if(ByteCart.debug)
			ByteCart.log.info("Button at (" + component.getLocation().toString() + ") : " + false);
*/		
		MathUtil.forceUpdate(block.getRelative(button.getAttachedFace()));
		
		ActivatedButtonMap.remove(block);
	}
}
