package com.github.catageek.ByteCart.Wanderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;

import com.github.catageek.ByteCartAPI.Wanderer.WandererManager;
import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCartAPI.Wanderer.InventoryContent;
import com.github.catageek.ByteCartAPI.Wanderer.WandererFactory;

public class BCWandererManager implements WandererManager {

	private static final Map<String, WandererFactory> map = new HashMap<String, WandererFactory>();

	/**
	 * Register a wanderer type
	 * 
	 * @param wanderer the wanderer class implementing the wanderer
	 * @param type the name that will reference this type of wanderer
	 */
	public boolean register(WandererFactory factory, String type) {
		if (map.containsKey(type))
			return false;
		map.put(type, factory);
		return true;
	}

	/**
	 * Turn a cart into a wanderer
	 * 
	 * @param ivc the content of the wanderer
	 * @param type the name of the type of wanderer previously registered
	 * @param suffix a suffix to add to book title
	 */
	public boolean create(InventoryContent ivc, String type, String suffix) {
		if (! map.containsKey(type))
			return false;
		try {
			WandererContentFactory.createWanderer(ivc.getInventory(), ivc.getRegion(), ivc.getLevel(),
					ivc.getPlayer(), type, suffix);
			WandererContentFactory.saveContent(ivc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Get a wanderer
	 * 
	 * @param bc the sign that request the wanderer
	 * @param inv the inventory where to extract the wanderercontent from
	 * @return the wanderer
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public WandererFactory getFactory(Inventory inv) throws ClassNotFoundException, IOException {
		if (WandererContentFactory.isWanderer(inv)
				&& WandererContentFactory.getWandererContent(inv) != null)
			return map.get(WandererContentFactory.getType(inv));
		return null;
	}

	/**
	 * Tells if this type is registered as a wanderer type
	 * 
	 * @param type the type to test
	 * @return true if the type is registered
	 */
	public boolean isWandererType(String type) {
		return map.containsKey(type);
	}

	public void saveContent(InventoryContent rte) throws ClassNotFoundException, IOException {

		WandererContentFactory.saveContent(rte);
	}

	@Override
	public void unregister(String name) {
		map.remove(name);
	}
}
