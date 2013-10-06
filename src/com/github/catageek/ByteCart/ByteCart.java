package com.github.catageek.ByteCart;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderManager;
import com.github.catageek.ByteCart.EventManagement.ByteCartListener;
import com.github.catageek.ByteCart.EventManagement.ConstantSpeedListener;
import com.github.catageek.ByteCart.EventManagement.PreloadChunkListener;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;
import com.github.catageek.ByteCart.Storage.IsTrainManager;
import com.github.catageek.ByteCart.Wanderer.BCWandererManager;
import com.github.catageek.ByteCart.plugins.BCDynmapPlugin;
import com.github.catageek.ByteCartAPI.ByteCartAPI;
import com.github.catageek.ByteCartAPI.ByteCartPlugin;
import com.github.catageek.ByteCartAPI.AddressLayer.Resolver;

/**
 * Main class
 */
public final class ByteCart extends JavaPlugin implements ByteCartPlugin {

	public static Logger log = Logger.getLogger("Minecraft");
	public static ByteCart myPlugin;
	public static boolean debug;
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


		// register updater factory
		this.getWandererManager().register(new UpdaterFactory(), "Updater");
		
		getCommand("mego").setExecutor(new BytecartCommandExecutor());
		getCommand("sendto").setExecutor(new BytecartCommandExecutor());
		getCommand("bcreload").setExecutor(new BytecartCommandExecutor());
		getCommand("bcupdater").setExecutor(new BytecartCommandExecutor());
		getCommand("bcticket").setExecutor(new BytecartCommandExecutor());
		getCommand("bcback").setExecutor(new BytecartCommandExecutor());
		getCommand("bcdmapsync").setExecutor(new BytecartCommandExecutor());

		if (Bukkit.getPluginManager().isPluginEnabled("dynmap")) {
			log.info("[ByteCart] loading dynmap plugin.");
			getServer().getPluginManager().registerEvents(new BCDynmapPlugin(), this);
		}

		if (this.getConfig().getBoolean("metrics", true)) {
			try {
				MetricsLite metrics = new MetricsLite(this);
				metrics.start();
				log.info("[ByteCart] Submitting stats to MCStats.");
			} catch (IOException e) {
				// Failed to submit the stats :-(
			}
		}


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
	protected final void loadConfig() {
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
	public Resolver getResolver() {
		return resolver;
	}

	/**
	 * Set the resolver that will be used
	 * 
	 * @param resolver the resolver provided
	 */
	public void setResolver(Resolver resolver) {
		this.resolver = resolver;
	}
	
	public final Logger getLog() {
		return log;
	}

	/**
	 * @return the wf
	 */
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
