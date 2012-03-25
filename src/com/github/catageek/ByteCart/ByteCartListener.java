package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;


public class ByteCartListener implements Listener {
	
	private TriggeredICFactory MyTriggeredICFactory;
	private PoweredICFactory MyPoweredICFactory;


	ByteCartListener() {
		this.MyTriggeredICFactory = new TriggeredICFactory();
		this.MyPoweredICFactory = new PoweredICFactory();
	}
	
	@EventHandler(ignoreCancelled = true)
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
			
			TriggeredIC myIC = this.MyTriggeredICFactory.getTriggeredIC(event.getTo().getBlock().getRelative(BlockFace.DOWN, 2),event.getVehicle());
						
			if (myIC != null) {

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: " + myIC.getName() + ".trigger()");

				myIC.trigger();
				
				if (myIC instanceof BC8010) {
					vehicle.setVelocity((new Vector(((AbstractIC) myIC).getCardinal().getModX(), ((AbstractIC) myIC).getCardinal().getModY(), ((AbstractIC) myIC).getCardinal().getModZ())).multiply(1.5));
		/*			ByteCart.myPlugin.getServer().broadcastMessage(myIC.getName() + " Velocity : " + (float) vehicle.getVelocity().length());
		*/		}
				
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

	@EventHandler(ignoreCancelled = true)
	public void onVehicleCreate(VehicleCreateEvent event) {
		
		Vehicle vehicle = event.getVehicle();
		Player player;
		int tax;
		
		if(vehicle instanceof Minecart) // we care only of minecart
		{
			
			// we instantiate a member of the BCXXXX class
			// XXXX is read from the sign
			
			TriggeredIC myIC = this.MyTriggeredICFactory.getTriggeredIC(event.getVehicle().getLocation().getBlock().getRelative(BlockFace.DOWN, 2),event.getVehicle());
						
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

	
	@EventHandler(ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		
		if (event.isCancelled())
			return;
		
		if (! AbstractIC.checkEligibility(event.getLine(1)))
			return;

		TriggeredIC myIC = this.MyTriggeredICFactory.getTriggeredIC(event.getBlock(), event.getLine(1));
		
		if (myIC != null) {
			if (! event.getPlayer().hasPermission(myIC.getBuildPermission())) {
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You are not authorized to place " + myIC.getFriendlyName() + " block.");
				event.setLine(1, "");
			}
			else
			{
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + myIC.getFriendlyName() + " block created.");
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "Tarif : " +myIC.getBuildtax() + " eur0x.");	
				event.setLine(2, myIC.getFriendlyName());
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		
		if (event.isCancelled())
			return;
		
		if (! AbstractIC.checkEligibility(event.getBlock().getRelative(BlockFace.DOWN)))
			return;
		
		PoweredIC myIC = this.MyPoweredICFactory.getIC(event.getBlock().getRelative(BlockFace.DOWN));

	
		if (myIC != null) {

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: " + myIC.getName() + ".power()");

			myIC.power();
		}

	}
}
