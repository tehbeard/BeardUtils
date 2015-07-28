/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tehbeard.utils.sql;

import com.sk89q.intake.Command;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public class TestJDBCClass extends CLIJDBCDataSource{

   
    public TestJDBCClass(String type, String driverClass, Logger logger) throws ClassNotFoundException {
        super(type, driverClass, logger);
    }

    @Override
    protected String getMigrationScriptPath(int toVersion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean generateBackup(String name) {
        return true;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean restoreBackup(String name) {
        return true;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDataSourceVersion() {
        return 3;
    }
    
    public static CLIJDBCDataSource getConsoleInstance(String[] args){
        try{
        Logger logger = Logger.getLogger("CLI");
        logger.setLevel(Level.INFO);
        
        TestJDBCClass db = new TestJDBCClass("sql","com.mysql.jdbc.Driver",logger);
        
        
        db.setTag("PREFIX", "stats");
        db.setConnectionUrl(String.format("jdbc:mysql://%s:%s/%s", "127.0.0.1","3306","test"));
        return db;
        }catch(Exception e){
            e.printStackTrace();
           return null;
            
        }
    }
    
    @Command(aliases = "version",desc = "Get schema version",help="Returns the schema version")
    public void commandDbVersion() throws SQLException{
        logger.log(Level.INFO, "Schema is {0}", getSchemaVersion());
    }
    @Command(aliases = "poke",desc = "Test KeyVal",help="")
    public void pokeKeyVal() throws SQLException{
        getKeyValStore().set("a", "first",true);
        getKeyValStore().set("a", "second",true);
        getKeyValStore().set("a", "third",true);
        getKeyValStore().set("b", "first");
        getKeyValStore().set("b", "second");
        getKeyValStore().set("b", "third");
        getKeyValStore().compact("a");
    }
    
    
    public static void main(String[] args){
        doCLI(getConsoleInstance(args),args);
    }
           
            
    
}
