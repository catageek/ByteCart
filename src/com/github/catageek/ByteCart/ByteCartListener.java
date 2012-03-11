package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;


public class ByteCartListener implements Listener {
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event) {
		
		
		Vehicle vehicle = event.getVehicle();
		Player player;
		int tax;
		
		// Check if the vehicle crosses a cube boundary
		if(MathUtil.isSameBlock(event.getFrom(),event.getTo()))
			return;	// no boundary crossed, resumed
		
		
		if(vehicle instanceof Minecart) // we care only of minecart
		{
			
			// we instantiate a member of the BCXXXX class
			// XXXX is read from the sign
			
			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getTo().getBlock().getRelative(BlockFace.DOWN, 2),event.getVehicle());
						
			if (myIC != null) {
				myIC.trigger();
				
				
				
				if ((! vehicle.isEmpty())
						&& ((Minecart) vehicle).getPassenger() instanceof Player) {
					
					player = (Player) vehicle.getPassenger();
					tax = myIC.getTriggertax();
					
					if (tax != 0)
						player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "1 aiguillage traversé (tarif: " + myIC.getTriggertax() + " eur0x");	
				}
			
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
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You are not authorized to place " + myIC.getFriendlyName() + " block.");
				event.setCancelled(true);
				event.getBlock().breakNaturally();
			}
			else
			{
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + myIC.getFriendlyName() + " block created.");
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "Tarif : " +myIC.getBuildtax() + " eur0x.");	
				event.setLine(2, myIC.getFriendlyName());
			}
		}
	}
}
