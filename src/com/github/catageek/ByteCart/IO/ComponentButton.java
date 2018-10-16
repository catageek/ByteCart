package com.github.catageek.ByteCart.IO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Switch;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A button
 */
class ComponentButton extends AbstractComponent implements OutputPin, InputPin {
	
	final static private Map<Location, Integer> ActivatedButtonMap = new ConcurrentHashMap<Location, Integer>();

	/**
	 * @param block the block containing the component
	 */
	protected ComponentButton(Block block) {
		super(block);
/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Button at " + block.getLocation().toString());
*/
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.IO.OutputPin#write(boolean)
	 */
	@Override
	public void write(boolean bit) {
		final Block block = this.getBlock();
		final BlockData blockdata = block.getBlockData();
		if(blockdata instanceof Switch) {
			final ComponentButton component = this;
			int id;
			
			final Switch button = (Switch) block.getBlockData();
			
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
					block.setBlockData(button);
					MathUtil.forceUpdate(block.getRelative(button.getFacing().getOppositeFace()));
			
					// delayed action to unpower the button after 2 s.
				
					id = ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new SetButtonOff(component, ActivatedButtonMap)
					, 40);
					
					// We update the HashMap
					ActivatedButtonMap.put(block.getLocation(), id);
				}
			}
				
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.IO.InputPin#read()
	 */
	@Override
	public boolean read() {
		final BlockData md = this.getBlock().getBlockData();
		if(md instanceof Powerable) {
			return ((Powerable) md).isPowered();
		}
		return false;
	}

	
}
