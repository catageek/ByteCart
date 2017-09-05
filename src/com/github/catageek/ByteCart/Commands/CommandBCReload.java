package com.github.catageek.ByteCart.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Util.LogUtil;

public class CommandBCReload implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
}
