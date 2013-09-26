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

/**
 * base class for JDBC based data providers Allows easy development of data
 * providers that make use of JDBC
 * 
 * @author James
 * 
 */
public abstract class JDBCStatDataSource {

    // Database connection
    protected Connection connection;
    protected Logger logger;
    protected String type;

   
    public JDBCStatDataSource(String type, String driverClass,Logger logger) throws ClassNotFoundException {
        try {
            Class.forName(driverClass);// load driver
        } catch (ClassNotFoundException e) {
            logger.severe("JDBC " + driverClass + " Library not found!");
            throw e;
        }
    }
    
    public String readSQL(String filename) {
        logger.fine("Loading SQL: " + filename);
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
     * @param scriptName
     *            name of script (sql/load/loadEntity)
     * @param keys
     *            (list of non-standard keys ${KEY_NAME} to replace)
     * 
     *            Scripts support # for status comments and #!/script/path/here
     *            to execute subscripts
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
                logger.info("Executing : " + subScript);
                executeScript(subScript, keys);
                continue;
            } else if (statement.startsWith("#")) {
                logger.info("Status : " + statement.substring(1));
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
}