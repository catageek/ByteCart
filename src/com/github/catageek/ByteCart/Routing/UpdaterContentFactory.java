package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.FileStorage.BCFile;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Routing.Updater.Scope;
import com.github.catageek.ByteCart.Util.LogUtil;

abstract public class UpdaterContentFactory {

	private static final String titleprefix = "Updater";
	public static boolean mustRemove = false;

	public static UpdaterContent getRoutingTableExchange(Inventory inv)
			throws IOException, ClassNotFoundException {
		UpdaterContent rte = null;
		try (BookFile file = new BookFile(inv, 0, true)) {
			if (! file.isEmpty()) {
				ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
				rte = (UpdaterContent) ois.readObject();
			}
		}
		rte.setInventory(inv);
		return rte;
	}

	public static boolean isRoutingTableExchange(Inventory inv) {
		return isRoutingTableExchange(inv, null, null);
	}

	public static boolean isRoutingTableExchange(Inventory inv, Scope scope) {
		return isRoutingTableExchange(inv, scope, null);
	}

	public static boolean isRoutingTableExchange(Inventory inv, Level level) {
		return isRoutingTableExchange(inv, level.scope, level.type);
	}

	public static boolean isRoutingTableExchange(Inventory inv, Scope scope, String type) {
		ItemStack stack = inv.getItem(0);
		if (stack != null && stack.getTypeId() == Material.WRITTEN_BOOK.getId() && stack.hasItemMeta()) {
			BookMeta book = (BookMeta) stack.getItemMeta();
			String booktitle = book.getTitle();
			String dot = "\\.";
			StringBuilder match = new StringBuilder();

			match.append("^").append(titleprefix).append(dot);

			if (scope != null)
				match.append(scope.name).append(dot);
			else
				match.append("[a-zA-Z]{1,}").append(dot);

			if (type != null)
				match.append(type).append(dot);

			match.append(".*");
			if (BookFile.isBookFile(inv, 0) 
					&& booktitle.matches(match.toString())) {
				if (! mustRemove)
					return true;
				deleteRoutingTableExchange(inv);
			}
		}
		return false;
	}

	public static void createRoutingTableExchange(Inventory inv, int region, Level level, Player player
			, boolean isfullreset, boolean isnew) throws IOException {
		try (BCFile file = new BookFile(inv, 0, true, "Updater")) {
			String dot = ".";
			StringBuilder match = new StringBuilder();
			match.append(titleprefix).append(dot).append(level.scope.name);
			match.append(dot).append(level.type).append(dot);
			file.setDescription(match.toString());
			file.flush();
		}
		UpdaterContent rte;
		if (level.scope.equals(Scope.LOCAL))
			rte = new LocalUpdaterContent(inv, level, region, player, isfullreset);
		else
			rte = new UpdaterContent(inv, level, region, player, isfullreset, isnew);
		try {
			UpdaterContentFactory.saveRoutingTableExchange(rte);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mustRemove = false;
	}

	public static <T extends UpdaterContent> void saveRoutingTableExchange(T rte)
			throws IOException, ClassNotFoundException {
		Inventory inv = rte.getInventory();

		// delete content if expired
		long creation = rte.getCreationtime();
		long expiration = rte.getExpirationTime();
		if (creation != expiration && Calendar.getInstance().getTimeInMillis() > expiration) {
			LogUtil.sendSuccess(rte.getPlayer(), "ByteCart : Updater created " + (new Date(rte.getCreationtime())).toString() + " expired");
			deleteRoutingTableExchange(inv);
			return;
		}

		try (BCFile file = new BookFile(inv, 0, true)) {
			file.clear();
			ObjectOutputStream oos = new ObjectOutputStream(file.getOutputStream());
			oos.writeObject(rte);
			oos.flush();
		}
	}

	public static void deleteRoutingTableExchange(Inventory inv) {
		inv.clear(0);
	}
}
