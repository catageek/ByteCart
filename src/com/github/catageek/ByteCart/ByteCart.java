package com.github.catageek.ByteCart;

import java.util.logging.Logger;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderManager;
import com.github.catageek.ByteCart.EventManagement.ByteCartListener;
import com.github.catageek.ByteCart.EventManagement.PreloadChunkListener;
import com.github.catageek.ByteCart.Storage.IsTrainManager;
import com.github.catageek.ByteCart.Storage.UpdaterManager;
import com.github.catageek.ByteCart.Storage.WandererManager;

public final class ByteCart extends JavaPlugin {

	public static Logger log = Logger.getLogger("Minecraft");
	public static ByteCart myPlugin;
	public static boolean debug, usebooks;
	private PreloadChunkListener preloadchunklistener;
	private CollisionAvoiderManager cam;
	private IsTrainManager it;
	private UpdaterManager um;
	private WandererManager wm;
	public int Lockduration;

	public void onEnable(){
		log.info("ByteCart plugin has been enabled.");

		myPlugin = this;

		this.saveDefaultConfig();

		this.loadConfig();

		this.setCam(new CollisionAvoiderManager());
		this.setIt(new IsTrainManager());
		this.setUm(new UpdaterManager());
		this.setWm(new WandererManager());

		getServer().getPluginManager().registerEvents(new ByteCartListener(), this);


		getCommand("mego").setExecutor(new BytecartCommandExecutor());
		getCommand("sendto").setExecutor(new BytecartCommandExecutor());
		getCommand("bcreload").setExecutor(new BytecartCommandExecutor());
		getCommand("bcupdater").setExecutor(new BytecartCommandExecutor());
	}

	public void onDisable(){ 
		log.info("Your plugin has been disabled.");

		myPlugin = null;
		log = null;

	}

	protected final void loadConfig() {
		debug = this.getConfig().getBoolean("debug", false);

		usebooks = this.getConfig().getBoolean("usebooks", true);

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

	/**
	 * @return the um
	 */
	public UpdaterManager getUm() {
		return um;
	}

	/**
	 * @param um the um to set
	 */
	private void setUm(UpdaterManager um) {
		this.um = um;
	}

	public WandererManager getWm() {
		return wm;
	}

	private void setWm(WandererManager wm) {
		this.wm = wm;
	}


}
