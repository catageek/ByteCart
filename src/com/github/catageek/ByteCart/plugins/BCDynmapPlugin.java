package com.github.catageek.ByteCart.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.Event.SignCreateEvent;
import com.github.catageek.ByteCart.Event.SignRemoveEvent;
import com.github.catageek.ByteCart.Event.UpdaterClearStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterPassStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterSetStationEvent;
import com.github.catageek.ByteCart.HAL.IC;
import com.github.catageek.ByteCart.Routing.Address;
import com.github.catageek.ByteCart.Routing.AddressFactory;
import com.github.catageek.ByteCart.Signs.BCSign;

public final class BCDynmapPlugin implements Listener {

	private static final String name = ByteCart.myPlugin.getConfig().getString("dynmap.layer", "ByteCart");
	private static MarkerSet markerset;
	private static MarkerIcon defaulticon;
	private static MarkerIcon erroricon;

	public BCDynmapPlugin() {
		DynmapCommonAPI api = (DynmapCommonAPI) Bukkit.getPluginManager().getPlugin("dynmap");
		MarkerAPI markerapi = api.getMarkerAPI();

		if (markerapi == null)
			throw new MissingResourceException("Dynmap API could not be loaded", MarkerAPI.class.getName(), "dynmap");

		defaulticon = loadIcon(markerapi,"bytecart_station", "station", "resources/bc2.png");
		erroricon = loadIcon(markerapi,"bytecart_error", "error", "resources/error.png");

		markerset = markerapi.getMarkerSet(name);
		if (markerset == null) {
			markerset = markerapi.createMarkerSet(name, name, null, true);
			markerset.addAllowedMarkerIcon(defaulticon);
			markerset.addAllowedMarkerIcon(erroricon);
			markerset.setDefaultMarkerIcon(defaulticon);
		}

	}

	private MarkerIcon loadIcon(MarkerAPI markerapi, String id, String label, String file) {
		MarkerIcon icon = markerapi.getMarkerIcon(id);
		if (icon == null) {
			try (InputStream png = getClass().getResourceAsStream(file)) {
				icon = markerapi.createMarkerIcon(id, label, png);
			} catch (IOException e) {
				throw new MissingResourceException("ByteCart : icon could not be loaded", this.getClass().getName(), file);
			}
		}
		return icon;
	}

	@EventHandler(ignoreCancelled = true)
	public void onUpdaterSetStation(UpdaterSetStationEvent event) {
		Block block = event.getIc().getBlock();
		Marker marker = markerset.findMarker(buildId(block));
		if (marker != null)
			marker.deleteMarker();
		Address address = event.getNewAddress();
		selectAndAddMarker(event.getIc().getFriendlyName(), block, address);
	}

	@EventHandler(ignoreCancelled = true)
	public void onUpdaterPassStation(UpdaterPassStationEvent event) {
		Block block = event.getIc().getBlock();
		Address address = event.getAddress();
		Marker marker = markerset.findMarker(buildId(block));
		if (marker != null) {
			if (address.isValid() && checkMarkerInformation(marker, block, address.toString(), event.getIc().getFriendlyName()))
				return;
			marker.deleteMarker();
		}
		selectAndAddMarker(event.getIc().getFriendlyName(), block, address);
	}


	@EventHandler(ignoreCancelled = true)
	public void onUpdaterClearStation(UpdaterClearStationEvent event) {
		Block block = event.getIc().getBlock();
		Marker marker = markerset.findMarker(buildId(block));
		if (marker != null)
			marker.deleteMarker();
	}

	@EventHandler(ignoreCancelled = true)
	public void onSignRemove(SignRemoveEvent event) {
		IC ic;
		if (! ((ic = event.getIc()) instanceof BCSign))
			return;
		Block block = ic.getBlock();
		Marker marker = markerset.findMarker(buildId(block));
		if (marker != null)
			marker.deleteMarker();
	}

	@EventHandler(ignoreCancelled = true)
	public void onSignCreate(SignCreateEvent event) {
		IC ic;
		if (! ((ic = event.getIc()).getName().equals("BC9001")))
			return;
		Block block = ic.getBlock();
		String address = event.getStrings()[3];
		Marker marker = markerset.findMarker(buildId(block));
		if (marker != null)
			marker.deleteMarker();
		selectAndAddMarker(event.getStrings()[2], block, AddressFactory.getAddress(address));
	}

	private static void selectAndAddMarker(String friendlyname, Block block, Address address) {
		MarkerIcon icon;
		String label;
		if (address.isValid()) {
			icon = defaulticon;
			label = buildLabel(address.toString(), friendlyname);
		}
		else {
			icon = erroricon;
			label = "Error setting address for " + friendlyname;
		}
		addMarker(label, block, icon);
	}

	private static void addMarker(String label, Block block, MarkerIcon markericon) {
		markerset.createMarker(buildId(block), label,
				block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), markericon, true);
	}

	private static String buildLabel(String address, String friendlyname) {
		return friendlyname + " (" + address + ")";
	}


	private static String buildId(Block block) {
		return block.getLocation().toString();
	}

	private static boolean checkMarkerInformation(Marker marker, Block block, String address, String friendlyName) {
		boolean ret = false;
		ret &= (block.getX() == marker.getX());
		ret &= (block.getY() == marker.getY());
		ret &= (block.getZ() == marker.getZ());
		ret &= (buildLabel(address, friendlyName).equals(marker.getLabel()));
		return ret;
	}
}
