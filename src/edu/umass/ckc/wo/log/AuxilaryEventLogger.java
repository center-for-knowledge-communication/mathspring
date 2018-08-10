package edu.umass.ckc.wo.log;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: Nov 19, 2009
 * Time: 10:51:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface AuxilaryEventLogger {

    /**
     *
     * @param conn
     * @return the ID of the row added so that the eventlog can point to it.
     */
    public int logEntry(Connection conn) throws SQLException;

    public String getAuxTable () ;
}
