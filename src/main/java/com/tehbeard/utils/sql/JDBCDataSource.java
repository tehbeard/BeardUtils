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
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
    private final String connectionUrl;
    private final Properties connectionProperties;
    protected final String type;

    public JDBCDataSource(String connectionUrl, Properties connectionProperties, String type, String driverClass, Logger logger) throws ClassNotFoundException {
        try {
            Class.forName(driverClass);// load driver
            this.logger = logger;
            this.type = type;
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "JDBC {0} Library not found!", driverClass);
            throw e;
        }
        this.connectionUrl = connectionUrl;
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
        for (Class c : getClasses()) {
            for (Field f : c.getDeclaredFields()) {
                if (PreparedStatement.class.isAssignableFrom(f.getType())) {
                    if (f.isAnnotationPresent(SQLScript.class)) {
                        try {
                            SQLScript script = f.getAnnotation(SQLScript.class);
                            f.set(this, getStatementFromScript(script.value(), script.flags()));
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(JDBCDataSource.class.getName()).log(Level.SEVERE, null, ex);
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
        for (Class c : getClasses()) {
            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(SQLInitScript.class)) {
                    try {
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

    private List<Class> getClasses() {
        List<Class> classes = new ArrayList<Class>();
        Class c = this.getClass();
        while (c != Object.class) {
            classes.add(c);
            c = c.getSuperclass();
        }
        return classes;
    }

    public void teardown() {
    }

    public String readSQL(String filename) {
        logger.log(Level.FINE, "Loading SQL: {0}", filename);
        InputStream is = getClass().getResourceAsStream(filename + "." + type);
        if (is == null) {
            is = getClass().getResourceAsStream(filename + ".sql");
        }
        if (is == null) {
            throw new IllegalArgumentException("No SQL file found with name " + filename);
        }
        Scanner scanner = new Scanner(is);
        String sql = scanner.useDelimiter("\\Z").next().replaceAll("\\Z", "").replaceAll("\\n|\\r", "");
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

    public void executeScript(String scriptName, final Map<String, String> keys) throws SQLException {
        CallbackMatcher matcher = new CallbackMatcher("\\$\\{([A-Za-z0-9_]*)\\}");

        String[] sqlStatements = readSQL(scriptName).split("\\;");
        for (String s : sqlStatements) {
            String statement = matcher.replaceMatches(s, new Callback() {
                @Override
                public String foundMatch(MatchResult result) {
                    if (keys.containsKey(result.group(1))) {
                        return keys.get(result.group(1));
                    }
                    return "";
                }
            });

            if (statement.startsWith("#!")) {
                String subScript = statement.substring(2);
                logger.log(Level.INFO, "Executing : {0}", subScript);
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

    protected abstract String getMigrationScriptPath(int fromVersion, int toVersion);

    public void doMigration(int fromVersion, int toVersion) {
        //TODO - Add migration handler
    }

    private void runCodeFor(int version, Class<? extends Annotation> ann) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Class c : getClasses()) {
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
     * DBVersion annotation
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DBVersion {

        int value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PreUpgrade {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PostUpgrade {
    }
}