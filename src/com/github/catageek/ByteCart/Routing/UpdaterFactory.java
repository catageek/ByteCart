package com.github.catageek.ByteCart.Routing;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.Updater.Level;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Storage.UpdaterManager;

public class UpdaterFactory {
	
	public static Updater getUpdater(BCSign bc) {
		int id = bc.getVehicle().getEntityId();
		UpdaterManager manager = ByteCart.myPlugin.getUm();
		if (manager.isUpdater(id, Level.REGION))
			return new UpdaterRegion(bc);
		if (manager.isUpdater(id, Level.RESET_REGION))
			return new UpdaterResetRegion(bc);
		if (manager.isUpdater(id, Level.BACKBONE))
			return new UpdaterBackBone(bc);
		if (manager.isUpdater(id, Level.RESET_BACKBONE))
			return new UpdaterResetBackbone(bc);
		if (manager.isUpdater(id, Level.RESET_LOCAL))
			return new UpdaterResetLocal(bc);
		if (manager.isUpdater(id, Level.LOCAL))
			return new UpdaterLocal(bc);
		return null;
	}
}
