package edu.umass.ckc.wo.db;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Jun 25, 2007
 * Time: 12:19:52 PM
 */
public class BaseMgr {

    /**
     * A useful method for closing a ResultSet and Statement
     * @param rs
     * @param stmt
     * @throws SQLException
     */
    public void closeQuery (ResultSet rs, Statement stmt) throws SQLException {
        if (rs != null)
            rs.close();
        if (stmt != null)
            stmt.close();
    }

    public void closeQuery (Statement stmt) throws SQLException {
        if (stmt != null)
            stmt.close();
    }
}
