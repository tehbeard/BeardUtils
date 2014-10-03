package com.tehbeard.utils.uuid;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.gson.stream.JsonReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * API to access Mojang's various web endpoints to get information
 * @author James
 *
 */
public class MojangWebAPI {
    
    public class ProfileEntry{
        public String error;
        public String id;
        public String name;
        
        public UUID getUUID(){
            return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));
        }
    }

    public static final String UUID_NAME_LOOKUP_ENDPOINT = "https://api.mojang.com/profiles/minecraft";
    public static final String NAME_DATA_LOOKUP_ENDPOINT = "https://sessionserver.mojang.com/session/minecraft/profile/";
    public static final String HAS_PAID_ENDPOINT         = "https://minecraft.net/haspaid.jsp?user=";
    public static final String NAME_PROFILE_AT_ENDPOINT  = "https://api.mojang.com/users/profiles/minecraft/";
    public static final String UUID_HISTORY_ENDPOINT     = "https://api.mojang.com/user/profiles/<uuid>/names";
    
    public static final int MAX_QUERIES_PER_REQUEST = 100;
    /**
     * Generates a mapping of name=>UUIDs,
     * WARNING: THIS WILL BLOCK THE THREAD DUE TO WEB CALLS
     * This method can handle >100 names, splitting up requests and piecing them back together again
     * @param names list of names to process
     * @return Map of name to it's UUID
     * @throws Exception
     */
    public static Map<String,UUID> lookupUUIDS(List<String> names) throws Exception{
        
        
        
        Map<String,UUID> map = new HashMap<String, UUID>();
        int offset = 0;
        while(offset < names.size()){
            map.putAll(lookupUUIDLimited(names.subList(offset, Math.min(names.size(), offset+MAX_QUERIES_PER_REQUEST))));
            offset += MAX_QUERIES_PER_REQUEST;
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String,UUID> lookupUUIDLimited(List<String> names) throws Exception{
        if(names.size() > MAX_QUERIES_PER_REQUEST){throw new IllegalArgumentException("Can only process " + MAX_QUERIES_PER_REQUEST + " names at a time!");}
        Map<String,UUID> uuidMap = new HashMap<String, UUID>();
        URL url = new URL(UUID_NAME_LOOKUP_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        
        OutputStream body = connection.getOutputStream();
        
        List<String> array = new ArrayList<String>();
        array.addAll(names);
        
        body.write(new Gson().toJson(array).getBytes());
        body.flush();
        body.close();
        
        //new InputStreamReader(connection.getInputStream())
        List<ProfileEntry> profiles = new Gson().fromJson(new JsonReader(new InputStreamReader(connection.getInputStream())), new TypeToken<List<ProfileEntry>>(){}.getType());
        for(ProfileEntry o : profiles){
            uuidMap.put(o.name, o.getUUID());
        }
        return uuidMap; 
    }
    
    /**
     * Checks if a username is a paid (premium) account
     * @param name
     * @return
     * @throws Exception
     */
    public static boolean hasPaid(String name) throws Exception{
        URL url = new URL(HAS_PAID_ENDPOINT + name);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.connect();
        connection.getResponseCode();
        Scanner scan = new Scanner(connection.getInputStream());
        String s = scan.next();
        scan.close();
        return Boolean.parseBoolean(s);
    }
    
    /**
     * Looks up the UUID of the account that owned this name at the provided time
     * @param name name to look up
     * @param timestamp unix timestamp of when to look at, use -1 to indicate NOW
     * @return UUID of the account who owned the name then, or null if no player with that name found.
     * @throws Exception
     */
    public static UUID lookupUuidByNameAt(String name,long timestamp) throws Exception{
        URL url = new URL(NAME_PROFILE_AT_ENDPOINT + name + (timestamp == -1 ? "": "?at="+timestamp));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.connect();
        connection.getResponseCode();
        
        ProfileEntry profile = new Gson().fromJson(new JsonReader(new InputStreamReader(connection.getInputStream())), ProfileEntry.class);
        if(profile.error.length() > 0){
        	return null;
        }
        return profile.getUUID();
    }
    
    /**
     * Returns a list of names that a UUID has been associated with.
     * @param uuid UUID of the player to look up
     * @return list of names inc. current one.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public static List<String> getProfileAliases(UUID uuid) throws Exception{
    	List<String> aliases = new ArrayList<String>();
    	
    	URL url = new URL(UUID_HISTORY_ENDPOINT.replace("<uuid>", uuid.toString().replaceAll("-", "")));
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.connect();
        connection.getResponseCode();
        
        return new Gson().fromJson(new JsonReader(new InputStreamReader(connection.getInputStream())), new TypeToken<List<String>>(){}.getType());
    }
    
    /**
     * Convenience method of getProfileAliases and lookupUUIDByNameAt 
     * @param name
     * @param timestamp
     * @return
     */
    public static List<String> getKnownAliases(String name,long timestamp) throws Exception{
    	return getProfileAliases(lookupUuidByNameAt(name, timestamp));
    }
    
    /**
     * Convenience method of getProfileAliases and lookupUUIDByNameAt
     * @param name
     * @return
     * @throws Exception
     */
    public static List<String> getKnownAliases(String name) throws Exception{
    	return getProfileAliases(lookupUuidByNameAt(name,-1));
    }
    
    public static UUID expandUUID(String uuid){
        return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" +uuid.substring(20, 32));
    }
    
    public static String compactUUID(UUID uuid){
        return uuid.toString().replaceAll("-", "");
    }
}
