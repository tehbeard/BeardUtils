package me.tehbeard.utils.testSuite.commands;


import static org.junit.Assert.*;

import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.tehbeard.utils.commands.CommandHandler;

import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@SuppressWarnings("unused")
public class TestCommandHandler {

    CommandHandler handler;
    
    
    @Before
    public void setup(){
        handler = new CommandHandler(null);
        
    }
    
    
    @Test
    public void testAddCommand(){
        handler.addCommand(ACommand.class);
        
    }
    
    @Test
    public void testGetInfo(){
        handler.addCommand(ACommand.class);
        assertNotNull(handler.getInfo("command"));
    }
    
    
    
    @Test
    public void testArgPackPassing(){
        handler.addCommand(BCommand.class);
        //CommandInfo ci = handler.getInfo("create");
        Player player = mock(Player.class);
        
        
        handler.executeCommand(player, "create steveAB -type creeper -a -c \"foo bar\"");
        
        
        //
    }
   
}