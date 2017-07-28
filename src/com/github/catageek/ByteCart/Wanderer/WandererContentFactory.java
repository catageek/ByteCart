package com.github.catageek.ByteCart.Wanderer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.FileStorage.InventoryFile;
import com.github.catageek.ByteCartAPI.Wanderer.InventoryContent;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Level;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Scope;

abstract public class WandererContentFactory {

	public static WandererContent getWandererContent(Inventory inv)
			throws IOException, ClassNotFoundException {
		WandererContent rte = null;
		InventoryFile file = null;
		if (InventoryFile.isInventoryFile(inv, null)) {
			file = new InventoryFile(inv, true, null);
		}
		if (file == null) {
			throw new IOException("No book found");
		}
		if (! file.isEmpty()) {
			ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
			rte = (WandererContent) ois.readObject();
		}
		
		rte.setInventory(inv);
		return rte;
	}

	public static boolean isWanderer(Inventory inv) {
		String prefix = getType(inv);
		if (prefix != null)
			return ByteCart.myPlugin.getWandererManager().isWandererType(prefix);
		
		return false;
	}

	static String getType(Inventory inv) {
		ItemStack stack = inv.getItem(0);
		String prefix = null;
		if (stack != null && stack.getType().equals(Material.WRITTEN_BOOK) && stack.hasItemMeta()) {
			BookMeta book = (BookMeta) stack.getItemMeta();
			String booktitle = book.getTitle();
			int index = booktitle.indexOf(".");
			if (index > 0)
				prefix = booktitle.substring(0, index);
		}
		return prefix;
	}

	public static boolean isWanderer(Inventory inv, Scope scope) {
		return isWanderer(inv, scope, null, null);
	}

	public static boolean isWanderer(Inventory inv, Level level, String type) {
		return isWanderer(inv, level.scope, level.type, type);
	}

	public static boolean isWanderer(Inventory inv, String type) {
		return isWanderer(inv, null, null, type);
	}

	private static boolean isWanderer(Inventory inv, Scope scope, String suffix, String type) {
		if (! isWanderer(inv))
			return false;
		ItemStack stack = inv.getItem(0);
		if (stack != null && stack.getType().equals(Material.WRITTEN_BOOK) && stack.hasItemMeta()) {
			final BookMeta book = (BookMeta) stack.getItemMeta();
			final String booktitle = book.getTitle();
			final String dot = "\\.";
			final StringBuilder match = new StringBuilder();

			match.append("^");

			final String alphanums = "[a-zA-Z]{1,}";
			
			if (type != null)
				match.append(type).append(dot);
			else
				match.append(alphanums).append(dot);

			if (scope != null)
				match.append(scope.name).append(dot);
			else
				match.append(alphanums).append(dot);

			if (suffix != null)
				match.append(suffix).append(dot);

			match.append(".*");
			if (InventoryFile.isInventoryFile(inv, type) 
					&& booktitle.matches(match.toString())) {
					return true;
			}
		}
		return false;
	}

	private static InventoryFile createWanderer(Inventory inv, int region, Level level, Player player
			, String name, Level type) throws IOException {
		InventoryFile file = new InventoryFile(inv, true, name);
		String dot = ".";
		StringBuilder match = new StringBuilder();
		match.append(name).append(dot).append(level.scope.name);
		match.append(dot).append(type.type).append(dot);
		file.setDescription(match.toString());
		return file;
		
	}

	public static <T extends InventoryContent> void saveContent(T rte, String name, Level type)
			throws IOException, ClassNotFoundException {
		Inventory inv = rte.getInventory();

		InventoryFile file = createWanderer(inv, rte.getRegion(), rte.getLevel(), rte.getPlayer(), name, type);
		ObjectOutputStream oos = new ObjectOutputStream(file.getOutputStream());
		oos.writeObject(rte);
		try {
			oos.flush();
		}
		catch (IOException e) {
			deleteContent(inv);
			ByteCart.log.info("Bytecart: I/O error (maximum capacity reached), updater deleted");
		}
	}

	public static void deleteContent(Inventory inv) {
		inv.clear(0);
	}
}
