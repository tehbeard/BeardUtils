package me.tehbeard.utils.testSuite.update;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import com.avaje.ebean.EbeanServer;

public class FakePlugin implements Plugin {

    /**
     * @param desc
     */
    public FakePlugin(PluginDescriptionFile desc) {
        super();
        this.desc = desc;
    }

    private PluginDescriptionFile desc;

    public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
            String[] arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    public FileConfiguration getConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    public File getDataFolder() {
        // TODO Auto-generated method stub
        return null;
    }

    public EbeanServer getDatabase() {
        // TODO Auto-generated method stub
        return null;
    }

    public ChunkGenerator getDefaultWorldGenerator(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    public PluginDescriptionFile getDescription() {
        
        return desc;
    }

    public Logger getLogger() {
        // TODO Auto-generated method stub
        return Logger.getLogger("Minecraft");
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public PluginLoader getPluginLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    public InputStream getResource(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Server getServer() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isNaggable() {
        // TODO Auto-generated method stub
        return false;
    }

    public void onDisable() {
        // TODO Auto-generated method stub
        
    }

    public void onEnable() {
        // TODO Auto-generated method stub
        
    }

    public void onLoad() {
        // TODO Auto-generated method stub
        
    }

    public void reloadConfig() {
        // TODO Auto-generated method stub
        
    }

    public void saveConfig() {
        // TODO Auto-generated method stub
        
    }

    public void saveDefaultConfig() {
        // TODO Auto-generated method stub
        
    }

    public void saveResource(String arg0, boolean arg1) {
        // TODO Auto-generated method stub
        
    }

    public void setNaggable(boolean arg0) {
        // TODO Auto-generated method stub
        
    }

}
