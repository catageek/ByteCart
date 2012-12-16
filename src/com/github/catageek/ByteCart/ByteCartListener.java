package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.util.Vector;



public class ByteCartListener implements Listener {

	private PoweredICFactory MyPoweredICFactory;
	private final Vector NullVector = new Vector(0,0,0);


	ByteCartListener() {
		this.MyPoweredICFactory = new PoweredICFactory();
	}
	/*
	@EventHandler(ignoreCancelled = true)
	public void onVehicleUpdate(VehicleUpdateEvent event) {
		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: onVehiculeUpdate vehicule at " + event.getVehicle().getLocation());

	}
	 */

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
			
			// preload and postload chunks
			
			from_x >>= 4;
			from_z >>= 4;
			to_x >>= 4;
			to_z >>= 4;


			int a = from_x.compareTo(to_x);

			if(a != 0) {
				// we enter a new chunk
				if (a > 0)
					MathUtil.unloadChunkXAxis(loc.getWorld(), from_x + 2, from_z);
				else
					MathUtil.unloadChunkXAxis(loc.getWorld(), from_x - 2, from_z);

				MathUtil.loadChunkAround(loc.getWorld(), to_x, to_z);				
			}
			else {

				a = from_z.compareTo(to_z);

				if(a != 0){
					// we enter a new chunk
					if (a > 0)
						MathUtil.unloadChunkZAxis(loc.getWorld(), from_x, from_z + 2);
					else
						MathUtil.unloadChunkZAxis(loc.getWorld(), from_x, from_z - 2);

					MathUtil.loadChunkAround(loc.getWorld(), to_x, to_z);				
				}
			}


			// we instantiate a member of the BCXXXX class
			// XXXX is read from the sign

			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getTo().getBlock().getRelative(BlockFace.DOWN, 2),vehicle);

			Player player;
			int tax;

			if (myIC != null) {

				/*				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: " + myIC.getName() + ".trigger()");
				 */
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

			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(vehicle.getLocation().getBlock().getRelative(BlockFace.DOWN, 2),vehicle);

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
			TriggeredIC myIC = TriggeredICFactory.getTriggeredIC(event.getBlock(), event.getLine(1), null);

			if (myIC != null) {
				if (! event.getPlayer().hasPermission(myIC.getBuildPermission())) {
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You are not authorized to place " + myIC.getFriendlyName() + " block.");
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED +"You must have " + myIC.getBuildPermission());
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

		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: event " + event.getBlock().toString());
		 */
		PoweredIC myIC = this.MyPoweredICFactory.getIC(event.getBlock().getRelative(BlockFace.DOWN));


		if (myIC != null) {
			myIC.power();
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (event.getAction().compareTo(Action.RIGHT_CLICK_BLOCK) != 0 || !AbstractIC.checkEligibility(event.getClickedBlock()))
			return;
		ClickedIC myIC = ClickedICFactory.getClickedIC(event.getClickedBlock(), event.getPlayer());

		if (myIC != null) {

			if(ByteCart.debug)
				ByteCart.log.info("ByteCart: " + myIC.getName() + ".click()");

			myIC.click();
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onChunkUnload(ChunkUnloadEvent event) {

		Entity[] entities = event.getChunk().getEntities();
		int i = entities.length -1;

		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart: Chunk requested to be unloaded at " + event.getChunk());
		 */
		for (; i >=0; --i) {
			if (entities[i] instanceof StorageMinecart && !((StorageMinecart)entities[i]).getVelocity().equals(NullVector)) {

				event.setCancelled(true);

				ByteCart.myPlugin.getServer().getScheduler().scheduleSyncDelayedTask(ByteCart.myPlugin, new LoadChunks(event.getChunk()), 1);

				if(ByteCart.debug)
					ByteCart.log.info("ByteCart: Chunk kept loaded " + event.getChunk());
				return;
			}
		}
	}

	private static final class LoadChunks implements Runnable {

		private final Chunk Chunk;

		LoadChunks(Chunk chunk) {
			Chunk = chunk;
		}

		@Override
		public void run() {
			MathUtil.loadChunkAround(Chunk.getWorld(), Chunk.getX(), Chunk.getZ());
		}

	}
}
