package com.github.catageek.ByteCart;

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
				
				player.sendMessage("mego command execution.");
			}
			return true;
		}
		return false;
	}

}
