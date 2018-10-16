package com.github.catageek.ByteCart.EventManagement;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.HAL.AbstractIC;
import com.github.catageek.ByteCart.Signs.Clickable;
import com.github.catageek.ByteCart.Signs.ClickedSignFactory;
import com.github.catageek.ByteCart.Signs.Powerable;
import com.github.catageek.ByteCart.Signs.PoweredSignFactory;
import com.github.catageek.ByteCart.Signs.Triggable;
import com.github.catageek.ByteCart.Signs.TriggeredSignFactory;
import com.github.catageek.ByteCartAPI.Event.SignCreateEvent;
import com.github.catageek.ByteCartAPI.Event.SignRemoveEvent;
import com.github.catageek.ByteCartAPI.HAL.IC;



/**
 * The main listener
 */
public class ByteCartListener implements Listener {

	private PoweredSignFactory MyPoweredICFactory;


	public ByteCartListener() {
		this.MyPoweredICFactory = new PoweredSignFactory();
	}

	/**
	 * Detect if a sign is under the cart moving
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true)
	@SuppressWarnings("ucd")
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

			Triggable myIC;
			myIC = TriggeredSignFactory.getTriggeredIC(event.getTo().getBlock().getRelative(BlockFace.DOWN, 2),vehicle);

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

	/**
	 * Detect a sign under the cart created
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true)
	@SuppressWarnings("ucd")
	public void onVehicleCreate(VehicleCreateEvent event) {

		Player player;
		int tax;

		if(event.getVehicle() instanceof Minecart) // we care only of minecart
		{

			Minecart vehicle = (Minecart) event.getVehicle();
			// we instantiate a member of the BCXXXX class
			// XXXX is read from the sign

			Triggable myIC;
			myIC = TriggeredSignFactory.getTriggeredIC(vehicle.getLocation().getBlock().getRelative(BlockFace.DOWN, 2),vehicle);

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

	/**
	 * Detect if we create a sign
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true)
	@SuppressWarnings("ucd")
	public void onSignChange(SignChangeEvent event) {

		if (! AbstractIC.checkEligibility(event.getLine(1)))
			return;

		AbstractIC.removeFromCache(event.getBlock());

		IC myIC = TriggeredSignFactory.getTriggeredIC(event.getBlock(), event.getLine(1), null);

		if (myIC == null) {
			myIC = ClickedSignFactory.getClickedIC(event.getBlock(), event.getLine(1), event.getPlayer());
		}

		if (myIC == null) {
			myIC = PoweredSignFactory.getPoweredIC(event.getBlock(), event.getLine(1));
		}

		if (myIC != null) {
			Player player = event.getPlayer();
			if (! player.hasPermission(myIC.getBuildPermission())) {
				player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You are not authorized to place " + myIC.getFriendlyName() + " block.");
				player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You must have " + myIC.getBuildPermission());
				event.setLine(1, "");
			}
			else
			{
				player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + myIC.getFriendlyName() + " block created.");
				int tax = myIC.getBuildtax();
				if (tax > 0)
					player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "Tarif : " +myIC.getBuildtax() + " eur0x.");
				if (event.getLine(2).compareTo("") == 0)
					event.setLine(2, myIC.getFriendlyName());
				Bukkit.getPluginManager().callEvent(new SignCreateEvent(myIC, player, event.getLines()));
			}
		}
	}

	/**
	 * Check if a sign was broken
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	@SuppressWarnings("ucd")
	public void onBlockBreak(BlockBreakEvent event) {
		removeSignIfNeeded(event.getBlock(), event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	@SuppressWarnings("ucd")
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		removeSignIfNeeded(event.getBlock(), event.getEntity());
	}

	/**
	 * Check if a sign was destroyed in the explosion
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	@SuppressWarnings("ucd")
	public void onEntityExplode(EntityExplodeEvent event) {
		Entity entity = event.getEntity();
		Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext())
			removeSignIfNeeded(it.next(), entity);
	}

	/**
	 * Check if a block is powered above a sign.
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true)
	@SuppressWarnings("ucd")
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.getChangedType() != Material.SIGN || ! event.getBlock().isBlockIndirectlyPowered()) {
			return;
		}

		final Powerable myIC = this.MyPoweredICFactory.getIC(event.getBlock());

		if (myIC != null) {
			myIC.power();
		}

	}
	/*
	@EventHandler(ignoreCancelled = true)
	public void onBlockRedstone(BlockRedstoneEvent event) {
		Block block = event.getBlock().getRelative(BlockFace.DOWN);
		List<Block> blocks = new ArrayList<Block>(4);
		blocks.add(block.getRelative(BlockFace.NORTH));
		blocks.add(block.getRelative(BlockFace.EAST));
		blocks.add(block.getRelative(BlockFace.SOUTH));
		blocks.add(block.getRelative(BlockFace.WEST));

		for (Block b : blocks) {
			if (! AbstractIC.checkEligibility(b))
				continue;

			Powerable myIC = this.MyPoweredICFactory.getIC(b);


			if (myIC != null) {
				try {
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart: power()");

					myIC.power();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return;
		}

	}
*/
	/**
	 * Detect a sign that a player right-clicks
	 *
	 * @param event
	 */
	@EventHandler(ignoreCancelled = true)
	@SuppressWarnings("ucd")
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getAction().compareTo(Action.RIGHT_CLICK_BLOCK) != 0)
			return;
		Clickable myIC = ClickedSignFactory.getClickedIC(event.getClickedBlock(), event.getPlayer());

		if (myIC == null)
			myIC = ClickedSignFactory.getBackwardClickedIC(event.getClickedBlock(), event.getPlayer());

		if (myIC != null) {

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: " + myIC.getName() + ".click()");

			myIC.click();
			event.setCancelled(true);
		}
	}

	/**
	 * Remove sign from cache and launch event
	 *
	 * @param block the sign
	 * @param entity the entity at origin of the event
	 */
	private static void removeSignIfNeeded(Block block, Entity entity) {
		if (! (block.getState() instanceof Sign))
			return;

		IC myIC;
		myIC = TriggeredSignFactory.getTriggeredIC(block, null);

		if (myIC == null)
			myIC = ClickedSignFactory.getClickedIC(block, null);

		if (myIC != null) {
			Bukkit.getPluginManager().callEvent(new SignRemoveEvent(myIC, entity));
			AbstractIC.removeFromCache(block);
		}
	}
}
