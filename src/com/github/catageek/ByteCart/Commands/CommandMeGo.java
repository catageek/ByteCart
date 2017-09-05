package com.github.catageek.ByteCart.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.Signs.BC7010;

public class CommandMeGo extends AbstractTicketCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		} else {
			return parse(sender, (Player) sender, 0, args);
		}
	}

	@Override
	protected boolean run(CommandSender sender, Player player, String addressString, boolean isTrain) {
		(new BC7010(player.getLocation().getBlock(), player)).setAddress(addressString, null, isTrain);
		return true;
	}
}
