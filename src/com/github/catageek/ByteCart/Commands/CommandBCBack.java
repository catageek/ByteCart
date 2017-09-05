package com.github.catageek.ByteCart.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.Signs.BC7017;
import com.github.catageek.ByteCart.Util.LogUtil;

public class CommandBCBack implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		}

		Player player = (Player) sender;

		(new BC7017(player.getLocation().getBlock(), player)).trigger();

		LogUtil.sendSuccess(player, "Return back");

		return true;
	}

}
