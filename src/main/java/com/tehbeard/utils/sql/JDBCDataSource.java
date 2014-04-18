package com.tehbeard.utils.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.MatchResult;

import com.tehbeard.utils.misc.CallbackMatcher;
import com.tehbeard.utils.misc.CallbackMatcher.Callback;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * base class for JDBC based data providers Allows easy development of data providers that make use of JDBC
 * Also includes migration system.
 * @author James
 *
 */
public abstract class JDBCDataSource {

    // Database connection
    protected Connection connection;
    protected final Logger logger;
    private String connectionUrl;
    private Properties connectionProperties;
    protected final String scriptSuffix;
    private Properties sqlFragments;
    
    private Properties sqlTags = new Properties();
    
    public void setTag(String key,String value){
        sqlTags.setProperty(key, value);
    }

    public JDBCDataSource(String type, String driverClass, Logger logger) throws ClassNotFoundException {
        try {
            Class.forName(driverClass);// load driver
            this.logger = logger;
            this.scriptSuffix = type;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "JDBC {0} Library not found!", driverClass);
            throw e;
        }
    }

    protected void setSqlFragments(Properties sqlFragments) {
        this.sqlFragments = sqlFragments;
    }

    protected void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    protected void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
    
    

    public void setup() throws SQLException {
        connection = DriverManager.getConnection(this.connectionUrl, this.connectionProperties);
        prepareStatementCalls();
        invokeCreateTable();
    }

    /**
     * Automagically prepares the fields that are Statement and have
     *
     * @SQLScript annotations
     * @throws SQLException
     */
    private void prepareStatementCalls() throws SQLException {
        for (Class<?> c : getClasses()) {
            for (Field f : c.getDeclaredFields()) {
                if (PreparedStatement.class.isAssignableFrom(f.getType())) {
                    if (f.isAnnotationPresent(SQLScript.class)) {
                        SQLScript script = null;
                        try {
                            script = f.getAnnotation(SQLScript.class);
                            f.setAccessible(true);
                            f.set(this, getStatementFromScript(script.value(), script.flags()));
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Failed to load script", ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Access error on script field", ex);
                        } catch(Exception ex){
                            throw new SQLException("Unknown error on script " + script.value(),ex);
                        }
                    }else if (f.isAnnotationPresent(SQLFragment.class)) {
                        SQLFragment script = null;
                        try {
                            script = f.getAnnotation(SQLFragment.class);
                            f.setAccessible(true);
                            f.set(this, this.connection.prepareStatement(processSQL(sqlFragments.getProperty(script.value() + "." + scriptSuffix)), script.flags()));
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Failed to load fragment", ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Access error on script fragment", ex);
                        } catch(Exception ex){
                            throw new SQLException("Unknown error on script fragment " + script.value(),ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * Invokes the field with
     *
     * @SQLInitScript annotation to create tables etc.
     * @throws SQLException
     */
    private void invokeCreateTable() throws SQLException {
        for (Class<?> c : getClasses()) {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(SQLInitScript.class)) {
                    try {
                        f.setAccessible(true);
                        PreparedStatement s = (PreparedStatement) f.get(this);
                        s.execute();
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private List<Class<?>> getClasses() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        Class<?> c = this.getClass();
        while (c != Object.class) {
            classes.add(c);
            c = c.getSuperclass();
        }
        return classes;
    }

    public void teardown() throws SQLException {
        connection.close();
    }

    public String readSQL(String filename) {
        logger.log(Level.FINE, "Loading SQL: {0}", filename);
        InputStream is = getClass().getClassLoader().getResourceAsStream(filename + "." + scriptSuffix);
        if (is == null) {
            is = getClass().getClassLoader().getResourceAsStream(filename + ".sql");
        }
        if (is == null) {
            throw new IllegalArgumentException("No SQL file found with name " + filename);
        }
        Scanner scanner = new Scanner(is);
        String sql = scanner.useDelimiter("\\Z").next().replaceAll("\\Z", "").replaceAll("\\n|\\r", " ");
        scanner.close();
        return sql;

    }

    /**
     * Execute a script
     *
     * @param scriptName name of script (sql/load/loadEntity)
     * @param keys (list of non-standard keys ${KEY_NAME} to replace)
     *
     * Scripts support # for status comments and #!/script/path/here to execute subscripts
     * @throws SQLException
     */
    public void executeScript(String scriptName) throws SQLException {
        executeScript(scriptName, new HashMap<String, String>());
    }
    
    private static final CallbackMatcher matcher = new CallbackMatcher("\\$\\{([A-Za-z0-9_]*)\\}");
    
    public String processSQL(String sql){return processSQL(sql,new HashMap<String, String>());}
    public String processSQL(String sql, final Map<String, String> keys){
        return matcher.replaceMatches(sql, new Callback() {
            @Override
            public String foundMatch(MatchResult result) {
                if (keys.containsKey(result.group(1))) {
                    return keys.get(result.group(1));
                }
                if (sqlTags.containsKey(result.group(1))) {
                    return sqlTags.getProperty(result.group(1));
                }
                return "";
            }
        });
    }

    public void executeScript(String scriptName, final Map<String, String> keys) throws SQLException {
        

        String[] sqlStatements = readSQL(scriptName).split("\\;");
        for (String s : sqlStatements) {
            String statement = processSQL(s, keys);

            if (statement.startsWith("#!")) {
                String subScript = statement.substring(2);
                logger.log(Level.INFO, "Executing : {0}", subScript);
                executeScript(subScript, keys);
                continue;
            } else if (statement.startsWith("#")) {
                logger.log(Level.INFO, "Status : {0}", statement.substring(1));
            } else {
                logger.log(Level.INFO,"Executing : " + statement);
                this.connection.prepareStatement(statement).execute();
            }
        }

    }

    public PreparedStatement getStatementFromScript(String scriptName, int flags) throws SQLException {
        return this.connection.prepareStatement(readSQL(scriptName), flags);
    }

    public PreparedStatement getStatementFromScript(String scriptName) throws SQLException {
        return this.connection.prepareStatement(readSQL(scriptName));
    }

    /**
     * Generate a backup of the database.
     * @param file
     * @return
     */
    protected abstract boolean generateBackup(File file);
    
    /**
     * restore the database from a backup.
     * @param file
     * @return
     */
    protected abstract boolean restoreBackup(File file);
    
    protected abstract File getTempDir();
    
    /**
     * Return the migration script path.
     * @param fromVersion
     * @param toVersion
     * @return 
     */
    protected abstract String getMigrationScriptPath(int toVersion);

    public boolean doMigration(int fromVersion, int toVersion) throws SQLException {
        //Do backup
        File backupFile = new File(getTempDir(),"backup.db");
        if(!generateBackup(backupFile)){
            logger.severe("Failed to generate backup file, aborting migration");
            throw new SQLException("Backup generation failed.");
        }
        
        connection.setAutoCommit(false);
        for(int migratingTo = fromVersion+1;migratingTo<=toVersion;migratingTo++){
            try {
                runCodeFor(migratingTo, PreUpgrade.class);
                executeScript(getMigrationScriptPath(migratingTo));
                runCodeFor(migratingTo, PostUpgrade.class);
                
                connection.commit();
            }
            //Enable autocommit
            //If error, rollback, restore backup
            catch (Exception ex) {
                Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                connection.rollback();
                restoreBackup(backupFile);
                return false;
            }
            finally{
                connection.setAutoCommit(true);
            }
        }
        connection.setAutoCommit(true);
        
        
        //Enable autocommit
        
        //If error, rollback, restore backup
        return true;
    }

    private void runCodeFor(int version, Class<? extends Annotation> ann) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Class<?> c : getClasses()) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(ann)) {
                    if (m.getAnnotation(DBVersion.class).value() == version) {
                        m.invoke(this);
                    }
                }
            }
        }
    }
}