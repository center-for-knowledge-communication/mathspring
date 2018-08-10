package edu.umass.ckc.wo.util;

import org.apache.log4j.Logger;

import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.sql.*;

import edu.umass.ckc.servlet.servbase.UserException;

public class SqlQuery {

    private static Logger logger = Logger.getLogger(SqlQuery.class);
    static final public String substDelimiter_ = "#";

    /**
     * take a query and substitute the values in the query with the
     * values in the substitute parameter
     *
     * @param query the query string
     * @param subst the substitute parameters
     */
    public static String getQuery(String query,
                                  TreeMap subst) {
        String newquery = query;
        Set keys = subst.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = subst.get(key).toString();
            String delimkey = substDelimiter_ + key + substDelimiter_;
            // find the key in the query, ignore if not found
            newquery = StringUtils.substitute(newquery,
                    delimkey,
                    StringUtils.escapeSQLChars(value));
        }
        return newquery;
    }


    /**
     * read the result set for the query provided.
     * Note:  After calling this method the method closeRS should be called to release the ResultSet object
     *
     * @param db    the database connection
     * @param query the query string
     * @return the result set
     * @throws Exception
     */
    public static ResultSet read(Connection db, String query)
            throws Exception {
        return executeQuery(query, db);
    }


    /**
     * excecute the query and return the result set
     */
    static public boolean execute(String q, Connection db)
            throws Exception {
        Statement stmt = db.createStatement();
        boolean result;
        try {
            result = stmt.execute(q);
        } catch (Exception ex) {
            throw getSqlException(ex, q);
        } finally {
            stmt.close();
        }
        return result;
    }


    /**
     * run an insertion in the prepared statement and retrieve it's identity value
     *
     * @param stmt the prepared statement
     * @return the inserted identity value
     * @throws Exception
     */
    static public int identityInsert(PreparedStatement stmt) throws Exception {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            if (rs.next() != true) {
                throw new Exception("identity insertion failed to return id of inserted value");
            }
            return rs.getInt(1);
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (rs != null) rs.close();
            stmt.close();
        }
    }

    /**
     * run an insertion query and retrieve it's identity value, this assumes
     * that the query q is an insertion query
     * <p/>
     * Note: For compatibility with MySql this function depends on the indentity field
     * of the table having the name "id".
     *
     * @param q  the insertion query
     * @param db the database connection
     * @return the inserted identity value
     * @throws Exception
     */
    static public int identityInsert(String q, Connection db) throws Exception {
        execute(q, db); // do the insert
        // get the table name
        int pos = Math.max(Math.max(q.indexOf("into"), q.indexOf("INTO")), q.indexOf("Into"));
        if (pos == -1)
            throw new UserException("SqlQuery.identityInsert expects INSERT statement to have an INTO keyword.  Got:" + q);
        String tmp = q.substring(pos + 4);
        String tableName = new StringTokenizer(tmp).nextToken();
        String sq = "select id from " + tableName;
        ResultSet rs = executeQuery(sq, db);

        if (rs.next()) {
            int id = rs.getInt(0);
            closeRS(rs);
            return id;
        } else   {
            closeRS(rs);
            throw new UserException("SqlQuery.identityInsert failed to return an id. " +
                    " Table must have identity field named 'id' :" + q);
        }
        // Sql Server specific code
//    q += " SELECT @@IDENTITY";
//    ResultSet rs = read(db,q);
//    if (rs.next())
//    {
//      return rs.getInt(1);
//    }
//    else throw new Exception("Insert failed to return an id " + q);
    }


    /**
     * execute an update query on the database, this is usually an update or insert
     * statement.
     *
     * @param q  the query to run
     * @param db the database connection
     * @return return the number of rows affected
     * @throws Exception
     */
    static public int executeUpdate(String q, Connection db)
            throws Exception {
        Statement stmt = db.createStatement();
        int result;
        try {
            result = stmt.executeUpdate(q);
        } catch (Exception ex) {
            throw getSqlException(ex, q);
        } finally {
            stmt.close();
        }
        return result;
    }

    /**
     * All users of the methods read need to call this when done processing their ResultSet
     *
     * @param rs
     */
    public static void closeRS(ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn(e);
            }
    }


    /**
     * excecute the query and return the result set
     */
    static protected ResultSet executeQuery(String query, Connection db)
            throws Exception {
        Statement stmt = db.createStatement();
        ResultSet results = null;
        try {
            results = stmt.executeQuery(query);
        } catch (Exception ex) {
            throw getSqlException(ex, query);
        } finally {
            // ResultSet shouldn't be closed because caller needs to use it.
//            if (results != null)
//                results.close();
//            stmt.close();
        }
        return results;
    }


    /**
     * excecute the query and return the result set.  Uses JDBC2 to allow caller to
     * use updateXXX methods.
     */
    public static ResultSet readAndUpdate(String query, Connection db)
            throws Exception {
        Statement stmt = db.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet results = null;
        try {
            results = stmt.executeQuery(query);
        } catch (Exception ex) {
            throw getSqlException(ex, query);
        }
        return results;
    }

    /**
     * Given string data, this prepares it for db entry by escaping SQL chars and putting  single quotes around it.
     */
    public static String stringVal(String val) {
        return "'" + StringUtils.escapeSQLChars(val) + "'";
    }

    /**
     * return a new Exception object that has the query string and the
     * exception message of the ex argument.  Only do this if the Exception
     * argument is an instance of SQLException.
     *
     * @param ex    retrieve the message from this exception
     * @param query the query string
     * @return
     */
    public static Exception getSqlException(Exception ex, String query) {
        if (ex instanceof SQLException) {
            StringBuffer error = new StringBuffer();
            error.append("Query String: ").append(query).append(" ");
            error.append("\nSQL Message: ").append(ex.getMessage());
            return new Exception(error.toString());
        }
        return ex;
    }

}
