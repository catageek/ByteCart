package com.github.catageek.ByteCart;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class ByteCart extends JavaPlugin {
	
	public static Logger log = Logger.getLogger("Minecraft");

	public static ByteCart myPlugin;

	public static boolean debug;
	
	private DelayedThreadManager<Object> dtm;
	
	private CollisionAvoiderManager cam;
	
	private IsTrainManager it;
	
	private UpdaterManager um;
	
	public int Lockduration;
	

    public void onEnable(){
    	log.info("ByteCart plugin has been enabled.");
    	
    	this.saveDefaultConfig();
    	
    	myPlugin = this;
    	
    	debug = this.getConfig().getBoolean("debug");
//    	this.getConfig().set("debug", debug);
//    	this.saveConfig();
    	
    	Lockduration = this.getConfig().getInt("Lockduration", 44);
    	
    	if(debug){
    		log.info("ByteCart : debug mode is on.");
    	}
    	
    	this.setDtm(new DelayedThreadManager<Object>());
    	this.setCam(new CollisionAvoiderManager());
    	this.setIt(new IsTrainManager());
    	this.setUm(new UpdaterManager());
    	
    	getServer().getPluginManager().registerEvents(new ByteCartListener(), this);
    	
    	getCommand("mego").setExecutor(new BytecartCommandExecutor());
       	getCommand("sendto").setExecutor(new BytecartCommandExecutor());
       	getCommand("bcupdater").setExecutor(new BytecartCommandExecutor());
    }
     
    public void onDisable(){ 
    	log.info("Your plugin has been disabled.");
    	
     	myPlugin = null;
     	log = null;
     	
    }
   

	/**
	 * @return the dtm
	 */
	public DelayedThreadManager<Object> getDelayedThreadManager() {
		return dtm;
	}

	/**
	 * @param dtm the dtm to set
	 */
	private void setDtm(DelayedThreadManager<Object> dtm) {
		this.dtm = dtm;
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
	public void setCam(CollisionAvoiderManager cam) {
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
	public void setIt(IsTrainManager it) {
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
	public void setUm(UpdaterManager um) {
		this.um = um;
	}
    
   
   }
