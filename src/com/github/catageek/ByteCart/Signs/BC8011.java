package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Routing.RoutingTableFactory;
import com.github.catageek.ByteCart.Routing.RoutingTableWritable;
import com.github.catageek.ByteCartAPI.Signs.BCRouter;
import com.google.gson.JsonSyntaxException;

final class BC8011 extends BC8010 implements BCRouter, Triggable, HasRoutingTable {

	BC8011(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle, false);
	}

	/* (non-Javadoc)
	 * @see com.github.catageek.ByteCart.Signs.BC8010#loadChest()
	 */
	protected RoutingTableWritable loadChest() {
		BlockState blockstate;
		if ((blockstate = getCenter().getState()) instanceof InventoryHolder) {
			// Loading inventory of chest at same level of the sign
			Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

			// Converting inventory in routing table
			try {
				return RoutingTableFactory.getRoutingTable(ChestInventory, 0);
			} catch (JsonSyntaxException | ClassNotFoundException | IOException e) {
				ByteCart.log.info("ByteCart: Error while loading chest at position " + this.getCenter().getLocation() + ". Please clean its content and run bcupdater region command.");
				return null;
			}
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
