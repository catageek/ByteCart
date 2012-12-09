package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.Inventory;

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
				
				(new BC7010(player.getLocation().getBlock(), player)).setAddress(address);
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
						if ((new BC7011(player.getLocation().getBlock(), ((StorageMinecart) inventory.getHolder()))).setAddress(address)) {
							player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetAddress"));
							player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.GetTTL") + AddressFactory.<AddressRouted>getAddress(inventory).getTTL());
						}
						else
							player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Error.SetAddress") );

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
				new ByteCartInventoryListener(ByteCart.myPlugin, player, new Execute(player, address));
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("bcupdater")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				
				if(args.length != 0)
					return false;

				final class Execute implements ModifiableRunnable<Inventory> {

					private final Player player;
					private Inventory inventory;


					public Execute(Player player) {
						this.player = player;
					}

					public void run() {
						int id = ((StorageMinecart) inventory.getHolder()).getEntityId();
						ByteCart.myPlugin.getUm().getMap().createEntry(id, null);
						player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + ByteCart.myPlugin.getConfig().getString("Info.SetUpdater") );

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
				new ByteCartInventoryListener(ByteCart.myPlugin, player, new Execute(player));
			}
			return true;
		}

		return false;
	}

}