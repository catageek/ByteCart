package com.github.catageek.ByteCart.Updaters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.FileStorage.InventoryFile;
import com.github.catageek.ByteCart.Util.LogUtil;
import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Level;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Scope;

public abstract class UpdaterContentFactory {

	public static UpdaterContent getUpdaterContent(Inventory inv)
			throws IOException, ClassNotFoundException {
		UpdaterContent rte = null;
		InventoryFile file = null;
		if (InventoryFile.isInventoryFile(inv, "Updater")) {
			file = new InventoryFile(inv, true, "Updater");
		}
		if (file == null) {
			return null;
		}
		if (! file.isEmpty()) {
			ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
			rte = (UpdaterContent) ois.readObject();
		}
		rte.setInventory(inv);
		return rte;
	}

	public static void createRoutingTableExchange(Inventory inv, int region, Level level, Player player
			, boolean isfullreset, boolean isnew) throws IOException {
		UpdaterContent rte;
		if (level.scope.equals(Scope.LOCAL))
			rte = new UpdaterContent(inv, level, region, player, isfullreset);
		else
			rte = new UpdaterContent(inv, level, region, player, isfullreset, isnew);
		try {
			saveContent(rte, "Updater", level);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void saveContent(UpdaterContent rte, String name, Level type)
			throws IOException, ClassNotFoundException {

		// delete content if expired
		long creation = rte.getCreationtime();
		long expiration = rte.getExpirationTime();
		if (creation != expiration && Calendar.getInstance().getTimeInMillis() > expiration) {
			LogUtil.sendSuccess(rte.getPlayer(), "ByteCart : Updater created " + (new Date(rte.getCreationtime())).toString() + " expired");
			WandererContentFactory.deleteContent(rte.getInventory());
			return;
		}

		WandererContentFactory.saveContent(rte, name, type);
	}

}
