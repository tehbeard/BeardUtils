package com.tehbeard.utils.testSuite.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.tehbeard.utils.session.SessionStore;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class TestSession {

    @Test
    public void testSessionManual() {
        SessionStore<Integer> session = new SessionStore<Integer>();

        assertFalse("no entry for alice", session.hasSession("alice"));

        session.putSession("alice", 5);

        assertTrue("Entry for alice", session.hasSession("alice"));

        assertEquals("session stored", session.getSession("alice"), (Integer) 5);

        session.clearSession("alice");
        assertFalse("entry for alice cleared", session.hasSession("alice"));
    }

    @Test
    public void testSessionEvent() {
        SessionStore<Integer> session = new SessionStore<Integer>();

        assertFalse("no entry for alice", session.hasSession("alice"));

        session.putSession("alice", 5);

        assertTrue("Entry for alice", session.hasSession("alice"));

        assertEquals("session stored", session.getSession("alice"), (Integer) 5);

        Player player = mock(Player.class);
        when(player.getName()).thenReturn("alice");
        session.logout(new PlayerQuitEvent(player, "HAXX"));
        assertFalse("entry for alice cleared", session.hasSession("alice"));
    }
}
