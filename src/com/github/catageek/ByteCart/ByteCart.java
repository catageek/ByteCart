package com.github.catageek.ByteCart;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ByteCart extends JavaPlugin {
	
	final public static Logger log = Logger.getLogger("Minecraft");
	public static Plugin myPlugin;

	public static boolean debug;
	

    public void onEnable(){
    	log.info("ByteCart plugin has been enabled.");
    	
    	myPlugin = this;
    	
    	debug = this.getConfig().getBoolean("debug");
    	this.getConfig().set("debug", debug);
    	this.saveConfig();
    	
    	if(debug){
    		log.info("ByteCart : debug mode is on.");
    	}
    	
    	getServer().getPluginManager().registerEvents(new ByteCartListener(), this);
    }
     
    public void onDisable(){ 
    	log.info("Your plugin has been disabled.");
    }
    
   
   }
