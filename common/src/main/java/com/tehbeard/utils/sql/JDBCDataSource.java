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

import com.tehbeard.utils.CallbackMatcher;
import com.tehbeard.utils.CallbackMatcher.Callback;
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
 * base class for JDBC based data providers Allows easy development of data
 * providers that make use of JDBC Also includes migration system.
 *
 * @author James
 *
 */
public abstract class JDBCDataSource {

    // Database connection
    protected Connection connection;
    protected final Logger logger;
    private String connectionUrl;
    protected final Properties connectionProperties = new Properties();
    protected final String scriptSuffix;
    private Properties sqlFragments;

    private final Properties sqlTags = new Properties();

    private final JDBCKeyValStore kvStore = new JDBCKeyValStore();

    /**
     * Set a SQL script tag
     *
     * @param key ${key} to replace
     * @param value value to set it to
     */
    public void setTag(String key, String value) {
        sqlTags.setProperty(key, value);
    }

    /**
     * Instantiate class and load driver
     *
     * @param type
     * @param driverClass
     * @param logger
     * @throws ClassNotFoundException
     */
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

    /**
     * Map of name=>sql code to use (useful for small queries that don't need
     * their own file).
     *
     * @param sqlFragments
     */
    protected void setSqlFragments(Properties sqlFragments) {
        this.sqlFragments = sqlFragments;
    }

    /**
     * Set JDBC connection URL
     *
     * @param connectionUrl
     */
    protected void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    /**
     * Connect to database, initialise statements, boot key-val store, run
     * create table script
     *
     * @throws SQLException
     */
    public void setup() throws SQLException {
        connection = DriverManager.getConnection(this.connectionUrl, this.connectionProperties);
        invokeCreateTable();
        kvStore.setup(this);
        prepareStatementCalls();
    }

    public synchronized boolean checkConnection() {
        logger.fine("Checking connection");
        try {
            if ((this.connection == null) || !this.connection.isValid(0)) {
                logger.fine("Connection down, reestablishing.");
                setup();
            }
        } catch (SQLException e) {
            this.connection = null;
            return false;
        } catch (AbstractMethodError e) {
            //Catch SQLite error??
        }

        return this.connection != null;
    }

    /**
     * Automagically prepares the fields that are Statement and have a @SQL...
     * compatible annotation
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
                        } catch (Exception ex) {
                            throw new SQLException("Unknown error on script " + script.value(), ex);
                        }
                    } else if (f.isAnnotationPresent(SQLFragment.class)) {
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
                        } catch (Exception ex) {
                            throw new SQLException("Unknown error on script fragment " + script.value(), ex);
                        }
                    } else if (f.isAnnotationPresent(SQLRawSet.class)) {
                        SQLRawSet set = f.getAnnotation(SQLRawSet.class);
                        try {
                            String script = null;
                            int flags = 0;
                            for (SQLRaw raw : set.value()) {
                                if (raw.type().equals(this.scriptSuffix)) {
                                    script = raw.sql();
                                    flags = raw.flags();
                                }
                            }
                            if (script == null) {
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
                        } catch (Exception ex) {
                            throw new SQLException("Unknown error on script raw " + f.getName(), ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * Invokes the field with @SQLInitScript
     *
     * @SQLInitScript annotation to create tables etc.
     * @throws SQLException
     */
    private void invokeCreateTable() throws SQLException {
        for (Class<?> f : getClasses()) {

            if (f.isAnnotationPresent(SQLInitScript.class)) {
                
                executeScript(f.getAnnotation(SQLInitScript.class).value());
            }
        }
    }

    /**
     * Helper method to get all classes for parsing.
     *
     * @return
     */
    private List<Class<?>> getClasses() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        Class<?> c = this.getClass();
        while (c != Object.class) {
            classes.add(c);
            c = c.getSuperclass();
        }
        return classes;
    }

    /**
     * Close JDBC connection
     *
     * @throws SQLException
     */
    public void teardown() throws SQLException {
        connection.close();
    }

    /**
     * Read SQL from a resource in the JAR.
     *
     * @param filename
     * @return
     */
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
        return processSQL(sql);

    }

    /**
     * Execute a script
     *
     * @param scriptName name of script (sql/load/loadEntity)
     * @param keys (list of non-standard keys ${KEY_NAME} to replace)
     *
     * Scripts support # for status comments and #!/script/path/here to execute
     * subscripts
     * @throws SQLException
     */
    public void executeScript(String scriptName) throws SQLException {
        executeScript(scriptName, new HashMap<String, String>());
    }

    private static final CallbackMatcher matcher = new CallbackMatcher("\\$\\{([A-Za-z0-9_]*)\\}");

    public String processSQL(String sql) {
        return processSQL(sql, new HashMap<String, String>());
    }

    public String processSQL(String sql, final Map<String, String> keys) {
        return matcher.replaceMatches(sql, new Callback() {
            @Override
            public String foundMatch(MatchResult result) {
                if (keys.containsKey(result.group(1))) {
                    return keys.get(result.group(1));
                }
                if (sqlTags.containsKey(result.group(1))) {
                    return sqlTags.getProperty(result.group(1));
                }
                throw new IllegalArgumentException("Key " + result.group(1) + " not found.");
            }
        }).trim();
    }

    public String getSQLTag(String tag) {
        return sqlTags.getProperty(tag, "");
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
     *
     * @param file
     * @return
     */
    public abstract boolean generateBackup(String name);

    /**
     * restore the database from a backup.
     *
     * @param file
     * @return
     */
    public abstract boolean restoreBackup(String name);

    /**
     * Return the migration script path for use with readSQL()
     *
     * @param fromVersion
     * @param toVersion
     * @return
     */
    protected abstract String getMigrationScriptPath(int toVersion);

    /**
     * Migrates database to the target version.
     *
     * @param toVersion
     * @return
     * @throws SQLException
     */
    public boolean doMigration(int toVersion) throws SQLException {
        int fromVersion = getSchemaVersion();
        String backupName = "migration." + Math.floor(System.currentTimeMillis() / 1000L) + "@v" + fromVersion;
        //Do backup
        if (!generateBackup(backupName)) {
            logger.severe("Failed to generate backup file, aborting migration");
            throw new SQLException("Backup generation failed.");
        }

        connection.setAutoCommit(false);
        for (int migratingTo = fromVersion + 1; migratingTo <= toVersion; migratingTo++) {
            try {
                runCodeFor(migratingTo, PreUpgrade.class);
                executeScript(getMigrationScriptPath(migratingTo));
                runCodeFor(migratingTo, PostUpgrade.class);

                getKeyValStore().set(KEY_SCHEMA_VERSION, "" + migratingTo, false);
                connection.commit();
            } //Enable autocommit
            //If error, rollback, restore backup
            catch (Exception ex) {
                Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                connection.rollback();
                restoreBackup(backupName);
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        }
        connection.setAutoCommit(true);

        //Enable autocommit
        //If error, rollback, restore backup
        return true;
    }

    /**
     * Used for doMigration() to call java code during migration either before
     * or after a migration script.
     *
     * @param version
     * @param ann
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
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

    /**
     * Get the key-val store for this database.
     *
     * @return
     */
    public JDBCKeyValStore getKeyValStore() {
        return this.kvStore;
    }

    /**
     * Return the schema version this database is designed for.
     *
     * @return
     */
    public abstract int getDataSourceVersion();

    /**
     * Return the schema version of the database.
     *
     * @return
     * @throws SQLException
     */
    public int getSchemaVersion() throws SQLException {
        JDBCKeyValStore.KeyValEntry key = getKeyValStore().get(KEY_SCHEMA_VERSION);
        if (key == null) {
            getKeyValStore().set(KEY_SCHEMA_VERSION, "" + getDataSourceVersion(), true);
            key = getKeyValStore().get(KEY_SCHEMA_VERSION);
        }
        return Integer.parseInt(key.value);
    }
    public static final String KEY_SCHEMA_VERSION = "schema_version";

    public static final class JDBCKeyValStore {

        public class KeyValEntry {

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
        @SQLRawSet(@SQLRaw(sql = "SELECT `key`,`value`,`added` FROM ${PREFIX}_keyval WHERE `key`=? order by `id` DESC LIMIT 1", type = "sql"))
        private PreparedStatement get;

        @SQLRawSet(@SQLRaw(sql = "SELECT `key`,`value`,`added` FROM (SELECT * FROM ${PREFIX}_keyval WHERE `key` IN (?) ORDER BY `id` DESC) _k GROUP BY `key` HAVING `value` IS NOT NULL", type = "sql"))
        private PreparedStatement getAll;

        @SQLRawSet(@SQLRaw(sql = "SELECT `key`,`value`,`added` FROM (SELECT * FROM ${PREFIX}_keyval WHERE `key` NOT IN (?) ORDER BY `id` DESC) _k GROUP BY `key` HAVING `value` IS NOT NULL", type = "sql"))
        private PreparedStatement getAllExcept;

        @SQLRawSet(@SQLRaw(sql = "SELECT `key`,`value`,`added` FROM ${PREFIX}_keyval WHERE `key`=? order by `id` DESC", type = "sql"))
        private PreparedStatement getIterations;

        @SQLRawSet(@SQLRaw(sql = "INSERT INTO ${PREFIX}_keyval (`key`,`value`,`added`,`noCompact`) VALUES(?,?,?,?)", type = "sql"))
        private PreparedStatement add;

        @SQLRawSet(@SQLRaw(sql = "DELETE FROM ${PREFIX}_keyval WHERE `noCompact` = 0 AND `id` NOT IN (SELECT `id` FROM (SELECT * FROM ${PREFIX}_keyval ORDER BY `id` DESC) _k GROUP BY `key` HAVING `value` IS NOT NULL)", type = "sql"))
        private PreparedStatement compactAll;

        @SQLRawSet(@SQLRaw(sql = "DELETE FROM ${PREFIX}_keyval WHERE `key` = ? AND (`id` NOT IN (SELECT `id` FROM (SELECT * FROM ${PREFIX}_keyval ORDER BY `id` DESC) _k GROUP BY `key` HAVING `value` IS NOT NULL) OR `value` IS NULL)", type = "sql"))
        private PreparedStatement compact;

        private Connection con;
        private JDBCDataSource source;

        private void setup(JDBCDataSource source) throws SQLException {
            this.source = source;
            con = source.connection;
            con.prepareStatement(source.processSQL("CREATE TABLE IF NOT EXISTS ${PREFIX}_keyval(`id` INT AUTO_INCREMENT,`noCompact` BOOLEAN NOT NULL DEFAULT FALSE, `key` VARCHAR( 255 ) NOT NULL , `value` VARCHAR( 255 ) NULL , `added` TIMESTAMP NOT NULL ,PRIMARY KEY (  `id` ))")).execute();
            for (Field f : this.getClass().getDeclaredFields()) {
                if (PreparedStatement.class.isAssignableFrom(f.getType())) {
                    if (f.isAnnotationPresent(SQLRawSet.class)) {
                        SQLRawSet set = f.getAnnotation(SQLRawSet.class);
                        try {
                            String script = null;
                            int flags = 0;
                            for (SQLRaw raw : set.value()) {
                                if (raw.type().equals(source.scriptSuffix) || (script == null && raw.type().equals("sql"))) {
                                    script = raw.sql();
                                    flags = raw.flags();
                                }
                            }
                            if (script == null) {
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
                        } catch (Exception ex) {
                            throw new SQLException("Unknown error on script raw " + f.getName(), ex);
                        }
                    }
                }
            }
        }

        public void set(String key, String value) throws SQLException{
            set(key,value,false);
        }
                
        /**
         * Sets the value of a key
         *
         * @param key
         * @param value
         * @param noCompact
         * @throws SQLException
         */
        public void set(String key, String value, boolean noCompact) throws SQLException {
            add.setString(1, key);
            add.setString(2, value);
            add.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            add.setBoolean(4, noCompact);
            add.execute();
        }

        /**
         * Return current value of key
         *
         * @param key
         * @return
         * @throws SQLException
         */
        public KeyValEntry get(String key) throws SQLException {
            get.setString(1, key);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                KeyValEntry k = new KeyValEntry(rs.getString(1), rs.getString(2), rs.getTimestamp(3).getTime());
                rs.close();
                return k;
            }
            return null;
        }
        
        public boolean hasKey(String key) throws SQLException{
            get.setString(1, key);
            ResultSet rs = get.executeQuery();
            boolean res = rs.next();
            rs.close();
            return res;
        }

        /**
         * Get all keys
         *
         * @return
         * @throws SQLException
         */
        public Map<String, KeyValEntry> getAll() throws SQLException {
            getAll.setString(1, "");
            ResultSet rs = getAll.executeQuery();
            Map<String, KeyValEntry> res = new HashMap<String, KeyValEntry>();
            while (rs.next()) {
                res.put(rs.getString(1), new KeyValEntry(rs.getString(1), rs.getString(2), rs.getTimestamp(3).getTime()));
            }
            rs.close();
            return res;
        }

        /**
         * Get all iterations of a key
         *
         * @param key
         * @return
         * @throws SQLException
         */
        public List<KeyValEntry> getIterations(String key) throws SQLException {
            getIterations.setString(1, key);
            ResultSet rs = getIterations.executeQuery();
            List<KeyValEntry> res = new ArrayList<KeyValEntry>();
            while (rs.next()) {
                res.add(new KeyValEntry(rs.getString(1), rs.getString(2), rs.getTimestamp(3).getTime()));
            }
            rs.close();
            return res;
        }

        /**
         * Remove all old key-val entries, leaving only the latest (does not
         * apply to those marked noCompact)
         *
         * @throws SQLException
         */
        public void compactAll() throws SQLException {
            compactAll.execute();

        }

        /**
         * Compacts a specific key, ignores noCompact
         *
         * @param key
         * @throws SQLException
         */
        public void compact(String key) throws SQLException {
            compact.setString(1, key);
            compact.execute();
            //compactAll.execute();
        }
    }

}
