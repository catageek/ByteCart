package com.github.catageek.ByteCart.EventManagement;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.Signs.ClickedSign;
import com.github.catageek.ByteCart.Signs.ClickedSignFactory;
import com.github.catageek.ByteCart.Signs.PoweredSign;
import com.github.catageek.ByteCart.Signs.PoweredSignFactory;
import com.github.catageek.ByteCart.Signs.TriggeredSign;
import com.github.catageek.ByteCart.Signs.TriggeredSignFactory;



public class ByteCartListener implements Listener {

	private PoweredSignFactory MyPoweredICFactory;


	public ByteCartListener() {
		this.MyPoweredICFactory = new PoweredSignFactory();
	}

	@EventHandler(ignoreCancelled = true)
	public void onVehicleMove(VehicleMoveEvent event) {

		Location loc = event.getFrom();
		Integer from_x = loc.getBlockX();
		Integer from_z = loc.getBlockZ();
		loc = event.getTo();
		int to_x = loc.getBlockX();
		int to_z = loc.getBlockZ();


		// Check if the vehicle crosses a cube boundary
		if(from_x == to_x && from_z == to_z)
			return;	// no boundary crossed, resumed

		if(event.getVehicle() instanceof Minecart) // we care only of minecart
		{
			Minecart vehicle = (Minecart) event.getVehicle();

			// we instantiate a member of the BCXXXX class
			// XXXX is read from the sign

			TriggeredSign myIC = TriggeredSignFactory.getTriggeredIC(event.getTo().getBlock().getRelative(BlockFace.DOWN, 2),vehicle);

			Player player;
			int tax;

			if (myIC != null) {

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: " + myIC.getName() + ".trigger()");

				myIC.trigger();

				if ((! vehicle.isEmpty())
						&& vehicle.getPassenger() instanceof Player) {

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

		Player player;
		int tax;

		if(event.getVehicle() instanceof Minecart) // we care only of minecart
		{

			Minecart vehicle = (Minecart) event.getVehicle();
			// we instantiate a member of the BCXXXX class
			// XXXX is read from the sign

			TriggeredSign myIC = TriggeredSignFactory.getTriggeredIC(vehicle.getLocation().getBlock().getRelative(BlockFace.DOWN, 2),vehicle);

			if (myIC != null) {
				myIC.trigger();



				if ((! vehicle.isEmpty())
						&& vehicle.getPassenger() instanceof Player) {

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
			TriggeredSign myIC = TriggeredSignFactory.getTriggeredIC(event.getBlock(), event.getLine(1), null);

			if (myIC != null) {
				if (! event.getPlayer().hasPermission(myIC.getBuildPermission())) {
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You are not authorized to place " + myIC.getFriendlyName() + " block.");
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You must have " + myIC.getBuildPermission());
					event.setLine(1, "");
				}
				else
				{
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + myIC.getFriendlyName() + " block created.");
					int tax = myIC.getBuildtax();
					if (tax > 0)
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

		/*				if(ByteCart.debug)
			ByteCart.log.info("ByteCart: event " + event.getBlock().toString());
		 */

		PoweredSign myIC = this.MyPoweredICFactory.getIC(event.getBlock().getRelative(BlockFace.DOWN));


		if (myIC != null) {
			myIC.power();
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getAction().compareTo(Action.RIGHT_CLICK_BLOCK) != 0 || !AbstractIC.checkEligibility(event.getClickedBlock()))
			return;
		ClickedSign myIC = ClickedSignFactory.getClickedIC(event.getClickedBlock(), event.getPlayer());

		if (myIC != null) {

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: " + myIC.getName() + ".click()");

			myIC.click();
			event.setCancelled(true);
		}
	}
}
