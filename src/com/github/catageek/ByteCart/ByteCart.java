package com.github.catageek.ByteCart;

import java.util.MissingResourceException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderManager;
import com.github.catageek.ByteCart.EventManagement.ByteCartListener;
import com.github.catageek.ByteCart.EventManagement.ConstantSpeedListener;
import com.github.catageek.ByteCart.EventManagement.PreloadChunkListener;
import com.github.catageek.ByteCart.Storage.IsTrainManager;
import com.github.catageek.ByteCart.Wanderer.BCWandererManager;
import com.github.catageek.ByteCart.plugins.BCDynmapPlugin;
import com.github.catageek.ByteCart.plugins.BCHostnameResolutionPlugin;
import com.github.catageek.ByteCart.plugins.BCWandererTracker;
import com.github.catageek.ByteCartAPI.ByteCartAPI;
import com.github.catageek.ByteCartAPI.ByteCartPlugin;
import com.github.catageek.ByteCartAPI.AddressLayer.Resolver;

import com.github.catageek.ByteCart.Commands.CommandBCBack;
import com.github.catageek.ByteCart.Commands.CommandBCDMapSync;
import com.github.catageek.ByteCart.Commands.CommandBCReload;
import com.github.catageek.ByteCart.Commands.CommandBCTicket;
import com.github.catageek.ByteCart.Commands.CommandBCUpdater;
import com.github.catageek.ByteCart.Commands.CommandMeGo;
import com.github.catageek.ByteCart.Commands.CommandSendTo;

/**
 * Main class
 */
public final class ByteCart extends JavaPlugin implements ByteCartPlugin {

	public static Logger log = Logger.getLogger("Minecraft");
	public static ByteCart myPlugin;
	public static boolean debug;
	private BCHostnameResolutionPlugin hostnamePlugin;
	private PreloadChunkListener preloadchunklistener;
	private ConstantSpeedListener constantspeedlistener;
	private CollisionAvoiderManager cam;
	private BCWandererManager wf;
	private IsTrainManager it;
	public int Lockduration;
	private boolean keepitems;
	private Resolver resolver;

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable(){

		myPlugin = this;

		ByteCartAPI.setPlugin(this);

		this.saveDefaultConfig();

		this.loadConfig();

		this.setCam(new CollisionAvoiderManager());
		this.setWf(new BCWandererManager());
		this.setIt(new IsTrainManager());

		getServer().getPluginManager().registerEvents(new ByteCartListener(), this);

		getCommand("mego").setExecutor(new CommandMeGo(this));
		getCommand("sendto").setExecutor(new CommandSendTo(this));
		getCommand("bcreload").setExecutor(new CommandBCReload());
		getCommand("bcupdater").setExecutor(new CommandBCUpdater());
		getCommand("bcticket").setExecutor(new CommandBCTicket(this));
		getCommand("bcback").setExecutor(new CommandBCBack());
		getCommand("bcdmapsync").setExecutor(new CommandBCDMapSync());

		if (Bukkit.getPluginManager().isPluginEnabled("dynmap")  && this.getConfig().getBoolean("dynmap", true)) {
			log.info("[ByteCart] loading dynmap plugin.");
			try {
				getServer().getPluginManager().registerEvents(new BCDynmapPlugin(), this);
			}
			catch (MissingResourceException e) {
				log.info("[ByteCart] Failed to load dynmap plugin.");
			}
		}

		if (this.getConfig().getBoolean("hostname_resolution", true)) {
			log.info("[ByteCart] loading BCHostname plugin.");
			hostnamePlugin = new BCHostnameResolutionPlugin();
			try {
				hostnamePlugin.onLoad();
				ByteCartAPI.setResolver(hostnamePlugin);
				getServer().getPluginManager().registerEvents(hostnamePlugin, this);
				getCommand("host").setExecutor(hostnamePlugin);
			}
			catch (MissingResourceException e) {
				log.info("[ByteCart] Failed to load BCHostname plugin.");
			}
		}

		BCWandererTracker updatertrackerplugin = new BCWandererTracker();
		getServer().getPluginManager().registerEvents(updatertrackerplugin, this);
		getCommand("bctracker").setExecutor(updatertrackerplugin);

		/* Uncomment to launch storage test
		Block block = this.getServer().getWorld("plat").getBlockAt(0, 61, 0);
		Inventory inventory = ((Chest)block.getState()).getInventory();
		(new FileStorageTest(inventory)).runTest();
		*/

		log.info("[ByteCart] plugin has been enabled.");
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable(){ 
		log.info("Your plugin has been disabled.");

		myPlugin = null;
		ByteCartAPI.setPlugin(null);
		log = null;

	}

	/**
	 * Load the configuration file
	 *
	 */
	public final void loadConfig() {
		debug = this.getConfig().getBoolean("debug", false);
		keepitems = this.getConfig().getBoolean("keepitems", true);

		Lockduration = this.getConfig().getInt("Lockduration", 44);

		if(debug){
			log.info("ByteCart : debug mode is on.");
		}

		if(this.getConfig().getBoolean("loadchunks")) {
			if (preloadchunklistener == null) {
				preloadchunklistener = new PreloadChunkListener();
				getServer().getPluginManager().registerEvents(preloadchunklistener, this);
			}
		}
		else
			if (preloadchunklistener != null) {
				HandlerList.unregisterAll(preloadchunklistener);
				preloadchunklistener = null;
			}

		if(this.getConfig().getBoolean("constantspeed", false)) {
			if (constantspeedlistener == null) {
				constantspeedlistener = new ConstantSpeedListener();
				getServer().getPluginManager().registerEvents(constantspeedlistener, this);
			}
		}
		else
			if (constantspeedlistener != null) {
				HandlerList.unregisterAll(constantspeedlistener);
				constantspeedlistener = null;
			}
	}


	/**
	 * @return the cam
	 */
	public CollisionAvoiderManager getCollisionAvoiderManager() {
		return cam;
	}

	/**
	 * @param cam the cam to set
	 */
	private void setCam(CollisionAvoiderManager cam) {
		this.cam = cam;
	}

	/**
	 * @return the it
	 */
	public IsTrainManager getIsTrainManager() {
		return it;
	}

	/**
	 * @param it the it to set
	 */
	private void setIt(IsTrainManager it) {
		this.it = it;
	}

	/**
	 * @return true if we must keep items while removing carts
	 */
	public boolean keepItems() {
		return keepitems;
	}

	/**
	 * @return the resolver registered
	 */
	@Override
	public Resolver getResolver() {
		return resolver;
	}

	/**
	 * Set the resolver that will be used
	 * 
	 * @param resolver the resolver provided
	 */
	@Override
	public void setResolver(Resolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public final Logger getLog() {
		return log;
	}

	/**
	 * @return the wf
	 */
	@Override
	public BCWandererManager getWandererManager() {
		return wf;
	}

	/**
	 * @param wf the wf to set
	 */
	private void setWf(BCWandererManager wf) {
		this.wf = wf;
	}
}
