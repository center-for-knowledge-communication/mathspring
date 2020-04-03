package edu.umass.ckc.wo.log;


import edu.umass.ckc.wo.tutor.Settings;


import java.sql.*;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.springframework.jndi.JndiTemplate;

/**

 * Frank 	12-09-19	Issue #21 add teacher activity logger support
 * Frank 	12-21-19	Issue #21r2 add reworked database access setup

 */
public class TeacherLogger {
    //private SessionManager smgr;
    private static Logger logger = Logger.getLogger(TeacherLogger.class);

    Connection conn;
    DataSource ds;
    public static final String TeacherEventLog = "teacherlog";

    public static final String[] TEACHER_EVENTS = { "Login", "Logout" };

    public TeacherLogger (ServletContext ctx) {
        //this.smgr = smgr;
       try {
            JndiTemplate jndiTemplate = new JndiTemplate();
            String dataSourceLookup = ctx.getInitParameter("wodb.datasource");
            Settings.webContentPath = ctx.getInitParameter("webContentPath");
            ds = (DataSource) jndiTemplate.lookup("java:comp/env/" + dataSourceLookup);
            this.conn = ds.getConnection();
        } catch (Exception e) {
        	logger.error(e.getMessage());
        	logger.error(e.getStackTrace());
        }
    }
    
    public int insertLoginEntry(int teacherId, String action, String activityName) throws SQLException {

        return logEntryWorker(teacherId, 0, action, activityName);
    }

    public int logEntryWorker(int teacherId, int sessionId, String action, String activityName) {

    	
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "insert into " + TeacherEventLog + " (teacherId,action,activityName,time) values (?,?,?,?)";

            ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, teacherId);
            ps.setString(2, action);
            if (activityName == null)
                ps.setNull(3,Types.VARCHAR);
            else ps.setString(3,activityName);
            ps.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            int newId = rs.getInt(1);
            ps.close();
            return newId;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public Connection getConn( ) {
    	return(this.conn);
    }
}
