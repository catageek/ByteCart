package com.github.catageek.ByteCart;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderManager;
import com.github.catageek.ByteCart.EventManagement.ByteCartListener;
import com.github.catageek.ByteCart.EventManagement.PreloadChunkListener;
import com.github.catageek.ByteCart.Storage.IsTrainManager;
import com.github.catageek.ByteCart.plugins.BCDynmapPlugin;

public final class ByteCart extends JavaPlugin {

	public static Logger log = Logger.getLogger("Minecraft");
	public static ByteCart myPlugin;
	public static boolean debug;
	private PreloadChunkListener preloadchunklistener;
	private CollisionAvoiderManager cam;
	private IsTrainManager it;
	public int Lockduration;

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
		
		if (Bukkit.getPluginManager().isPluginEnabled("dynmap")) {
			log.info("[ByteCart] loading dynmap plugin.");
			getServer().getPluginManager().registerEvents(new BCDynmapPlugin(), this);
		}

		log.info("[ByteCart] plugin has been enabled.");
	}

	public void onDisable(){ 
		log.info("Your plugin has been disabled.");

		myPlugin = null;
		log = null;

	}

	protected final void loadConfig() {
		debug = this.getConfig().getBoolean("debug", false);

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
}
