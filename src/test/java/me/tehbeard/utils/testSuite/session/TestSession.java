//package me.tehbeard.utils.testSuite.session;
//
//import static org.junit.Assert.*;
//import me.tehbeard.utils.session.SessionStore;
//import me.tehbeard.utils.testSuite.fake.FakePlayer;
//
//import org.bukkit.event.player.PlayerQuitEvent;
//import org.junit.Test;
//
//public class TestSession {
//
//    
//    @Test
//    public void testSessionManual(){
//        SessionStore<Integer> session = new SessionStore<Integer>();
//        
//        
//        assertFalse("no entry for alice",session.hasSession("alice"));
//        
//        session.putSession("alice", 5);
//        
//        assertTrue("Entry for alice",session.hasSession("alice"));
//        
//        
//        
//        assertEquals("session stored", session.getSession("alice"), (Integer)5);
//        
//        
//        session.clearSession("alice");
//        assertFalse("entry for alice cleared",session.hasSession("alice"));
//    }
//    
//    @Test
//    public void testSessionEvent(){
//        SessionStore<Integer> session = new SessionStore<Integer>();
//        
//        
//        assertFalse("no entry for alice",session.hasSession("alice"));
//        
//        session.putSession("alice", 5);
//        
//        assertTrue("Entry for alice",session.hasSession("alice"));
//        
//        
//        
//        assertEquals("session stored", session.getSession("alice"), (Integer)5);
//        
//        
//        session.logout(new PlayerQuitEvent(new FakePlayer("alice"), "HAXX"));
//        assertFalse("entry for alice cleared",session.hasSession("alice"));
//    }
//}
