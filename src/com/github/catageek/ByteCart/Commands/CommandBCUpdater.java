package com.github.catageek.ByteCart.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.ModifiableRunnable;
import com.github.catageek.ByteCart.EventManagement.ByteCartInventoryListener;
import com.github.catageek.ByteCart.EventManagement.ByteCartUpdaterMoveListener;
import com.github.catageek.ByteCart.Updaters.UpdaterFactory;
import com.github.catageek.ByteCart.Util.LogUtil;
import com.github.catageek.ByteCart.Wanderer.BCWandererManager;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;

public class CommandBCUpdater implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
			BCWandererManager wm = ByteCart.myPlugin.getWandererManager();
			wm.getFactory("Updater").removeAllWanderers();
			wm.unregister("Updater");
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
		} else {
			Player player = (Player) sender;

			int region = 0;

			if(args.length == 0 || args.length > 4 || !Wanderer.Level.isMember(args[0].toLowerCase()))
				return false;


			if (args.length == 1 && ! args[0].equalsIgnoreCase("backbone")
					&& ! args[0].equalsIgnoreCase("reset_backbone"))
				return false;

			boolean full_reset = false;
			boolean isnew = false;

			if (! ByteCart.myPlugin.getWandererManager().isRegistered("Updater"))
				ByteCart.myPlugin.getWandererManager().register(new UpdaterFactory(), "Updater");

			if (args.length >= 2){

				if (! args[0].equalsIgnoreCase("region")
						&& ! args[0].equalsIgnoreCase("local")
						&& ! args[0].equalsIgnoreCase("reset_region")
						&& ! args[0].equalsIgnoreCase("reset_local"))
					return false;

				region = Integer.parseInt(args[1]);

				if (region < 1 || region > 2047)
					return false;

				if (args.length == 3) {
					if (args[0].startsWith("reset")) {
						if (args[2].equalsIgnoreCase("full"))
							full_reset  = true;
						else
							return false;
					}
					else
						if (args[2].equalsIgnoreCase("new"))
							isnew = true;
						else
							return false;
				}
			}


			final class Execute implements ModifiableRunnable<Inventory> {

				private final Player player;
				private final Wanderer.Level level;
				private final int region;
				private Inventory inventory;
				private boolean isfullreset;
				private boolean isnew;


				private Execute(Player player, Wanderer.Level level, int region, boolean isfullreset, boolean isnew) {
					this.player = player;
					this.level = level;
					this.region = region;
					this.isfullreset = isfullreset;
					this.isnew = isnew;
				}

				@Override
				public void run() {
					int id = ((StorageMinecart) inventory.getHolder()).getEntityId();
					// register updater factory
					final BCWandererManager wm = ByteCart.myPlugin.getWandererManager();
					if (! wm.isRegistered("Updater"))
						wm.register(new UpdaterFactory(), "Updater");
					wm.getFactory("Updater").createWanderer(id, inventory, region, level, player, isfullreset, isnew);
					if (! ByteCartUpdaterMoveListener.isExist()) {
						Listener updatermove = new ByteCartUpdaterMoveListener();
						ByteCart.myPlugin.getServer().getPluginManager().registerEvents(updatermove, ByteCart.myPlugin);
						ByteCartUpdaterMoveListener.setExist(true);
					}
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

			LogUtil.sendSuccess(player, ByteCart.myPlugin.getConfig().getString("Info.RightClickCart") );

			new ByteCartInventoryListener(ByteCart.myPlugin, player
					,new Execute(player, Wanderer.Level.valueOf(args[0].toUpperCase()), region
							,full_reset, isnew)
			,true);
		}
		return true;
	}
}
