package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;


public class ByteCartListener implements Listener {
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event) {
		
		// Check if the vehicle crosses a cube boundary
		if(MathUtil.isSameBlock(event.getFrom(),event.getTo()))
			return;	// no boundary crossed, resumed
		
		
		if(event.getVehicle() instanceof Minecart) // we care only of minecart
		{
			
			// we instantiate a member of the BCXXXX class
			// XXXX is read from the sign
			
			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getTo().getBlock().getRelative(BlockFace.DOWN, 2),event.getVehicle());
						
			if (myIC != null) {
				myIC.trigger();
			}
			
		}
    	
    	
	}

	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		
		if (! AbstractIC.checkEligibility(event.getLine(1)))
			return;

		TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getBlock(), event.getLine(1));
		
		if (myIC != null) {
			if (! event.getPlayer().hasPermission(myIC.getBuildPermission())) {
				event.getPlayer().sendMessage(ChatColor.RED+"You are not authorized to put this sign");
				event.setCancelled(true);
				event.getBlock().breakNaturally();
			}
			else
			{
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"Bytecart block created");
			}
		}
	}
}
