package com.github.catageek.ByteCart;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class ByteCart extends JavaPlugin {
	
	public static Logger log = Logger.getLogger("Minecraft");

	public static ByteCart myPlugin;

	public static boolean debug;
	
	private DelayedThreadManager dtm;
	
	private CollisionAvoiderManager cam;
	

    public void onEnable(){
    	log.info("ByteCart plugin has been enabled.");
    	
    	this.saveDefaultConfig();
    	
    	myPlugin = this;
    	
    	debug = this.getConfig().getBoolean("debug");
//    	this.getConfig().set("debug", debug);
//    	this.saveConfig();
    	
    	if(debug){
    		log.info("ByteCart : debug mode is on.");
    	}
    	
    	this.setDtm(new DelayedThreadManager());
    	this.setCam(new CollisionAvoiderManager());
    	
    	getServer().getPluginManager().registerEvents(new ByteCartListener(), this);
    }
     
    public void onDisable(){ 
    	log.info("Your plugin has been disabled.");
    	
     	myPlugin = null;
     	log = null;
     	
    }
   

	/**
	 * @return the dtm
	 */
	public DelayedThreadManager getDelayedThreadManager() {
		return dtm;
	}

	/**
	 * @param dtm the dtm to set
	 */
	private void setDtm(DelayedThreadManager dtm) {
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
    
   
   }
