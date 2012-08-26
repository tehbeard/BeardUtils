package com.tehbeard.update;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class UpdateReader {

	private Logger log;
	private String url = "http://tehbeard.com/updateTest/update.yml";
	private Plugin plugin;
	private VersionChecker checker;
	public UpdateReader(Plugin plugin,VersionChecker checker){
        this.plugin = plugin;
        log = plugin!=null ? plugin.getLogger() : Logger.getLogger("Minecraft");
        this.checker = checker;
    }
	public UpdateReader(String url,Plugin plugin,VersionChecker checker){
	    this(plugin, checker);
		this.url = url;
	}

	public boolean checkUpdate(){
		try {
			//load config
			YamlConfiguration config = YamlConfiguration.loadConfiguration((new URL(url)).openStream());
			log.info("Parsing update file");
			
			String updateSiteName = config.getString("name","[]");
			log.info("Reading updates from " + updateSiteName);
			
			ConfigurationSection updatePage = config.getConfigurationSection("plugin." + plugin.getDescription().getName());
			if(updatePage == null){
				log.info("Update file does not have information on our plugin");
				return false;
			}
			String newVersion = updatePage.getString("version", plugin.getDescription().getVersion());
			int value = checker.checkVersion(plugin.getDescription().getVersion(), newVersion);
			
			switch(value)
			{
			case -2:
			case -1:log.info("Running a future version");break;
			case 0:log.info("Running most up to date version");break;
			case 1:
			case 2:
			    if(updatePage.getBoolean("priority",false)){
			        log.info("ALERT: Update availble that has been labelled a priority update. This means it most likely fixes bugs");
			    }
			    else
			    {
			        log.info("Update found! New version " + updatePage.getString("version") + " Download at " + updatePage.getString("url"));
			    }
			    return true;
			}
			
			
		} catch (MalformedURLException e) {
			log.severe("Update URL specified could not be understood");
		} catch (IOException e) {
			log.severe("An I/O error occured while trying to read the update");
		}
		return false;
	}
	
	
	
	
}
