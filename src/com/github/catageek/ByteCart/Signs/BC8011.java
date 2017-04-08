package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.Routing.RoutingTableFactory;
import com.github.catageek.ByteCart.Routing.RoutingTableWritable;
import com.github.catageek.ByteCartAPI.Signs.BCRouter;

public final class BC8011 extends BC8010 implements BCRouter, Triggable, HasRoutingTable {

	public BC8011(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle)
			throws ClassNotFoundException, IOException {
		super(block, vehicle, false);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#loadChest()
	 */
	protected RoutingTableWritable loadChest() throws ClassNotFoundException, IOException {
		BlockState blockstate;
		if ((blockstate = getCenter().getState()) instanceof InventoryHolder) {
			// Loading inventory of chest at same level of the sign
			Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

			// Converting inventory in routing table
			return RoutingTableFactory.getRoutingTable(ChestInventory, 0);
		}
		else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#getName()
	 */
	@Override
	public String getName() {
		return "BC8011";
	}

}
