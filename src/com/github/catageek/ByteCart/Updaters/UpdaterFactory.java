package com.github.catageek.ByteCart.Updaters;

import java.io.IOException;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Level;
import com.github.catageek.ByteCartAPI.Wanderer.WandererFactory;

public final class UpdaterFactory implements WandererFactory {

	@Override
	public Wanderer getWanderer(BCSign bc, Inventory inv) throws ClassNotFoundException, IOException {
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
