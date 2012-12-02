package com.github.catageek.ByteCart;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BytecartCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("mego")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				if(args.length != 1 || !AddressString.isAddress(args[0]))
					return false;
				
				(new BC7010(player.getLocation().getBlock(), player)).setAddress(new AddressString(args[0]));
			}
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("followme")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				
				if(args.length != 1 || !AddressString.isAddress(args[0]))
					return false;
				
				player.sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.RightClickCart") );
				new ByteCartInventoryListener(ByteCart.myPlugin, player, new AddressString(args[0]));
			}
			return true;
		}

		return false;
	}

}
