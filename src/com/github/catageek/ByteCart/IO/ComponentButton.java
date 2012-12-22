package com.github.catageek.ByteCart.IO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Button;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.MathUtil;

public class ComponentButton extends AbstractComponent implements OutputPin, InputPin {
	
	final static private Map<Block, Integer> ActivatedButtonMap = new ConcurrentHashMap<Block, Integer>();

	protected ComponentButton(Block block) {
		super(block.getLocation());
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Button at " + block.getLocation().toString());
*/
	}

	@Override
	public void write(boolean bit) {
		if(this.getLocation().getBlock().getState().getData() instanceof Button) {
			final ComponentButton component = this;
			final Block block = this.getLocation().getBlock();
			int id;
			
			final Button button = new Button(Material.STONE_BUTTON, this.getLocation().getBlock().getData());
			
			if (bit) {
				if (ActivatedButtonMap.containsKey(block)) {
					
					// if button is already on, we cancel the scheduled thread
					ByteCart.myPlugin.getServer().getScheduler().cancelTask(ActivatedButtonMap.get(block));
					
					// and we reschedule one
					id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new SetButtonOff(component, ActivatedButtonMap)
					, 40);
					
					// We update the HashMap
					ActivatedButtonMap.put(block, id);
					
				}
				
				else {
					// if button is off, we power the button
					button.setPowered(true);
					this.getLocation().getBlock().setData(button.getData(), true);
					MathUtil.forceUpdate(this.getLocation().getBlock().getRelative(button.getAttachedFace()));
			
			
/*			if(ByteCart.debug)
				ByteCart.log.info("Button at (" + this.getLocation().toString() + ") : " + bit);
*/
			
			
					// delayed action to unpower the button after 2 s.
				
					id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new SetButtonOff(component, ActivatedButtonMap)
					, 40);
					
					// We update the HashMap
					ActivatedButtonMap.put(block, id);
				}
			}
				
		}
	}

	@Override
	public boolean read() {
		if(this.getLocation().getBlock().getState().getData() instanceof Button) {
			final Button button = new Button(Material.STONE_BUTTON, this.getLocation().getBlock().getData());
			return button.isPowered();
				
		}
		return false;
	}

	
}
