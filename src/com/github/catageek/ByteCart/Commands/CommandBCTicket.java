package com.github.catageek.ByteCart.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.Signs.BC7010;
import com.github.catageek.ByteCartAPI.ByteCartPlugin;

public class CommandBCTicket extends AbstractTicketCommand implements CommandExecutor, TabCompleter {

	public CommandBCTicket(ByteCartPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length < 2)
				return false;
			@SuppressWarnings("deprecation")
			Player player = Bukkit.getServer().getPlayer(args[0]);

			if(player == null) {
				sender.sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.RED + "Can't find player " + args[0] + ".");
				return false;
			}

			return parse(sender, player, 1, args);
		} else {
			return parse(sender, (Player) sender, 0, args);
		}
	}

	@Override
	protected boolean run(CommandSender sender, Player player, String addressString, boolean isTrain) {
		(new BC7010(player.getLocation().getBlock(), player)).setAddress(addressString, null, isTrain);

		player.sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.YELLOW + "Ticket created successfully.");

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			if (args.length <= 1) {
				// List players
				return null;
			} else {
				return tabComplete(1, args);
			}
		} else {
			return tabComplete(0, args);
		}
	}
}
