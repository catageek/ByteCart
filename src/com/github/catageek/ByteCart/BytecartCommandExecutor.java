package com.github.catageek.ByteCart;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.AddressString;
import com.github.catageek.ByteCart.EventManagement.ByteCartInventoryListener;
import com.github.catageek.ByteCart.EventManagement.ByteCartUpdaterMoveListener;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.Routing.RoutingTableFactory;
import com.github.catageek.ByteCart.Routing.RoutingTableWritable;
import com.github.catageek.ByteCart.Signs.BC7010;
import com.github.catageek.ByteCart.Signs.BC7011;
import com.github.catageek.ByteCart.Signs.BC7017;
import com.github.catageek.ByteCart.Updaters.UpdaterContentFactory;
import com.github.catageek.ByteCart.Updaters.UpdaterFactory;
import com.github.catageek.ByteCart.Util.LogUtil;
import com.github.catageek.ByteCart.plugins.BCDynmapPlugin;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;
import com.google.gson.JsonSyntaxException;

/**
 * The command executor
 */
public class BytecartCommandExecutor implements CommandExecutor {

	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("mego")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;

				if(args.length == 0)
					return false;
				String host_or_address;
				boolean isTrain  = false;
				if (args[args.length-1].equalsIgnoreCase("train")) {
					isTrain   = true;
					host_or_address = concat(args, 0, 1);
				}
				else {
					host_or_address = concat(args, 0, 0);
				}

				if(!AddressString.isResolvableAddressOrName(host_or_address)) {
					sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "No valid destination supplied.");
					return false;
				}

				(new BC7010(player.getLocation().getBlock(), player)).setAddress(host_or_address, null, isTrain);
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("sendto")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;

				if(args.length == 0)
					return false;
				final String host_or_address;
				boolean isTrain  = false;
				if (args[args.length-1].equalsIgnoreCase("train")) {
					isTrain   = true;
					host_or_address = concat(args, 0, 1);
				}
				else {
					host_or_address = concat(args, 0, 0);
				}

				if(!AddressString.isResolvableAddressOrName(host_or_address)) {
					sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "No valid destination supplied.");
					return false;
				}

				final class Execute implements ModifiableRunnable<Inventory> {

					private final Player player;
					private final String address;
					private Inventory inventory;
					private boolean istrain;


					public Execute(Player player, String host_or_address, boolean isTrain) {
						this.player = player;
						this.address = host_or_address;
						this.istrain = isTrain;
					}

					public void run() {
						if ((new BC7011(player.getLocation().getBlock(), ((org.bukkit.entity.Vehicle) inventory.getHolder()))).setAddress(address, null, this.istrain)) {
							LogUtil.sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.SetAddress") + " " + host_or_address);
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
				new ByteCartInventoryListener(ByteCart.myPlugin, player, new Execute(player, host_or_address, isTrain), false);
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

			if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
				ByteCart.myPlugin.getWandererManager().unregister("Updater");
				ByteCartUpdaterMoveListener.clearUpdaters();
				return true;
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;

				int region = 0;

				if(args.length == 0 || args.length > 4 || !Wanderer.Level.isMember(args[0].toLowerCase()))
					return false;


				if (args.length == 1 && ! args[0].equalsIgnoreCase("backbone")
						&& ! args[0].equalsIgnoreCase("reset_backbone"))
					return false;

				boolean full_reset = false;
				boolean isnew = false;
				
				if (! ByteCart.myPlugin.getWandererManager().isWandererType("Updater"))
					ByteCart.myPlugin.getWandererManager().register(new UpdaterFactory(), "Updater");

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
					private final Wanderer.Level level;
					private final int region;
					private Inventory inventory;
					private boolean isfullreset;
					private boolean isnew;


					public Execute(Player player, Wanderer.Level level, int region, boolean isfullreset, boolean isnew) {
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
						,new Execute(player, Wanderer.Level.valueOf(args[0].toUpperCase()), region
								,full_reset, isnew)
				,true);
			}
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("bcdmapsync")) {
			BCDynmapPlugin.removeObsoleteMarkers();
			return true;
		}


		if (cmd.getName().equalsIgnoreCase("bcedit")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				final Player player = (Player) sender;
				final PlayerInventory inv = player.getInventory();
				if (! RoutingTableFactory.isRoutingTable(inv, player.getInventory().getHeldItemSlot())) {
					LogUtil.sendError(player, "You must hold a routing table book in your hand.");
					return false;
				}
				final ItemStack stack = player.getItemInHand();
				stack.setType(Material.BOOK_AND_QUILL);
				inv.setItemInHand(stack);
				player.updateInventory();
				return true;
			}
		}

		if (cmd.getName().equalsIgnoreCase("bcsign")) {
			final PlayerInventory inv;
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				final Player player = (Player) sender;
				final ItemStack oldstack = player.getItemInHand();
				final ItemStack stack = oldstack.clone();
				if (BookFile.sign(stack, "RoutingTable") == null) {
					LogUtil.sendError(player, "You must hold a routing table book in your hand.");
					return false;					
				}
				inv = player.getInventory();
				inv.setItemInHand(stack);
				try {
					final RoutingTableWritable rt = RoutingTableFactory.getRoutingTable(player.getInventory(), player.getInventory().getHeldItemSlot());
					if (rt == null) {
						LogUtil.sendError(player, "Error");
						inv.setItemInHand(oldstack);
						return true;
					}
				}
				catch(JsonSyntaxException e) {
					LogUtil.sendError(player, e.getLocalizedMessage());
					inv.setItemInHand(oldstack);
					return true;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.updateInventory();
				return true;
			}
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
		String addressString;
		boolean isTrain = false;

		if (!(sender instanceof Player)) {
			if(args.length < 2)
				return false;
			String host_or_address;
			if (args[args.length-1].equalsIgnoreCase("train")) {
				isTrain   = true;
				host_or_address = concat(args, 1, 1);
			}
			else {
				host_or_address = concat(args, 1, 0);
			}

			player = Bukkit.getServer().getPlayer(args[0]);
			addressString = host_or_address;

			if(player == null) {
				sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "Can't find player "+args[0]+".");
				return false;
			}
		} else {
			if(args.length < 1) {
				return false;
			}

			String host_or_address;
			if (args[args.length-1].equalsIgnoreCase("train")) {
				isTrain   = true;
				host_or_address = concat(args, 0, 1);
			}
			else {
				host_or_address = concat(args, 0, 0);
			}

			player = (Player) sender;
			addressString = host_or_address;
		}

		if(!AddressString.isResolvableAddressOrName(addressString)) {
			sender.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + "No valid address supplied.");
			return false;
		}

		(new BC7010(player.getLocation().getBlock(), player)).setAddress(addressString, null, isTrain);

		player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + "Ticket created successfully.");

		return true;
	}

	private String concat(String[] s, int first, int omitted) {
		if (s.length <= first) {
			return "";
		}
		String ret = s[first];
		for (int i = first + 1; i < s.length - omitted; ++i) {
			ret += " ";
			ret += s[i];
		}
		return ret;
	}
}
