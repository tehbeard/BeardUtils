package me.tehbeard.utils.testSuite.update;

import static org.junit.Assert.*;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Before;
import org.junit.Test;

import com.tehbeard.update.*;


public class Test_Update {

    UpdateReader readerOld;
    UpdateReader readerNew;
    UpdateReader readerSame;
    @Before
    public void setUp(){
        
        readerOld = new UpdateReader(new FakePlugin(new PluginDescriptionFile("BeardStat", "0.4.7", "tehbeard.BeardStat")), new SemVerChecker());
        readerSame = new UpdateReader(new FakePlugin(new PluginDescriptionFile("BeardStat", "0.4.8", "tehbeard.BeardStat")), new SemVerChecker());
        readerNew = new UpdateReader(new FakePlugin(new PluginDescriptionFile("BeardStat", "0.5.0", "tehbeard.BeardStat")), new SemVerChecker());
        
    }
    
    @Test
    public void Test_old(){
        assertTrue("is older",readerOld.checkUpdate());
    }
    
    @Test
    public void Test_same(){
        assertFalse("is same",readerSame.checkUpdate());
    }
    
    @Test
    public void Test_new(){
        assertFalse("is newer",readerNew.checkUpdate());
    }
    
    
    
}
