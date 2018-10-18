package com.github.catageek.ByteCart.Commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.github.catageek.ByteCart.AddressLayer.AddressString;
import com.github.catageek.ByteCartAPI.ByteCartPlugin;
import com.google.common.collect.Lists;

public abstract class AbstractTicketCommand {
	protected final ByteCartPlugin plugin;
	protected AbstractTicketCommand(ByteCartPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Parses command arguments and then calls {@link #run}
	 *
	 * @param sender The entity that called the command
	 * @param player The player to execute on
	 * @param startIndex The index at which to start parsing the address
	 * @param args Arguments to parse from.
	 * @return true if successful
	 */
	protected final boolean parse(CommandSender sender, Player player, int startIndex, String[] args) {
		if (args.length <= startIndex) {
			return false;
		}

		String addressString;
		boolean isTrain = false;

		if (args[args.length-1].equalsIgnoreCase("train")) {
			isTrain = true;
			addressString = concat(args, startIndex, 1);
		} else {
			addressString = concat(args, startIndex, 0);
		}

		if (!AddressString.isResolvableAddressOrName(addressString)) {
			sender.sendMessage(ChatColor.DARK_GREEN + "[Bytecart] " + ChatColor.RED + "No valid destination supplied.");
			return false;
		}

		return run(sender, player, addressString, isTrain);
	}

	/**
	 * Gets a list of possible tab completion options.
	 */
	protected final List<String> tabComplete(int startIndex, String[] args) {
		if (args.length <= startIndex) {
			return Collections.emptyList();
		}

		String written = concat(args, startIndex, 1);
		List<String> options = Lists.newArrayList();
		if (plugin.getResolver() != null) {
			String prefix = concat(args, startIndex, 0);
			List<String> names = plugin.getResolver().getMatchingNames(prefix);
			for (String name : names) {
				name = name.substring(prefix.length());
				if (name.contains(" ")) {
					name = name.substring(0, name.indexOf(' '));
				}
				options.add(name);
			}
		}
		if (StringUtil.startsWithIgnoreCase("train", args[args.length - 1])) {
			if (AddressString.isResolvableAddressOrName(written)) {
				options.add("train");
			}
		}
		return options;
	}

	/**
	 * Performs actual command logic.
	 *
	 * @param sender The entity that called the command
	 * @param player The player to execute on
	 * @param addressString The address parsed from command arguments
	 * @param isTrain True if this is a train
	 * @return true if successful
	 */
	protected abstract boolean run(CommandSender sender, Player player, String addressString, boolean isTrain);

	private static String concat(String[] s, int first, int omitted) {
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
