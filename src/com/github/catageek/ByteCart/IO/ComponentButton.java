package com.github.catageek.ByteCart.IO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Button;
import org.bukkit.material.MaterialData;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.MathUtil;

public class ComponentButton extends AbstractComponent implements OutputPin, InputPin {
	
	final static private Map<Location, Integer> ActivatedButtonMap = new ConcurrentHashMap<Location, Integer>();

	protected ComponentButton(Block block) {
		super(block);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Button at " + block.getLocation().toString());
*/
	}

	@Override
	public void write(boolean bit) {
		final Block block = this.getBlock();
		final BlockState blockstate = block.getState();
		if(blockstate.getData() instanceof Button) {
			final ComponentButton component = this;
			int id;
			
			final Button button = (Button) blockstate.getData();
			
			if (bit) {
				if (ActivatedButtonMap.containsKey(block)) {
					
					// if button is already on, we cancel the scheduled thread
					ByteCart.myPlugin.getServer().getScheduler().cancelTask(ActivatedButtonMap.get(block));
					
					// and we reschedule one
					id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new SetButtonOff(component, ActivatedButtonMap)
					, 40);
					
					// We update the HashMap
					ActivatedButtonMap.put(block.getLocation(), id);
					
				}
				
				else {
					// if button is off, we power the button
					button.setPowered(true);
					blockstate.setData(button);
					blockstate.update(false, true);
					MathUtil.forceUpdate(this.getBlock().getRelative(button.getAttachedFace()));
			
			
/*			if(ByteCart.debug)
				ByteCart.log.info("Button at (" + this.getLocation().toString() + ") : " + bit);
*/
			
			
					// delayed action to unpower the button after 2 s.
				
					id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new SetButtonOff(component, ActivatedButtonMap)
					, 40);
					
					// We update the HashMap
					ActivatedButtonMap.put(block.getLocation(), id);
				}
			}
				
		}
	}

	@Override
	public boolean read() {
		MaterialData md = this.getBlock().getState().getData();
		if(md instanceof Button) {
			return ((Button) md).isPowered();
		}
		return false;
	}

	
}
