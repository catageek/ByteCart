package com.github.catageek.ByteCart;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.AddressString;
import com.github.catageek.ByteCart.EventManagement.ByteCartInventoryListener;
import com.github.catageek.ByteCart.EventManagement.ByteCartUpdaterMoveListener;
import com.github.catageek.ByteCart.Routing.UpdaterContentFactory;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Signs.BC7010;
import com.github.catageek.ByteCart.Signs.BC7011;
import com.github.catageek.ByteCart.Signs.BC7017;
import com.github.catageek.ByteCart.Util.LogUtil;
import com.github.catageek.ByteCart.plugins.BCDynmapPlugin;

public class BytecartCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("mego")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;

				if(args.length == 0 || args.length >= 3 || !AddressString.isAddress(args[0]))
					return false;

				Address address = new AddressString(args[0]);

				boolean isTrain  = false;
				if (args.length == 2 && args[1].equalsIgnoreCase("train"))
					isTrain   = true;

				(new BC7010(player.getLocation().getBlock(), player)).setAddress(address, null, isTrain);
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("sendto")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;

				if(args.length == 0 || args.length >= 3 || !AddressString.isAddress(args[0]))
					return false;

				Address address = new AddressString(args[0]);

				boolean isTrain  = false;
				if (args.length == 2 && args[1].equalsIgnoreCase("train"))
					isTrain   = true;

				final class Execute implements ModifiableRunnable<Inventory> {

					private final Player player;
					private final Address address;
					private Inventory inventory;
					private boolean istrain;


					public Execute(Player player, Address address, boolean isTrain) {
						this.player = player;
						this.address = address;
						this.istrain = isTrain;
					}

					public void run() {
						if ((new BC7011(player.getLocation().getBlock(), ((org.bukkit.entity.Vehicle) inventory.getHolder()))).setAddress(address, null, this.istrain)) {
							LogUtil.sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " " + address);
							LogUtil.sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.GetTTL") + AddressFactory.<AddressRouted>getAddress(inventory).getTTL());
						}
						else
							LogUtil.sendError(player, ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );

					}


					/**
					 * @param inventory
					 * @param inventory the inventory to set
					 */

					@Override
					public void SetParam(Inventory inventory) {
						this.inventory = inventory;
					}

				}


				player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.RightClickCart") );
				new ByteCartInventoryListener(ByteCart.myPlugin, player, new Execute(player, address, isTrain), false);
			}
			return true;
		}


		if (cmd.getName().equalsIgnoreCase("bcreload")) {
			ByteCart.myPlugin.reloadConfig();
			ByteCart.myPlugin.loadConfig();

			String s = "Configuration file reloaded.";

			if (!(sender instanceof Player)) {
				sender.sendMessage(s);
			} else {
				Player player = (Player) sender;
				LogUtil.sendError(player, s);
			}

			return true;
		}


		if (cmd.getName().equalsIgnoreCase("bcticket")) {
			return bcticket(sender, cmd, label, args);
		}


		if (cmd.getName().equalsIgnoreCase("bcback")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
				return true;
			}

			Player player = (Player) sender;

			(new BC7017(player.getLocation().getBlock(), player)).trigger();

			LogUtil.sendSuccess(player, "Return back");

			return true;
		}


		if (cmd.getName().equalsIgnoreCase("bcupdater")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;

				if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
					UpdaterContentFactory.mustRemove = true;
					return true;
				}

				int region = 0;

				if(args.length == 0 || args.length > 4 || !Updater.Level.isMember(args[0].toLowerCase()))
					return false;


				if (args.length == 1 && ! args[0].equalsIgnoreCase("backbone")
						&& ! args[0].equalsIgnoreCase("reset_backbone"))
					return false;

				boolean full_reset = false;
				boolean isnew = false;
				if (args.length >= 2){

					if (! args[0].equalsIgnoreCase("region")
							&& ! args[0].equalsIgnoreCase("local")
							&& ! args[0].equalsIgnoreCase("reset_region")
							&& ! args[0].equalsIgnoreCase("reset_local"))
						return false;

					region = Integer.parseInt(args[1]);

					if (region < 1 || region > 2047)
						return false;

					if (args.length == 3) {
						if (args[0].startsWith("reset")) {
							if (args[2].equalsIgnoreCase("full"))
								full_reset  = true;
							else
								return false;
						}
						else
							if (args[2].equalsIgnoreCase("new"))
								isnew = true;
							else
								return false;
					}
				}


				final class Execute implements ModifiableRunnable<Inventory> {

					private final Player player;
					private final Updater.Level level;
					private final int region;
					private Inventory inventory;
					private boolean isfullreset;
					private boolean isnew;


					public Execute(Player player, Updater.Level level, int region, boolean isfullreset, boolean isnew) {
						this.player = player;
						this.level = level;
						this.region = region;
						this.isfullreset = isfullreset;
						this.isnew = isnew;
					}

					public void run() {
						int id = ((StorageMinecart) inventory.getHolder()).getEntityId();
						try {
							UpdaterContentFactory.createRoutingTableExchange(inventory, region, level, player, isfullreset, isnew);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (! ByteCartUpdaterMoveListener.isExist()) {
							Listener updatermove = new ByteCartUpdaterMoveListener();
							ByteCart.myPlugin.getServer().getPluginManager().registerEvents(updatermove, ByteCart.myPlugin);
							ByteCartUpdaterMoveListener.setExist(true);
						}
						ByteCartUpdaterMoveListener.addUpdater(id);
						LogUtil.sendError(player, ByteCart.myPlugin.getConfig().getString("Info.SetUpdater") );
					}


					/**
					 * @param inventory
					 * @param inventory the inventory to set
					 */

					@Override
					public void SetParam(Inventory inventory) {
						this.inventory = inventory;
					}

				}

				LogUtil.sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.RightClickCart") );

				new ByteCartInventoryListener(ByteCart.myPlugin, player
						,new Execute(player, Updater.Level.valueOf(args[0].toUpperCase()), region
								,full_reset, isnew)
				,true);
			}
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("bcdmapsync")) {
			BCDynmapPlugin.removeObsoleteMarkers();
			return true;
		}

		return false;
	}

	/**
	 * bcticket command.
	 *
	 * Usage: /bcticket destination [isTrain]
	 *     OR /bcticket player destination [isTrain]
	 *
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @return True on success of the command.
	 */
	protected boolean bcticket(CommandSender sender, Command cmd, String label, String[] args) {
		Player player;
		Address destination;
		String addressString;
		boolean isTrain = false;

		if (!(sender instanceof Player)) {
			if(args.length < 2) {
				return false;
			}

			if(!AddressString.isAddress(args[1])) {
				sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "No valid address supplied.");
				return false;
			}

			player = Bukkit.getServer().getPlayer(args[0]);
			addressString = args[1];
			isTrain = (args.length == 3 && args[2].equalsIgnoreCase("train"));

			if(player == null) {
				sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "Can't find player "+args[0]+".");
				return false;
			}
		} else {
			if(args.length < 1) {
				return false;
			}

			player = (Player) sender;
			addressString = args[0];
			isTrain = (args.length == 2 && args[1].equalsIgnoreCase("train"));
		}

		if(!AddressString.isAddress(addressString)) {
			sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "No valid address supplied.");
			return false;
		}

		destination = new AddressString(addressString);
		destination.setTrain(isTrain);

		(new BC7010(player.getLocation().getBlock(), player)).setAddress(destination, null);

		player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + "Ticket created successfully.");

		return true;
	}

}
