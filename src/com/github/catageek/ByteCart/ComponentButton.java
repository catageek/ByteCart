package com.github.catageek.ByteCart;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Button;

public class ComponentButton extends AbstractComponent implements OutputPin {

	protected ComponentButton(Block block) {
		super(block.getLocation());
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : adding Button at " + block.getLocation().toString());

	}

	@Override
	public void write(boolean bit) {
		if(bit == true && this.getLocation().getBlock().getState().getData() instanceof Button) {
			final ComponentButton component = this;
			final Button button = new Button(Material.STONE_BUTTON, this.getLocation().getBlock().getData());
			button.setPowered(true);
			this.getLocation().getBlock().setData(button.getData(), true);
			
			if(ByteCart.debug)
				ByteCart.log.info("Button at (" + this.getLocation().toString() + ") : " + true);

			
			MathUtil.forceUpdate(this.getLocation().getBlock().getRelative(button.getAttachedFace()));
			
			// delayed action to unpower the button after 2 s.

			ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new Runnable() {
				public void run() {
					button.setPowered(false);
					component.getLocation().getBlock().setData(button.getData(), true);
					
					if(ByteCart.debug)
						ByteCart.log.info("Button at (" + component.getLocation().toString() + ") : " + false);
				
					MathUtil.forceUpdate(component.getLocation().getBlock().getRelative(button.getAttachedFace()));
					}
			}
			, 40);
				
		}
	}

	
}
