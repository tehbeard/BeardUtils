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
import java.sql.ResultSet;
import java.sql.Timestamp;
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
    
    private final Properties sqlTags = new Properties();
    
    private final JDBCKeyValStore kvStore = new JDBCKeyValStore();
    
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
        kvStore.setup(this);
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
                    }else if (f.isAnnotationPresent(SQLRawSet.class)) {
                        SQLRawSet set = f.getAnnotation(SQLRawSet.class);
                        try {
                            String script = null;
                            int flags = 0;
                            for(SQLRaw raw : set.value()){
                                if(raw.type().equals(this.scriptSuffix)){
                                    script = raw.sql();
                                    flags = raw.flags();
                                }
                            }
                            if(script == null){
                                throw new SQLException("No @SQLRaw found for " + f.getName() + " for type " + this.scriptSuffix);
                            }
                            f.setAccessible(true);
                            f.set(this, this.connection.prepareStatement(this.processSQL(script), flags));
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Failed to load raw", ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Access error on script raw", ex);
                        } catch(Exception ex){
                            throw new SQLException("Unknown error on script raw " + f.getName(),ex);
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
        }).trim();
    }

    public void executeScript(String scriptName, final Map<String, String> keys) throws SQLException {
        

        String[] sqlStatements = readSQL(scriptName).split("\\;");
        for (String s : sqlStatements) {
            String statement = processSQL(s, keys);
            if (statement.startsWith("#!")) {
                String subScript = statement.substring(2);
                executeScript(subScript, keys);
                continue;
            } else if (statement.startsWith("#")) {
                logger.log(Level.INFO, "Status : {0}", statement.substring(1));
            } else {
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
    protected abstract boolean generateBackup(String name);
    
    /**
     * restore the database from a backup.
     * @param file
     * @return
     */
    protected abstract boolean restoreBackup(String name);
    
    
    /**
     * Return the migration script path.
     * @param fromVersion
     * @param toVersion
     * @return 
     */
    protected abstract String getMigrationScriptPath(int toVersion);

    public boolean doMigration(int toVersion) throws SQLException {
        int fromVersion = Integer.parseInt(getKeyValStore().get("schema_version").value);
        String backupName = "migration." + Math.floor(System.currentTimeMillis()/1000L) + "@v" + fromVersion;
        //Do backup
        if(!generateBackup(backupName)){
            logger.severe("Failed to generate backup file, aborting migration");
            throw new SQLException("Backup generation failed.");
        }
        
        connection.setAutoCommit(false);
        for(int migratingTo = fromVersion+1;migratingTo<=toVersion;migratingTo++){
            try {
                runCodeFor(migratingTo, PreUpgrade.class);
                executeScript(getMigrationScriptPath(migratingTo));
                runCodeFor(migratingTo, PostUpgrade.class);
                
                getKeyValStore().set("schema_version", "" + migratingTo,false);
                connection.commit();
            }
            //Enable autocommit
            //If error, rollback, restore backup
            catch (Exception ex) {
                Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                connection.rollback();
                restoreBackup(backupName);
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
    
    public JDBCKeyValStore getKeyValStore(){
        return this.kvStore;
    }
    
    public static final class JDBCKeyValStore {
        
        public class KeyValEntry{
            public final String key;
            public final String value;
            public final long added;

            public KeyValEntry(String key, String value, long added) {
                this.key = key;
                this.value = value;
                this.added = added;
            }

            @Override
            public String toString() {
                return "KeyValEntry{" + "key=" + key + ", value=" + value + ", added=" + added + '}';
            }

        }
        
        @SQLRawSet(value=@SQLRaw(sql="CREATE TABLE IF NOT EXISTS ${PREFIX}_keyval(`id` INT AUTO_INCREMENT,`noCompact` BOOLEAN NOT NULL DEFAULT FALSE, `key` VARCHAR( 255 ) NOT NULL , `value` VARCHAR( 255 ) NULL , `added` TIMESTAMP NOT NULL ,PRIMARY KEY (  `id` ))",type="sql"))
        private PreparedStatement create;
        
        @SQLRawSet(value=@SQLRaw(sql="SELECT `key`,`value`,`added` FROM ${PREFIX}_keyval WHERE `key`=? order by `added` DESC LIMIT 1",type="sql"))
        private PreparedStatement get;
        
        @SQLRawSet(value=@SQLRaw(sql="SELECT * FROM (SELECT * FROM ${PREFIX}_keyval WHERE `key` IN (?) ORDER BY `added` DESC) _k GROUP BY `key` HAVING `value` IS NOT NULL",type="sql"))
        private PreparedStatement getAll;
        
        @SQLRawSet(value=@SQLRaw(sql="SELECT * FROM (SELECT * FROM ${PREFIX}_keyval WHERE `key` NOT IN (?) ORDER BY `added` DESC) _k GROUP BY `key` HAVING `value` IS NOT NULL",type="sql"))
        private PreparedStatement getAllExcept;
        
        @SQLRawSet(value=@SQLRaw(sql="SELECT * FROM ${PREFIX}_keyval WHERE `key`=? order by `added` DESC",type="sql"))
        private PreparedStatement getIterations;
        
        @SQLRawSet(value=@SQLRaw(sql="INSERT INTO ${PREFIX}_keyval (`key`,`value`,`added`,`noCompact`) VALUES(?,?,?,?)",type="sql"))
        private PreparedStatement add;
        
        @SQLRawSet(value=@SQLRaw(sql="DELETE FROM kv USING ${PREFIX}_keyval kv INNER JOIN (SELECT * FROM (SELECT * FROM ${PREFIX}_keyval WHERE `noCompact`=0 ORDER BY `id` DESC) as kv GROUP BY `key`) as _k ON ( ( kv.`key` = _k.`key` AND kv.`id` < _k.`id` ) OR ( kv.`key` = _k.`key` AND _k.`value` IS NULL ) )",type="sql"))
        private PreparedStatement compactAll;
        
        @SQLRawSet(value=@SQLRaw(sql="DELETE FROM kv USING ${PREFIX}_keyval kv INNER JOIN (SELECT * FROM ${PREFIX}_keyval WHERE `key`=? order by `id` DESC LIMIT 1) as _k ON ( ( kv.`key` = _k.`key` AND kv.`id` < _k.`id` ) OR ( kv.`key` = _k.`key` AND _k.`value` IS NULL ) )",type="sql"))
        private PreparedStatement compact;
        
        private Connection con;
        private JDBCDataSource source;

        private void setup(JDBCDataSource source) throws SQLException{
            this.source = source;
            con = source.connection;
            for (Field f : this.getClass().getDeclaredFields()) {
                if (PreparedStatement.class.isAssignableFrom(f.getType())) {
                    if (f.isAnnotationPresent(SQLRawSet.class)) {
                        SQLRawSet set = f.getAnnotation(SQLRawSet.class);
                        try {
                            String script = null;
                            int flags = 0;
                            for(SQLRaw raw : set.value()){
                                if(raw.type().equals(source.scriptSuffix)){
                                    script = raw.sql();
                                    flags = raw.flags();
                                }
                            }
                            if(script == null){
                                throw new SQLException("No @SQLRaw found for " + f.getName() + " for type " + source.scriptSuffix);
                            }
                            f.setAccessible(true);
                            f.set(this, source.connection.prepareStatement(source.processSQL(script), flags));
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Failed to load raw", ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                            throw new SQLException("Access error on script raw", ex);
                        } catch(Exception ex){
                            throw new SQLException("Unknown error on script raw " + f.getName(),ex);
                        }
                    }
                }
            }
            create.execute();
        }
        
        /**
         * Sets the value of a key
         * @param key
         * @param value
         * @throws SQLException 
         */
        public void set(String key, String value,boolean noCompact) throws SQLException{
            add.setString(1, key);
            add.setString(2, value);
            add.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            add.setBoolean(4, noCompact);
            add.execute();
        }
        
        
        
        /**
         * Return current value of key
         * @param key
         * @return
         * @throws SQLException 
         */
        public KeyValEntry get(String key) throws SQLException{
            get.setString(1,key);
            ResultSet rs = get.executeQuery();
            if(rs.next()){
                KeyValEntry k = new KeyValEntry(rs.getString(1), rs.getString(2), rs.getTimestamp(3).getTime());
                rs.close();
                return k;
            }
            return null;
        }
        
        /**
         * Get all keys provided
         * @param key
         * @return
         * @throws SQLException 
         */
        public Map<String,String> getAll(String... key) throws SQLException{
            getAll.setArray(1, con.createArrayOf("varchar", key));
            ResultSet rs = getAll.executeQuery();
            Map<String,String> res = new HashMap<String, String>();
            while(rs.next()){
                res.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
            return res;
        }
        
        /**
         * Get all keys except those provided.
         * @param key
         * @return
         * @throws SQLException 
         */
        public Map<String,String> getAllExcept(String... key) throws SQLException{
            getAllExcept.setArray(1, con.createArrayOf("varchar", key));
            ResultSet rs = getAllExcept.executeQuery();
            Map<String,String> res = new HashMap<String, String>();
            while(rs.next()){
                res.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
            return res;
        }
        
        /**
         * Get all iterations of a key
         * @param key
         * @return
         * @throws SQLException 
         */
        public Map<String,Map<Long,String>> getIterations(String key) throws SQLException{
            getIterations.setString(1,  key);
            ResultSet rs = getIterations.executeQuery();
            Map<String,Map<Long,String>> res = new HashMap<String,Map<Long,String>>();
            while(rs.next()){
                if(!res.containsKey(rs.getString(1))){
                    res.put(rs.getString(1), new HashMap<Long, String>());
                }
                res.get(rs.getString(1)).put(rs.getTimestamp(3).getTime(), rs.getString(2));
            }
            rs.close();
            return res;
        }
        
        public void compactAll() throws SQLException{
            compactAll.execute();

        }
        
        public void compact(String key) throws SQLException{
            compact.setString(1, key);
            compact.execute();
            //compactAll.execute();
        }
    }
    
}