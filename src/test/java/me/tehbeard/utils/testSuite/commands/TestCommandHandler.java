package me.tehbeard.utils.testSuite.commands;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import me.tehbeard.utils.commands.ArgumentPack;
import me.tehbeard.utils.commands.CommandHandler;
import me.tehbeard.utils.commands.CommandHandler.CommandInfo;

public class TestCommandHandler {

    CommandHandler handler;
    
    
    @Before
    public void setup(){
        handler = new CommandHandler();
        
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
    public void testExecute(){
        handler.addCommand(ACommand.class);
        assertFalse(handler.getInfo("command").execute(null));
    }
    
    @Test
    public void testArgPackPassing(){
        handler.addCommand(BCommand.class);
        CommandInfo ci = handler.getInfo("create");
        String arguments = "steveAB -type creeper -a -c \"foo bar\"";
        ArgumentPack pack = new ArgumentPack(ci.boolFlags, ci.optFlags, arguments);
        ci.execute(pack);
        
        //
    }
   
}
