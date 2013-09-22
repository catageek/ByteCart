package com.github.catageek.ByteCart;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderManager;
import com.github.catageek.ByteCart.EventManagement.ByteCartListener;
import com.github.catageek.ByteCart.EventManagement.ConstantSpeedListener;
import com.github.catageek.ByteCart.EventManagement.PreloadChunkListener;
import com.github.catageek.ByteCart.Storage.IsTrainManager;
import com.github.catageek.ByteCart.plugins.BCDynmapPlugin;

/**
 * Main class
 */
public final class ByteCart extends JavaPlugin {

	public static Logger log = Logger.getLogger("Minecraft");
	public static ByteCart myPlugin;
	public static boolean debug;
	private PreloadChunkListener preloadchunklistener;
	private ConstantSpeedListener constantspeedlistener;
	private CollisionAvoiderManager cam;
	private IsTrainManager it;
	public int Lockduration;
	private boolean keepitems;

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable(){

		myPlugin = this;

		this.saveDefaultConfig();

		this.loadConfig();

		this.setCam(new CollisionAvoiderManager());
		this.setIt(new IsTrainManager());

		getServer().getPluginManager().registerEvents(new ByteCartListener(), this);


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

		log.info("[ByteCart] plugin has been enabled.");
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable(){ 
		log.info("Your plugin has been disabled.");

		myPlugin = null;
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
}
