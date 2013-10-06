package com.github.catageek.ByteCart.Wanderer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.github.catageek.ByteCart.FileStorage.BCFile;
import com.github.catageek.ByteCart.FileStorage.BookFile;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Level;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Scope;

abstract public class WandererContentFactory {

	public static boolean mustRemove = false;

	public static WandererContent getWandererContent(Inventory inv)
			throws IOException, ClassNotFoundException {
		WandererContent rte = null;
		try (BookFile file = new BookFile(inv, 0, true)) {
			if (! file.isEmpty()) {
				ObjectInputStream ois = new ObjectInputStream(file.getInputStream());
				rte = (WandererContent) ois.readObject();
			}
		}
		rte.setInventory(inv);
		return rte;
	}

	public static boolean isWanderer(Inventory inv) {
		return isWanderer(inv, (Scope) null, null, null);
	}

	public static boolean isWanderer(Inventory inv, Scope scope) {
		return isWanderer(inv, scope, null, null);
	}

	public static boolean isWanderer(Inventory inv, Level level, String nameprefix) {
		return isWanderer(inv, level.scope, level.type, nameprefix);
	}

	public static boolean isWanderer(Inventory inv, String type, Level level) {
		return isWanderer(inv, level.scope, type, null);
	}

	private static boolean isWanderer(Inventory inv, Scope scope, String type, String titleprefix) {
		ItemStack stack = inv.getItem(0);
		if (stack != null && stack.getType().equals(Material.WRITTEN_BOOK) && stack.hasItemMeta()) {
			BookMeta book = (BookMeta) stack.getItemMeta();
			String booktitle = book.getTitle();
			String dot = "\\.";
			StringBuilder match = new StringBuilder();

			match.append("^").append(titleprefix).append(dot);

			if (scope != null)
				match.append(scope.name).append(dot);
			else
				match.append("[a-zA-Z]{1,}").append(dot);

			if (type != null)
				match.append(type).append(dot);

			match.append(".*");
			if (BookFile.isBookFile(inv, 0) 
					&& booktitle.matches(match.toString())) {
				if (! mustRemove)
					return true;
				deleteContent(inv);
			}
		}
		return false;
	}

	public static void createWanderer(Inventory inv, int region, Level level, Player player
			, String name, String type) throws IOException {
		try (BCFile file = new BookFile(inv, 0, true, name)) {
			String dot = ".";
			StringBuilder match = new StringBuilder();
			match.append(name).append(dot).append(level.scope.name);
			match.append(dot).append(type).append(dot);
			file.setDescription(match.toString());
			file.flush();
		}
		mustRemove = false;
	}

	public static <T extends WandererContent> void saveContent(T rte)
			throws IOException, ClassNotFoundException {
		Inventory inv = rte.getInventory();

		try (BCFile file = new BookFile(inv, 0, true)) {
			file.clear();
			ObjectOutputStream oos = new ObjectOutputStream(file.getOutputStream());
			oos.writeObject(rte);
			oos.flush();
		}
	}

	public static void deleteContent(Inventory inv) {
		inv.clear(0);
	}
}
