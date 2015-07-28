package com.tehbeard.utils.session;

import java.util.HashMap;
import java.util.Map;


/**
 * Generic class for storing sessions You can activate killing a session on
 * logout by registering the instance of this class with event bus
 * 
 * @author James
 * 
 * @param <T>
 */
public class SessionStore<T> {

    private final Map<String, T> sessions;

    public SessionStore() {
        this.sessions = new HashMap<String, T>();
    }

    /**
     * Put a value into the session store
     * 
     * @param player
     * @param session
     */
    public void putSession(String player, T session) {
        this.sessions.put(player, session);
    }

    /**
     * returns if the player has a session stored
     * 
     * @param player
     * @return
     */
    public boolean hasSession(String player) {
        return this.sessions.containsKey(player);
    }

    /**
     * returns a session
     * 
     * @param player
     * @return
     */
    public T getSession(String player) {
        return this.sessions.get(player);
    }

    public void clearSession(String player) {
        this.sessions.remove(player);
    }

// TODO : Add Logout handler for easy session clearing 
//    public void logout(Object e) {
//        clearSession(e.getPlayer().getName());
//    }
    
    public Map<String, T> getMap(){
        return sessions;
    }

}
