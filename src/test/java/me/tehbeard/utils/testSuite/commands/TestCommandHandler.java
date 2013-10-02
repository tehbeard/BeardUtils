package me.tehbeard.utils.testSuite.commands;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


import me.tehbeard.utils.commands.CommandHandler;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class TestCommandHandler {

    CommandHandler handler;
    Server server;

    @Before
    public void setup() {
        this.server = mock(Server.class);
        when(server.getPluginCommand(anyString())).thenReturn(null);
        this.handler = new CommandHandler(server);

    }

    @Test
    public void testAddCommand() {
        this.handler.addCommand(ACommand.class);

    }

    @Test
    public void testGetInfo() {
        this.handler.addCommand(ACommand.class);
        assertNotNull(this.handler.getInfo("command"));
    }

    @Test
    public void testArgPackPassing() {
        this.handler.addCommand(BCommand.class);
        // CommandInfo ci = handler.getInfo("create");
        Player player = mock(Player.class);

        this.handler.executeCommand(player, "create steveAB -type creeper -a -c \"foo bar\"");

        //
    }
    
    @Test
    public void testViaOnCommand() {
        this.handler.addCommand(BCommand.class);

        Player player = mock(Player.class);
        
        

        this.handler.executeCommand(player, "create steveAB -type creeper -a -c \"foo bar\"");
        verify(player).sendMessage("SUCCESS");
    }

}