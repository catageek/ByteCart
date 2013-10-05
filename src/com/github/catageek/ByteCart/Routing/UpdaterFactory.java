package com.github.catageek.ByteCart.Routing;

import java.io.IOException;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.Routing.Updater;
import com.github.catageek.ByteCartAPI.Routing.Updater.Level;
import com.github.catageek.ByteCartAPI.Signs.BCSign;

public class UpdaterFactory {
	
	public static Updater getUpdater(BCSign bc, Inventory inv) throws ClassNotFoundException, IOException {
		
		UpdaterContent rte;
		if (WandererContentFactory.isWanderer(inv, Level.REGION, "Updater")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterRegion(bc, rte);

		if (WandererContentFactory.isWanderer(inv, Level.RESET_REGION, "Updater")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterResetRegion(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.BACKBONE, "Updater")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterBackBone(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.RESET_BACKBONE, "Updater")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterResetBackbone(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.RESET_LOCAL, "Updater")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterResetLocal(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.LOCAL, "Updater")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterLocal(bc, rte);
		return null;
	}

}
