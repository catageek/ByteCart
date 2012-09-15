package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;


public class ByteCartListener implements Listener {

	private PoweredICFactory MyPoweredICFactory;


	ByteCartListener() {
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

			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getTo().getBlock().getRelative(BlockFace.DOWN, 2),event.getVehicle());

			if (myIC != null) {

				/*				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: " + myIC.getName() + ".trigger()");
				 */
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
						player.sendMessage(ChatColor.DARK_GRAY+"[Bytecart] " + "Echangeur (tarif: " + myIC.getTriggertax() + " eur0x)");	
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

			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getVehicle().getLocation().getBlock().getRelative(BlockFace.DOWN, 2),event.getVehicle());

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

		if (! AbstractIC.checkEligibility(event.getLine(1)))
			return;

		try {
			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getBlock(), event.getLine(1), null);

			if (myIC != null) {
				if (! event.getPlayer().hasPermission(myIC.getBuildPermission())) {
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You are not authorized to place " + myIC.getFriendlyName() + " block.");
					event.setLine(1, "");
				}
				else
				{
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + myIC.getFriendlyName() + " block created.");
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "Tarif : " +myIC.getBuildtax() + " eur0x.");	
					if (event.getLine(2).compareTo("") == 0)
						event.setLine(2, myIC.getFriendlyName());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event) {

		if (event.getChangedType() != Material.REDSTONE_WIRE || ! AbstractIC.checkEligibility(event.getBlock().getRelative(BlockFace.DOWN)))
			return;

		PoweredIC myIC = this.MyPoweredICFactory.getIC(event.getBlock().getRelative(BlockFace.DOWN));


		if (myIC != null) {
			myIC.power();
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {

		/*		if(ByteCart.debug) {
				ByteCart.log.info("ByteCart : bloc clicked " + event.getClickedBlock().getLocation());
				ByteCart.log.info("ByteCart : !AbstractIC.checkEligibility :" + !AbstractIC.checkEligibility(event.getClickedBlock()));
		}
		 */		
		if (event.getAction().compareTo(Action.RIGHT_CLICK_BLOCK) != 0 || !AbstractIC.checkEligibility(event.getClickedBlock()))
			return;
		ClickedIC myIC = ClickedICFactory.getClickedIC(event.getClickedBlock(), event.getPlayer());

		if (myIC != null) {

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: " + myIC.getName() + ".click()");

			myIC.click();
		}
	}

}
