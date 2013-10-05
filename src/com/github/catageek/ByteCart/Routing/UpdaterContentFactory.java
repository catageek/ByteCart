package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCart.Util.LogUtil;
import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.Routing.Updater.Level;
import com.github.catageek.ByteCartAPI.Routing.Updater.Scope;

public abstract class UpdaterContentFactory {

	public static UpdaterContent getUpdaterContent(Inventory inv)
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

	public static void createRoutingTableExchange(Inventory inv, int region, Level level, Player player
			, boolean isfullreset, boolean isnew) throws IOException {
		WandererContentFactory.createWanderer(inv, region, level, player, "Updater", level.type);
		UpdaterContent rte;
		if (level.scope.equals(Scope.LOCAL))
			rte = new LocalUpdaterContent(inv, level, region, player, isfullreset);
		else
			rte = new UpdaterContent(inv, level, region, player, isfullreset, isnew);
		try {
			UpdaterContentFactory.saveContent(rte);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static <T extends UpdaterContent> void saveContent(T rte)
			throws IOException, ClassNotFoundException {

		// delete content if expired
		long creation = rte.getCreationtime();
		long expiration = rte.getExpirationTime();
		if (creation != expiration && Calendar.getInstance().getTimeInMillis() > expiration) {
			LogUtil.sendSuccess(rte.getPlayer(), "ByteCart : Updater created " + (new Date(rte.getCreationtime())).toString() + " expired");
			WandererContentFactory.deleteContent(rte.getInventory());
			return;
		}

		WandererContentFactory.saveContent(rte);
	}
}
