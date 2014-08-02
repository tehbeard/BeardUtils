package com.tehbeard.utils.uuid;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * API to access Mojang's various web endpoints to get information
 * @author James
 *
 */
public class MojangWebAPI {

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
        
        JSONArray array = new JSONArray();
        
        array.addAll(names);
        
        body.write(array.toJSONString().getBytes());
        body.flush();
        body.close();
        
        JSONParser jsonParser =  new JSONParser();
        JSONArray profiles = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
        for(Object o : profiles){
            JSONObject jsonProfile = (JSONObject) o;
            String id = (String) jsonProfile.get("id");
            String name = (String) jsonProfile.get("name");
            UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));
            uuidMap.put(name, uuid);
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
        
        JSONObject profile = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
        if(profile.containsKey("error")){
        	return null;
        }
        String id = (String) profile.get("id");
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));
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
        
        Object res = new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
        if(res instanceof JSONArray == false){
        	return null;
        }
        Iterator<String> it = ((JSONArray)res).iterator();
        while(it.hasNext()){
        	aliases.add((String)it.next());
        }
    	return aliases;
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
    
    public static void main(String[] args) throws Exception{
        String[] names = new String[]{"Tulonsae","demonnaruto19","WokkA1"};
        for(String name : names){
          System.out.println(name + " :: "  + (MojangWebAPI.hasPaid(name) ? "paid" : "not paid"));
        }
        
        List<String> alias = MojangWebAPI.getKnownAliases("tehbeard");
        System.out.println("Aliases of Tehbeard:");
        for(String a : alias){
        	System.out.println(a);
        }
    }
    
}
