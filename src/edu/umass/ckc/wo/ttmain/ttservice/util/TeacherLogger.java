package edu.umass.ckc.wo.ttmain.ttservice.util;


import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttservice.loggerservice.TTLoggerService;
import edu.umass.ckc.wo.ttmain.ttservice.loggerservice.impl.TTLoggerServiceImpl;
import edu.umass.ckc.wo.tutor.Settings;

import java.sql.*;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

//import org.springframework.jndi.JndiTemplate;

/**

 * Frank 	12-09-19	Issue #21 add teacher activity logger support
 * Frank 	12-21-19	Issue #21r2 add reworked database access setup
 * Frank 	01-14-20	Issue #45 & #21 add teacher logging by using the request object to get the TeacherLogger object
 * Frank 	06-17-20	Issue #149
 */
@Component
public class TeacherLogger {
    
    private static Logger logger = Logger.getLogger(TeacherLogger.class);

    @Autowired
    private TTLoggerService loggerService;
    
    public static final String[] TEACHER_EVENTS = { "Login", "Logout" };
    
    public int insertLoginEntry(int teacherId, int sessionId, String action, String activityName) throws SQLException { 	
        return logEntryWorker(teacherId, sessionId, action, activityName);
    }

    public int logEntryWorker(int teacherId, int sessionId, String action, String activityName) {

    	loggerService.tloggerAssist(teacherId, sessionId, action, activityName);
    	
    	return 0;
    	
    }
    public int logEntryWorker(int teacherId, int sessionId, String classId, String action, String activityName) {

    	loggerService.tloggerAssist(teacherId, sessionId, classId, action, activityName);
    	
    	return 0;
    	
    }

}
