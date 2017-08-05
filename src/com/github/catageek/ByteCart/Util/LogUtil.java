package com.github.catageek.ByteCart.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.ByteCart;

public final class LogUtil {
	public static void sendError(CommandSender sender, String message) {
		display(sender, ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.RED + message);
	}

	public static void sendSuccess(CommandSender sender, String message) {
		display(sender, ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + message);
	}
	
	private static void display(CommandSender sender, String message) {
		if (sender != null && (sender instanceof Player) && ((Player) sender).isOnline())
			sender.sendMessage(message);
		else
			ByteCart.log.info(ChatColor.stripColor(message));
	}
}
