package com.github.catageek.ByteCart.Routing;

import java.io.IOException;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.Wanderer.WandererContentFactory;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Level;

public class WandererFactory {
	
	public static Wanderer getWanderer(BCSign bc, Inventory inv) throws ClassNotFoundException, IOException {
		Wanderer u;
		if ((u = getUpdater(bc, inv)) != null)
			return u;
		return null;
	}
	
	private static Wanderer getUpdater(BCSign bc, Inventory inv) throws ClassNotFoundException, IOException {
		UpdaterContent rte;
		if (WandererContentFactory.isWanderer(inv, Level.REGION, "Wanderer")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterRegion(bc, rte);

		if (WandererContentFactory.isWanderer(inv, Level.RESET_REGION, "Wanderer")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterResetRegion(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.BACKBONE, "Wanderer")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterBackBone(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.RESET_BACKBONE, "Wanderer")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterResetBackbone(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.RESET_LOCAL, "Wanderer")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterResetLocal(bc, rte);
		
		if (WandererContentFactory.isWanderer(inv, Level.LOCAL, "Wanderer")
				&& (rte = UpdaterContentFactory.getUpdaterContent(inv)) != null)
			return new UpdaterLocal(bc, rte);
		return null;
	}

}
