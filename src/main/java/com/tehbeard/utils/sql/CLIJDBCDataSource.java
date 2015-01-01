/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tehbeard.utils.sql;

import com.sk89q.intake.Command;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.dispatcher.SimpleDispatcher;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.util.auth.AuthorizationException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author James
 */
public abstract class CLIJDBCDataSource extends JDBCDataSource {

    public CLIJDBCDataSource(String type, String driverClass, Logger logger) throws ClassNotFoundException {
        super(type, driverClass, logger);
    }

    public static final void doCLI(CLIJDBCDataSource database, String[] args) {
        System.out.println("Database CLI");
        System.out.println("============");
        System.out.println("Loading database and commands.");

        Dispatcher dispatcher = new SimpleDispatcher();
        ParametricBuilder builder = new ParametricBuilder();
        builder.registerMethodsAsCommands(dispatcher, new CLIJDBCStandardCommands(database,dispatcher));
        builder.registerMethodsAsCommands(dispatcher, database);

        System.out.println("Connecting to database");
        try {
            database.setup();
        } catch (SQLException ex) {
            System.out.println("Failed to load database");
            return;
        }
        System.out.println("Connected to database");
        Scanner stdin = new Scanner(System.in);
        boolean doLoop = true;
        while (doLoop) {
            System.out.print(">");
            String command = stdin.nextLine();
            doLoop = !command.equalsIgnoreCase("quit");
            try {
                CommandMapping cmd = dispatcher.get(command.split(" ")[0]);
                if(cmd == null){
                    System.out.println("Command not found");
                }else{
                dispatcher.call(command, null, new String[0]);
                }
            } catch (CommandException ex) {
                    ex.printStackTrace();
            } catch (AuthorizationException ex) {
                System.out.println("Command authentication error");
            }
        }
        stdin.close();
        
    }

    public static class CLIJDBCStandardCommands {
        private final Dispatcher dispatcher;
        private final JDBCDataSource database;
        
        public void line(String line){
            System.out.println(line);
        }

        public CLIJDBCStandardCommands(JDBCDataSource database, Dispatcher dispatcher){
            this.database = database;
            this.dispatcher = dispatcher;
        }
        
        @Command(aliases = "quit", desc="Close database and exit")
        public void commandQuit(){
            try {
            database.teardown();
        } catch (SQLException ex) {
            line("WARNING: Database teardown did not execute successfully, data corruption possible.");
            ex.printStackTrace();
        }
        }

        @Command(aliases = "migrate", desc = "Migrates the database")
        public void commandMigrate() {
            try{
            database.doMigration(database.getDataSourceVersion());
            } catch (SQLException ex) {
            line("WARNING: Database migration failed, data corruption possible.");
            ex.printStackTrace();
        }
        }

        @Command(aliases = "help", desc = "Displays help information")
        public final void commandHelp(@Optional(value = "") String command) {
            if(!command.equals("")){
                CommandMapping cmd = dispatcher.get(command);
                if(cmd == null){
                    line("Command not found");
                }
                line(cmd.getPrimaryAlias() + " - " + cmd.getDescription().getHelp());
            }
            else{
                for(CommandMapping cmd: dispatcher.getCommands()){
                    line(cmd.getPrimaryAlias() + " - " + cmd.getDescription().getShortDescription());
                }
            }
        }
        
        @Command(aliases = "version", desc="Show schema version")
        public void commandVersion(){
            line("Data source: " + database.getDataSourceVersion());
            try{
            line("Schema: " + database.getSchemaVersion());
            }
            catch (SQLException ex) {
                line("Could not fetch Schema version.");
                ex.printStackTrace();
            }

        }
    }
}
