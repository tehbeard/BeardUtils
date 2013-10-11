/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tehbeard.utils.bungee;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 *
 * @author James
 */
public class BungeeCordChannelInterface implements PluginMessageListener {

    private Plugin plugin;
    private Map<String, PluginMessageListener> listeners = new HashMap<String, PluginMessageListener>();
    
    private Queue<MessageCallback<IPQuery>> ipQueuries = new LinkedBlockingQueue<MessageCallback<IPQuery>>();
    

    public BungeeCordChannelInterface(Plugin plugin) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        this.plugin = plugin;
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void getIP(Player player,MessageCallback<IPQuery> callback){
        try {
            ByteBackedDataOutputStream bbd = ByteBackedDataOutputStream.getDos();
            bbd.writeUTF("IP");
            player.sendPluginMessage(plugin, "BungeeCord", bbd.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(BungeeCordChannelInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        ipQueuries.add(callback);
    }

    public void connect(Player player, String serverName) {
        try {
            ByteBackedDataOutputStream bbd = ByteBackedDataOutputStream.getDos();
            bbd.writeUTF("Connect");
            bbd.writeUTF(serverName);
            player.sendPluginMessage(plugin, "BungeeCord", bbd.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(BungeeCordChannelInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connectOther(String player, String serverName) {
        try {
            ByteBackedDataOutputStream bbd = ByteBackedDataOutputStream.getDos();
            bbd.writeUTF("ConnectOther");
            bbd.writeUTF(player);
            bbd.writeUTF(serverName);
            if (Bukkit.getOnlinePlayers().length > 0) {
                Bukkit.getOnlinePlayers()[0].sendPluginMessage(plugin, "BungeeCord", bbd.toByteArray());
            }
        } catch (IOException ex) {
            Logger.getLogger(BungeeCordChannelInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void message(String player, String message) {
        try {
            ByteBackedDataOutputStream bbd = ByteBackedDataOutputStream.getDos();
            bbd.writeUTF("Message");
            bbd.writeUTF(player);
            bbd.writeUTF(message);
            if (Bukkit.getOnlinePlayers().length > 0) {
                Bukkit.getOnlinePlayers()[0].sendPluginMessage(plugin, "BungeeCord", bbd.toByteArray());
            }
        } catch (IOException ex) {
            Logger.getLogger(BungeeCordChannelInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void forward(String server, String subchannel, byte[] data) {
        try {
            ByteBackedDataOutputStream out = ByteBackedDataOutputStream.getDos();
            out.writeUTF("Forward");
            out.writeUTF(server);
            out.writeUTF(subchannel);

            out.writeShort(data.length);
            out.write(data);
            if (Bukkit.getOnlinePlayers().length > 0) {
                Bukkit.getOnlinePlayers()[0].sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }
        } catch (IOException ex) {
            Logger.getLogger(BungeeCordChannelInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class IPQuery {
        public final String host;
        public final int port;
        
        public IPQuery(String host,int port){
            this.host = host;
            this.port = port;
        }
    }
    
    
    public interface MessageCallback<T>{
        public void callback(T data);
    }
}
