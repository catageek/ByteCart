package com.github.catageek.ByteCart.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.catageek.ByteCart.plugins.BCDynmapPlugin;

public class CommandBCDMapSync implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		BCDynmapPlugin.removeObsoleteMarkers();
		return true;
	}
}
