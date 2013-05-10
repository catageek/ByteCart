package com.github.catageek.ByteCart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import com.github.catageek.ByteCart.EventManagement.ByteCartInventoryListener;
import com.github.catageek.ByteCart.EventManagement.ByteCartUpdaterMoveListener;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Routing.AddressRouted;
import com.github.catageek.ByteCart.Routing.AddressString;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Signs.BC7010;
import com.github.catageek.ByteCart.Signs.BC7011;
import com.github.catageek.ByteCart.Signs.BC7017;

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

				if (args.length == 2 && args[1].equalsIgnoreCase("train"))
					address.setTrain(true);

				(new BC7010(player.getLocation().getBlock(), player)).setAddress(address, null);
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

				if (args.length == 2 && args[1].equalsIgnoreCase("train"))
					address.setTrain(true);

				final class Execute implements ModifiableRunnable<Inventory> {

					private final Player player;
					private final Address address;
					private Inventory inventory;


					public Execute(Player player, Address address) {
						this.player = player;
						this.address = address;
					}

					public void run() {
						if ((new BC7011(player.getLocation().getBlock(), ((org.bukkit.entity.Vehicle) inventory.getHolder()))).setAddress(address, null)) {
							sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.SetAddress"));
							sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.GetTTL") + AddressFactory.<AddressRouted>getAddress(inventory).getTTL());
						}
						else
							sendError(player, ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );

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
				new ByteCartInventoryListener(ByteCart.myPlugin, player, new Execute(player, address), false);
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
				sendError(player, s);
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

			sendSuccess(player, "Return back");

			return true;
		}


		if (cmd.getName().equalsIgnoreCase("bcupdater")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;

				if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
					ByteCart.myPlugin.getUm().getMapRoutes().clear();
					return true;
				}

				int region = 0;

				if(args.length == 0 || args.length > 3 || !Updater.Level.isMember(args[0].toLowerCase()))
					return false;


				if (args.length == 1 && ! args[0].equalsIgnoreCase("backbone")
						&& ! args[0].equalsIgnoreCase("reset_backbone"))
					return false;

				if (args.length == 2){

					if (! args[0].equalsIgnoreCase("region")
							&& ! args[0].equalsIgnoreCase("local")
							&& ! args[0].equalsIgnoreCase("reset_region")
							&& ! args[0].equalsIgnoreCase("reset_local"))
						return false;

					region = Integer.parseInt(args[1]);

					if (region < 1 || region > 53)
						return false;
				}


				final class Execute implements ModifiableRunnable<Inventory> {

					private final Player player;
					private final Updater.Level level;
					private final int region;
					private Inventory inventory;


					public Execute(Player player, Updater.Level level, int region) {
						this.player = player;
						this.level = level;
						this.region = region;
					}

					public void run() {
						int id = ((StorageMinecart) inventory.getHolder()).getEntityId();
						ByteCart.myPlugin.getUm().addUpdater(id, level, region);
						ByteCart.myPlugin.getWm().addWanderer(id, region);
						if (! ByteCartUpdaterMoveListener.isExist()) {
							Listener updatermove = new ByteCartUpdaterMoveListener();
							ByteCart.myPlugin.getServer().getPluginManager().registerEvents(updatermove, ByteCart.myPlugin);
							ByteCartUpdaterMoveListener.setExist(true);
						}
						sendError(player, ByteCart.myPlugin.getConfig().getString("Info.SetUpdater") );
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

				sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.RightClickCart") );

				new ByteCartInventoryListener(ByteCart.myPlugin, player, new Execute(player, Updater.Level.valueOf(args[0].toUpperCase()), region), true);
			}
			return true;
		}

		return false;
	}

	protected void sendError(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + message);
	}

	protected void sendSuccess(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + message);
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
