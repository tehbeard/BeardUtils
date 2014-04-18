package com.tehbeard.utils.uuid;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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
    
    
    public static void main(String[] args) throws Exception{
        String[] names = new String[]{"Tulonsae","demonnaruto19","WokkA1"};
        for(String name : names){
          System.out.println(name + " :: "  + (MojangWebAPI.hasPaid(name) ? "paid" : "not paid"));
        }
    }
    
}
