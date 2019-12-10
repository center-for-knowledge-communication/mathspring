package edu.umass.ckc.wo.log;

import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.event.NavigationEvent;
import edu.umass.ckc.wo.smgr.SessionManager;
import edu.umass.ckc.wo.event.tutorhut.*;
import edu.umass.ckc.wo.tutor.response.HintResponse;
import edu.umass.ckc.wo.tutor.response.ProblemResponse;
import edu.umass.ckc.wo.tutor.response.Response;
import edu.umass.ckc.wo.tutor.response.InterventionResponse;
import edu.umass.ckc.wo.tutor.response.AttemptResponse;
import edu.umass.ckc.wo.tutor.response.ExampleResponse;
import edu.umass.ckc.wo.tutor.response.HintSequenceResponse;
import edu.umass.ckc.wo.content.Hint;

import java.sql.*;
import java.util.List;

/**

 * Frank 	12-09-19	Issue #21 add teacher activity logger support

 */
public class TeacherLogger {
    //private SessionManager smgr;
    Connection conn;
    public static final String TeacherEventLog = "teacherlog";

    public static final String[] TEACHER_EVENTS = { "Login", "Logout" };

    boolean tableExists = true;
    


    public TeacherLogger (Connection conn) {
        //this.smgr = smgr;
        this.conn = conn;
        
        testTable();
        
        if (!tableExists) {
        	createTable();
        }

    }

    private void testTable() {
 
    	
    	
     String querytest = "SELECT COLUMN_NAME FROM information_schema.columns where table_name = 'teacherlog';"; 
     try {

    	    PreparedStatement ps = null;
    	    ResultSet rs = null;        
            ps = this.conn.prepareStatement(querytest);
            rs = ps.executeQuery();
            if (!rs.next()) {
            	tableExists = false;
        	}
            ps.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }        
    }
    
    private void createTable() {
    	
        String querycreate = 
        		
        		"CREATE TABLE `teacherlog` (" + 
        		"  `id` int(10) unsigned NOT NULL AUTO_INCREMENT," + 
        		"  `teacherId` int(11) DEFAULT NULL," + 
        		"  `sessNum` int(11) DEFAULT NULL," + 
        		"  `action` varchar(45) NOT NULL," + 
        		"  `elapsedTime` int(10) unsigned NOT NULL DEFAULT 0," + 
        		"  `activityName` varchar(200) DEFAULT NULL," + 
        		"  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," + 
        		"  PRIMARY KEY (`id`)," + 
        		"  KEY `FK_eventlog_sess` (`sessNum`)," + 
        		"  KEY `tchid` (`teacherId`)," + 
        		"  KEY `timeindex` (`time`)," + 
        		"  CONSTRAINT `FK_teventlog_sess` FOREIGN KEY (`sessNum`) REFERENCES `session` (`id`)," + 
        		"  CONSTRAINT `FK_teventlog_tch` FOREIGN KEY (`teacherId`) REFERENCES `teacher` (`ID`)" + 
        		")" + 
        		"ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;";

        
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;        

               ps = this.conn.prepareStatement(querycreate);
               ps.executeUpdate();
               ps.close();
           }
           catch (SQLException e) {
               e.printStackTrace();
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
}
