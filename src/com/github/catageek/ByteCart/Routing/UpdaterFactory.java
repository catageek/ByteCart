package com.github.catageek.ByteCart.Routing;

import java.io.IOException;
import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Signs.BCSign;

public class UpdaterFactory {
	
	public static Updater getUpdater(BCSign bc, Inventory inv) throws ClassNotFoundException, IOException {
		
		UpdaterContent rte;
		if (UpdaterContentFactory.isRoutingTableExchange(inv, Level.REGION)
				&& (rte = UpdaterContentFactory.getRoutingTableExchange(inv)) != null)
			return new UpdaterRegion(bc, rte);

		if (UpdaterContentFactory.isRoutingTableExchange(inv, Level.RESET_REGION)
				&& (rte = UpdaterContentFactory.getRoutingTableExchange(inv)) != null)
			return new UpdaterResetRegion(bc, rte);
		
		if (UpdaterContentFactory.isRoutingTableExchange(inv, Level.BACKBONE)
				&& (rte = UpdaterContentFactory.getRoutingTableExchange(inv)) != null)
			return new UpdaterBackBone(bc, rte);
		
		if (UpdaterContentFactory.isRoutingTableExchange(inv, Level.RESET_BACKBONE)
				&& (rte = UpdaterContentFactory.getRoutingTableExchange(inv)) != null)
			return new UpdaterResetBackbone(bc, rte);
		
		if (UpdaterContentFactory.isRoutingTableExchange(inv, Level.RESET_LOCAL)
				&& (rte = UpdaterContentFactory.getRoutingTableExchange(inv)) != null)
			return new UpdaterResetLocal(bc, rte);
		
		if (UpdaterContentFactory.isRoutingTableExchange(inv, Level.LOCAL)
				&& (rte = UpdaterContentFactory.getRoutingTableExchange(inv)) != null)
			return new UpdaterLocal(bc, rte);
		return null;
	}

}
