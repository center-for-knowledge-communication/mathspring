package edu.umass.ckc.wo.ttmain.ttservice.miscservice.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.mysql.jdbc.ResultSetMetaData;
import com.sun.mail.smtp.SMTPTransport;

import edu.umass.ckc.wo.beans.StudentDetails;
import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassLandingReportStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;
import edu.umass.ckc.wo.ttmain.ttmodel.TeacherClassListEntry;
import edu.umass.ckc.wo.ttmain.ttmodel.TeacherListEntry;
import edu.umass.ckc.wo.ttmain.ttmodel.TeacherLogEntry;
import edu.umass.ckc.wo.ttmain.ttmodel.datamapper.TeacherLogEntryMapper;
import edu.umass.ckc.wo.ttmain.ttservice.miscservice.TTMiscService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TTUtil;
import edu.umass.ckc.wo.tutor.Settings;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by Frank 
 * Frank 	06-17-20	Issue #149
 */
@Service
public class TTMiscServiceImpl implements TTMiscService {
    private static Logger logger = Logger.getLogger(TTMiscServiceImpl.class);

	private ResourceBundle rb = null;
	private Locale ploc;
	
    @Autowired
    private TTConfiguration connection;
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public void test(String msg) {
    	System.out.println("test message is " + msg);
    	logger.debug("test message is " + msg);
    };

    @Override
    public String getCohortTeachersClasses(String cohortId, String teacherId, String classId, String reportType, String lang, String filter) throws TTCustomException {

        try {
        	
    		// Multi=lingual enhancement

        	Locale loc = new Locale("en","US");	
        	if (lang.substring(0,2).equals("es")) {
        		loc = new Locale("es","AR");	
        	}        	
    		ploc = loc;
    		rb = ResourceBundle.getBundle("MathSpring",loc);

    		String result = "";
    		switch (reportType) {
			case "getCohortTeachersClasses":
	       		result = getCohortTeachersClasses(cohortId, teacherId, classId, filter);
				break;
		
			case "getCohorts":
	       		result = getCohorts(reportType, lang, filter);
				break;
		    			
    		}
    		return result;

 		
    		
//       		return getCohortTeachersClasses(cohortId, teacherId, classId, filter);
/*
        		case "perStudentReport":
                	String logMsg = "";
                	Timestamp tsFromDate = null;
                	Timestamp tsToDate = null;
                	
                	String filters[] = filter.split("~");
                	String modFilter = "%";
                	if (filters.length > 0) {
                		modFilter = filters[0].trim() + "%";
                	}

                	if (filters.length > 1) {
                		if (filters[1].equals("")) {
                   			tsFromDate = defaultFromDate();
                   			tsToDate = defaultToDate();    		    			
                		}
                		else {
            	    		String dateFilters[] = filters[1].split("thru");
            	    		if (dateFilters[0].length() > 1) {
            	    			tsFromDate = convertFromDate(dateFilters[0].trim());
            	    			tsToDate = convertToDate(dateFilters[1].trim());    			
            	    		}
            	    		else {
            	    			tsFromDate = defaultFromDate();
            	    			tsToDate = defaultToDate();    		
            	    		}
            	    	}
                       	logMsg = "{ \"dates\" : \"" +  filters[1].trim() + "\" }";     

                	}
                	else {
               			tsFromDate = defaultFromDate();
               			tsToDate = defaultToDate();    		    			    		
                	}    	
*/
        		

        }
        catch (MissingResourceException e) {
        	logger.error(e.getMessage());
        	throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
         }
    }

    public String getCohortTeachersClasses(String cohort, String teacherId, String classId, String filter) throws SQLException {
    	ResultSet rs=null;
        PreparedStatement stmt=null;
        JSONArray classNameIdArray = new JSONArray();
    	try {
        	Connection conn = connection.getConnection();
            String q = "select tmc.researchcohortid as cohortId, coh.name as cohortName, t.ID as teacherId, t.username as username, fname as firstName ,lname as lastName, c.id as classId, c.name as className from teacher t, teacher_map_cohorts tmc, class c, research_cohort as coh where t.ID = tmc.teacherid and  tmc.researchcohortid = coh.researchcohortid and c.teacherId = t.ID order by tmc.researchcohortid,lname asc;";
            stmt = conn.prepareStatement(q);
            List<Teacher> result = new ArrayList<Teacher>();
            rs = stmt.executeQuery();
            ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
            int columns = md.getColumnCount();
            int column = 1;
            String prevCohort = "";
            String currCohort = "";
        	JSONArray cohortArr = new JSONArray();
            JSONObject cohortJson = new JSONObject();
        	JSONArray allCohortsArr = new JSONArray();
            while (rs.next()) {
            	JSONObject teacherJson = new JSONObject();            	
	            for (column=1;column<=columns;column++) {	            	
	            	String colName = md.getColumnName(column);
	            	String colType = md.getColumnTypeName(column);
	            	String colValue = "";
	            	if (colType.equals("INT")) {
	            		colValue = String.valueOf(rs.getInt(column));
	            		System.out.println(colName + " " + colType + " " +  String.valueOf(rs.getInt(column)));
	            	}
	            	else {
	            		colValue = rs.getString(column);
	        			System.out.println(colName + " " + colType + " " +  rs.getString(column));	 
	            	}
	            	if (colName.equals("researchcohortid")) {
            			currCohort = colValue;	            			
	            	}
            		teacherJson.put(colName, colValue);	            		
	            }
               	if (currCohort.equals(prevCohort)) {
       	            cohortArr.add(teacherJson); 
            	}
               	else {
               		if (prevCohort.length() > 0) {
	                    cohortJson.put("cohortId", prevCohort);
	                    cohortJson.put("cohortArr", cohortArr);
	                    allCohortsArr.add(cohortJson);
               		}
            		prevCohort = currCohort;
            		cohortArr = new JSONArray();
       	            cohortArr.add(teacherJson);       	            
               	}
            }
            // put final cohort
            cohortJson.put("cohortId", prevCohort);
            cohortJson.put("cohortArr", cohortArr);
            allCohortsArr.add(cohortJson);
            
            return allCohortsArr.toString();    	
        }
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return "";

/*
            while (rs.next()) {
            	String 
            
                int id= rs.getInt(1);
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String uname = rs.getString("username");
//                Teacher t = new Teacher(null,id,fname,lname,uname,null,0);
                JSONObject ctcJson = new JSONObject();
                String fname = rs.getString("fname");
                ctcJson.put("name", element.getName());
                nameIdJson.put("Id", element.getClassid());
                classNameIdArray.add(nameIdJson);                   
                if (includeClasses) {
                    List<ClassInfo> classes = DbClass.getTeacherClasses(conn,t.getId());
                    t.setClasses(classes);
                }
                result.add(t);
            }
*/
/*
            try {
	            if (classInfoListLatest.size() > 0) {
	            	classInfoListLatest.stream().forEach(element ->
	                {
	                    JSONObject nameIdJson = new JSONObject();
	                    nameIdJson.put("name", element.getName());
	                    nameIdJson.put("Id", element.getClassid());
	                    classNameIdArray.add(nameIdJson);                   
	                });
	            }
	            else {
                    JSONObject nameIdJson = new JSONObject();
                    nameIdJson.put("name", "dummyClass");
                    nameIdJson.put("Id", 0);
                    classNameIdArray.add(nameIdJson);                   	            	
	            }
	        } catch (JSONException e1) {
	                // TODO Auto-generated catch block
	              e1.printStackTrace();
	        }
            return result;
        }
*/        
    }

    
    public String getCohortTeachersClassIds(int cohortId) throws SQLException {
    	
    	ResultSet rs=null;
        PreparedStatement stmt=null;
        String teacherClasses = "";
        boolean first = true;
    	try {
        	Connection conn = connection.getConnection();
        	String q = "select distinct tch.ID as teacherId, tch.userName as uname, cls.ID as classId, coh.schoolYear as schoolYear from class as cls, teacher as tch,  teacher_map_cohorts as tmc, research_cohort as coh where tch.ID = tmc.teacherid and tmc.researchcohortid = ? and cls.teacherId = tch.ID and coh.researchcohortid = tmc.researchcohortid and coh.schoolYear = cls.schoolYear order by tch.ID;";

        	//String q = "select distinct tch.ID as teacherId, tch.userName as uname, cls.ID as classId from class as cls, teacher as tch,  teacher_map_cohorts where tch.ID = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and cls.teacherId = tch.ID order by tch.ID;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	if (!first) {
            		teacherClasses += ",";
            	}
            	else {
            		first = false;
            	}
            	String line = String.valueOf(rs.getInt("teacherId")) + "~" + String.valueOf(rs.getString("uname")) + "~" + String.valueOf(rs.getInt("classId")) + "~" + String.valueOf(rs.getInt("schoolYear"));
            	teacherClasses += line;
    		}
            return teacherClasses;    	
        }
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
        finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return "";
    }    
    
    public String getCohorts(String reportType,  String lang, String filter) {
    	ResultSet rs=null;
        PreparedStatement stmt=null;
    	try {
        	Connection conn = connection.getConnection();
        	String q = "select distinct tmc.researchcohortid as cohortId, coh.name as cohortName, coh.startdate as startdate, coh.enddate as enddate from teacher_map_cohorts tmc INNER JOIN research_cohort as coh ON tmc.researchcohortid=coh.researchcohortid  order by tmc.researchcohortid asc;";
        	stmt = conn.prepareStatement(q);
            rs = stmt.executeQuery();
        	JSONArray cohortsArr = new JSONArray();
            JSONObject cohortJson = new JSONObject();
            while (rs.next()) {
                cohortJson.put("cohortId", rs.getInt("cohortId"));
                cohortJson.put("cohortName", rs.getString("cohortName"));
                cohortJson.put("cohortStartdate", rs.getString("startdate"));
                cohortJson.put("cohortEnddate", rs.getString("enddate"));
   	            cohortsArr.add(cohortJson);       	            
            }            
            stmt.close();
            rs.close();
            return cohortsArr.toString();    	
        }
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
        return "";

    }

    public String getTeacherLogins(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int count = 0;
        String username = "";
        String ticks = "";
        try {
            String q = "SELECT teacherlog.teacherId, teacher.lname as lname, userName as uname, COUNT(*) AS logins FROM teacher, teacherlog, teacher_map_cohorts where teacherlog.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and teacher.id = teacherlog.teacherId and action = 'login' GROUP BY teacherId;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            rs = stmt.executeQuery();

        	JSONArray resultArr = new JSONArray();
            JSONObject resultJson = new JSONObject();
            while (rs.next()) {
            	resultJson.put("lname", rs.getString("lname"));
            	resultJson.put("username", rs.getString("uname"));
            	resultJson.put("logins", rs.getInt("logins"));
            	resultArr.add(resultJson);       	            
            }            
            stmt.close();
            rs.close();
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }
           
    public String getTeacherDaysSinceLogin(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int count = 0;
        String username = "";
        String prevUsername = "";
        String ticks = "";
        try {
//            String q = "select tch.lname as lname, tch.userName as uname, tlog.time as sinceDate from teacherlog as tlog, teacher as tch, teacher_map_cohorts as tmc where tlog.teacherId = tmc.teacherid and tmc.researchcohortid = ? and tch.id = tlog.teacherId Group BY tch.Id"; 
            String q = "select tch.lname as lname, tch.userName as uname, tch.id, tlog.time as sinceDate from teacherlog as tlog, teacher as tch, teacher_map_cohorts as tmc where tlog.teacherId = tmc.teacherid and tmc.researchcohortid = ? and tch.id = tlog.teacherId order by tch.Id, tlog.time DESC;"; 
            		
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            rs = stmt.executeQuery();
            //System.out.println(rs.getString(1));
            //System.out.println(rs.getString(2));
            //System.out.println((rs.getTimestamp(3)).toString());

        	JSONArray resultArr = new JSONArray();
            while (rs.next()) {           
	        	if (!prevUsername.equals(rs.getString("uname"))) {
	                JSONObject resultJson = new JSONObject();
	            	resultJson.put("username", rs.getString("uname"));
	            	resultJson.put("lname", rs.getString("lname"));
                	Timestamp ts = rs.getTimestamp("sinceDate");
                	long dt =  ts.getTime();
                	Date da = new Date(dt);
                	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                	String fd = formatter.format(da);
                	resultJson.put("since", (String) fd);
	            	resultArr.add(resultJson);       	                   		
	        		prevUsername = rs.getString("uname");
	        	}
            }            

            stmt.close();
            rs.close();
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }
    
    
    public String getTeacherActivityMetrics(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String prevUsername = "";
        int currentActionTotal = 0;
        try {
            String q = "SELECT teacherlog.teacherId, userName as uname, action, count(*) as total FROM teacher, teacherlog, teacher_map_cohorts where teacherlog.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and teacher.id = teacherlog.teacherId GROUP BY teacherId,action;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();
            JSONObject resultJson = new JSONObject();
    		resultJson.put("logins", 0);
			resultJson.put("actions", 0);
    		resultJson.put("logouts", 0);

            while (rs.next()) {
            	if (!prevUsername.equals(rs.getString("uname"))) {
            		if (!prevUsername.equals("")) {
            			resultJson.put("actions", currentActionTotal);
                    	resultArr.add(resultJson);
                    	currentActionTotal = 0;
                    	resultJson = new JSONObject();
                		resultJson.put("logins", 0);
            			resultJson.put("actions", 0);
                		resultJson.put("logouts", 0);
            		}
            		resultJson.put("username", rs.getString("uname"));
            		prevUsername = rs.getString("uname");
            	}
            	switch (rs.getString("action")) {
            	case "login":
            		resultJson.put("logins", rs.getInt("total"));
            		break;
            	case "logout":
            		resultJson.put("logouts", rs.getInt("total"));
            		break;
            	default:
            		currentActionTotal += rs.getInt("total");
            		resultJson.put("actions", rs.getInt("total"));
            	}
            }            
            // finish up last set
            resultJson.put("actions", currentActionTotal);
        	resultArr.add(resultJson);
            stmt.close();
            rs.close();
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }

    
    public String getTeacherClassProblems(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int currentActionTotal = 0;
        try {
            String q = "SELECT tcs.teacherId AS teacherId, tcs.teacherUsername as uname, tcs.classId, sum(tcs.nbr_problems_seen) as sumProblemSeen, sum(tcs.nbr_problems_solved) as sumProblemsSolved FROM teacher_class_slices as tcs, teacher_map_cohorts where tcs.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? GROUP BY teacherId,classId";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("teacherId", rs.getString("teacherId"));
        		resultJson.put("classId", rs.getString("classId"));
        		resultJson.put("username", rs.getString("uname"));
           		resultJson.put("nbr_problems_seen", rs.getInt("sumProblemSeen"));
       			resultJson.put("nbr_problems_solved", rs.getInt("sumProblemsSolved"));
            	resultArr.add(resultJson);
            }            
            stmt.close();
            rs.close();
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public String getTeacherActiveStudentCount(Connection conn, int cohortId, String filter) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        
        int week_nbr = Integer.valueOf(filter);
        try {
        	
        	String q = "SELECT tcs.teacherId AS teacherId, tcs.teacherUsername as uname, tcs.classId, sum(tcs.nbr_active_students) as sumActiveStudents FROM teacher_class_slices as tcs, teacher_map_cohorts where tcs.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and tcs.week_nbr = ? GROUP BY teacherId,classId";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, week_nbr);
            
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("teacherId", rs.getString("teacherId"));
        		resultJson.put("classId", rs.getString("classId"));
        		resultJson.put("username", rs.getString("uname"));
           		resultJson.put("nbr_active_students", rs.getInt("sumActiveStudents"));
            	resultArr.add(resultJson);
            }            
            stmt.close();
            rs.close();
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public String getClassActiveStudentList(Connection conn, int classId, Timestamp tsFromDate, Timestamp tsToDate) throws SQLException {
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "Select distinct s.id as id, s.fname as fname, s.lname as lname, s.username as uname from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=? and sh.mode != 'demo' and sh.problemBeginTime BETWEEN ? AND ?;";

            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            stmt.setTimestamp(2, tsFromDate);
            stmt.setTimestamp(3, tsToDate);
            rs = stmt.executeQuery();

        	JSONArray resultArr = new JSONArray();            
            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("studentId", rs.getInt("id"));
        		resultJson.put("fname", rs.getString("fname"));
        		resultJson.put("lname", rs.getString("lname"));
        		resultJson.put("username", rs.getString("uname"));
            	resultArr.add(resultJson);
            }
            stmt.close();
            rs.close();
            return resultArr.toString(); 
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }

    
    
    @Override
    public List<TeacherLogEntry> generateTeacherLogReport(String filter) {
    	// Note: Target teacherID is passed from requester in the ClassId parameter. 
        SqlParameterSource namedParameters = new MapSqlParameterSource("targetId", filter);
        List<TeacherLogEntry> teacherLogEntries = (List) namedParameterJdbcTemplate.query(TTUtil.TEACHER_LOG_QUERY_FIRST, namedParameters, new TeacherLogEntryMapper());
        return teacherLogEntries;
    }
 
    
    
    public String getCohortReport(String cohortId, String reportType, String lang, String filter) throws TTCustomException {
    	
    	String result = "theReport";
    	try {
    		Connection conn = connection.getConnection();
    		switch (reportType) {
    		case "teacherLoginCount":
    			result = getTeacherLogins(conn, Integer.valueOf(cohortId));
    			break;
    		case "teacherActivityMetrics":
				result = getTeacherActivityMetrics(conn, Integer.valueOf(cohortId));  
				break;
			case "teacherDaysSinceLogin":
	       		result = getTeacherDaysSinceLogin(conn, Integer.valueOf(cohortId));
				break;
    		case "teacherClassProblems":
				result = getTeacherClassProblems(conn, Integer.valueOf(cohortId));  
				break;
    		case "teacherClassActiveStudentCount":
				result = getTeacherActiveStudentCount(conn, Integer.valueOf(cohortId), filter);  
				break;
            case "perTeacherReport":
            	String classId = filter;
            	String filters[] = filter.split("~");
            	// Note: classId parameter is used to communicate target teacherId for this report only
                List<TeacherLogEntry> TeacherLogEntries = generateTeacherLogReport(filters[0]);
                String[][] teacherData = TeacherLogEntries.stream().map(TeacherLogEntry1 -> new String[]{TeacherLogEntry1.getTimestampString(lang.substring(0,2)),TeacherLogEntry1.getTeacherId(), TeacherLogEntry1.getTeacherName(), TeacherLogEntry1.getUserName(), TeacherLogEntry1.getAction(), TeacherLogEntry1.getClassId(), TeacherLogEntry1.getActivityName(reportType)}).toArray(String[][]::new);
                ObjectMapper teacherMapper = new ObjectMapper();
                Map<String, Object> teacherMap = new HashMap<>();
                teacherMap.put("levelOneData", teacherData);
                return teacherMapper.writeValueAsString(teacherMap);
		
    		default:
    			return result;
    		}
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	
    	return result;
    };

    
    public String cohortAdmin(String cohortId, String command, String lang, String filter) throws TTCustomException {
    	
    	String result = "theCommand";
    	try {
    		Connection conn = connection.getConnection();
    		switch (command) {
    		case "getCohortWeeks":
    			result = getCohortWeeks(conn, Integer.valueOf(cohortId),filter);
    			break;
    		case "createCohortSlice":
    			result = createCohortSlice(conn, Integer.valueOf(cohortId),filter);
    			break;
    		case "updateCohortSliceTeacherActivity":
    			result = updateCohortSliceTeacherActivity(conn, Integer.valueOf(cohortId),filter);
    			break;
    		case "updateAllCohortSlicesTeacherActivity":
    			result = updateAllCohortSlicesTeacherActivity(conn, Integer.valueOf(cohortId), filter);
    			break;
    		case "updateAllCohortClassStudentSlices":
    			result = updateAllCohortClassStudentSlices(conn, Integer.valueOf(cohortId), filter);
    			break;
       		case "getCohortSlice":
    			result = getCohortSlice(conn, Integer.valueOf(cohortId),filter);
    			break;
       		case "getCohortRangeActivitySlices":
    			result = getCohortRangeActivitySlices(conn, Integer.valueOf(cohortId),filter);
    			break;
       		case "getCohortRangeTeacherClassSlices":
    			result = getCohortRangeTeacherClassSlices(conn, Integer.valueOf(cohortId),filter);
    			break;
    		default:
    			System.out.println("unrecognized command:" + command);
    			return "unrecognized command:" + command;
    		}
    	}
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	
    	return result;
    };

    public String getCohortWeeks(Connection conn, int cohortId, String filter) throws SQLException {

   	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        JSONObject resultJson = new JSONObject();            

        try {
            String q_start = "SELECT startdate, YEAR(startdate) year_no, WEEK(startdate) week_no FROM research_cohort where research_cohort.researchcohortid = ?";
            stmt = conn.prepareStatement(q_start);
            stmt.setInt(1, cohortId);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	resultJson.put("startyear", (int) rs.getInt("year_no"));
            	resultJson.put("startweek", (int) rs.getInt("week_no"));
            	Timestamp ts = rs.getTimestamp("startdate");
            	long dt =  ts.getTime();
            	Date da = new Date(dt);
            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            	String fd = formatter.format(da);
            	resultJson.put("startdate", (String) fd);
            	break;
            }            
            stmt.close();            
            rs.close();

            String q_latest = "SELECT YEAR(teacherlog.time) year_no, WEEK(teacherlog.time) week_no, time as latestdate  FROM teacher, teacherlog, teacher_map_cohorts where teacherlog.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and teacher.id = teacherlog.teacherId ORDER BY time desc;";
            stmt = conn.prepareStatement(q_latest);
            stmt.setInt(1, cohortId);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	resultJson.put("latestyear", (int) rs.getInt("year_no"));
            	resultJson.put("latestweek", (int) rs.getInt("week_no"));
            	break;
            }           
            
            stmt.close();            
            rs.close();
            
            return resultJson.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }
    
    
    public String createCohortSlice(Connection conn, int cohortId, String filter) throws SQLException {
/*
    	String[] splitter = filter.split("~");
    	
    	int year_no = Integer.valueOf(splitter[0]);
    	int week_no = Integer.valueOf(splitter[1]);
 
    	
    	String startDate = splitter[0];
    	int intDuration = Integer.valueOf(splitter[1]);
 
    	
        Timestamp tsFromDate = convertStartDate(startDate);
        Timestamp endDateTimestamp = xDaysFromStartDate(tsFromDate,intDuration);
    	
     	
        int prevTeacherId = 0;

    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q1 = "select t.ID as teacherId, t.userName as uname, tmc.researchcohortid as cohortId,  c.id as classId, YEAR(?) as year_no, WEEK(?) as week_no  from teacher t, teacher_map_cohorts tmc, class c where tmc.researchcohortId = ? and t.ID = tmc.teacherid and c.teacherId = t.ID order by tmc.researchcohortid,lname asc;";

            stmt = conn.prepareStatement(q1);
            stmt.setInt(1, year_no);
            stmt.setInt(2, week_no);
            stmt.setInt(3, cohortId);
            rs = stmt.executeQuery();
            while (rs.next()) {
	        	if (prevTeacherId != rs.getInt("teacherId")) {
                	insertCohortSlice(conn,rs.getInt("cohortId"),rs.getInt("teacherId"),rs.getString("uname"),0, tsFromDate);
	        	}
        		insertCohortSlice(conn,rs.getInt("cohortId"),rs.getInt("teacherId"),rs.getString("uname"),rs.getInt("classId"), tsFromDate);
            	prevTeacherId = rs.getInt("teacherId");
            }
        	stmt.close();
            rs.close();
            return "Success";  
        }
        catch (Exception e) {
            	System.out.println(e.getMessage());
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
*/
        return "Deprecated";

    }

    public String insertCohortSlice(Connection conn, int cohortId, int teacherId, String uname, int classId, Timestamp week_startdate, int week_nbr) throws SQLException {
    	
    	int result = 0;
    	String resultMessage = "Success";
        PreparedStatement stmt = null;
        try {
            String q = "INSERT into teacher_log_slices (cohortId,teacherId,teacherUsername,classId, week_startdate, week_nbr) VALUES (?,?,?,?,?,?);";

            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, teacherId);
            stmt.setString(3,uname);
            stmt.setInt(4, classId);
            stmt.setTimestamp(5, week_startdate);
            stmt.setInt(6, week_nbr);
            
            result = stmt.executeUpdate();
            
            if (result == 0) {
            	return "SQL Insert error"; 
            }
            	
        	stmt.close();

        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        } finally {
            if (stmt != null)
                stmt.close();
        }    
        return resultMessage;
    }


    
    public String updateCohortSliceActivity(Connection conn, int cohortId, int teacherId, String teacherUsername, int classId, Timestamp week_startdate, int week_nbr, int logins, int actions, int logouts) throws SQLException {
    	
    	int result = 0;
        String q = "";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
        	if (classId == 0) {
        		q = "UPDATE teacher_log_slices SET logins = ?,actions = ?,logouts = ? where cohortId = ? and teacherId = ? and teacherUsername = ? and classId = ? and week_startdate = ? and week_nbr = ?;";
	            stmt = conn.prepareStatement(q);
	            stmt.setInt(1, logins);
	            stmt.setInt(2, actions);
	            stmt.setInt(3, logouts);
	            stmt.setInt(4, cohortId);
	            stmt.setInt(5, teacherId);
	            stmt.setString(6, teacherUsername);
	            stmt.setInt(7, classId);
	            stmt.setTimestamp(8, week_startdate);
	            stmt.setInt(9, week_nbr);
	            
	            result = stmt.executeUpdate();
        	}

            if (result == 0) {
                String q2 = "INSERT into teacher_log_slices (cohortId,teacherId,teacherusername, classId, week_startdate, logins, actions, logouts, week_nbr) VALUES (?,?,?,?,?,?,?,?,?);";
                stmt2 = conn.prepareStatement(q2);
                stmt2.setInt(1, cohortId);
                stmt2.setInt(2, teacherId);
                stmt2.setString(3, teacherUsername);
                stmt2.setInt(4, classId);
                stmt2.setTimestamp(5, week_startdate);
                stmt2.setInt(6, logins);
                stmt2.setInt(7, actions);
                stmt2.setInt(8, logouts);
                stmt2.setInt(9, week_nbr);
                
                result = stmt2.executeUpdate();

            	stmt2.close();
            	
            }

        	stmt.close();

            return "Success"; 
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        } finally {
            if (stmt != null)
                stmt.close();
        }    
        return "Success";
    }
    
    
    public String updateAllCohortSlicesTeacherActivity(Connection conn, int cohortId, String filter) throws SQLException {
    	

    	String[] splitter = filter.split("~");
    	
    	String startDate = splitter[0];
    	int intDuration = Integer.valueOf(splitter[1]);
    	int weeksToReport = Integer.valueOf(splitter[2]);

    	Timestamp tsFromDate = convertStartDate(startDate);
        Timestamp tsToDate = xDaysFromStartDate(tsFromDate,intDuration);
    	    	
    	for (int week_nbr = 1;week_nbr<=weeksToReport;week_nbr++) {
            int currentActionTotal = 0;

            int prevTeacherId = 0;
            int teacherId = 0;
            String teacherUsername = "";
            int classId = 0;
            
            int totalLogins;
            int totalActions;
            int totalLogouts;
            
            boolean found = false;
            
        	ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                String q = "SELECT teacherlog.teacherId as teacherId, teacher.UserName as teacherUsername, teacherlog.action as action, count(*) as total  FROM teacher, teacher_map_cohorts, teacherlog where teacherlog.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and teacher.id = teacherlog.teacherId and teacherlog.time BETWEEN ? AND ? GROUP BY teacherId,action;";
//                String q = "SELECT YEAR(teacherlog.time) year_no, WEEK(teacherlog.time) week_no, time, teacherlog.teacherId as teacherId, userName as teachername, COUNT(*) AS logins FROM teacher, teacherlog, teacher_map_cohorts where teacherlog.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and teacher.id = teacherlog.teacherId and action = 'login' and YEAR(teacherlog.time) = ? and WEEK(teacherlog.time) = ? GROUP BY year_no, week_no, teacherId;";

    //and e.time BETWEEN (:tsFromDate) AND (:tsToDate)

                stmt = conn.prepareStatement(q);
                stmt.setInt(1, cohortId);
                stmt.setTimestamp(2, tsFromDate);
                stmt.setTimestamp(3, tsToDate);
                rs = stmt.executeQuery();

                totalLogins = 0;
                totalActions = 0;
                totalLogouts = 0;
                found = false;
                while (rs.next()) {
                	found = true;
    	        	if (prevTeacherId != rs.getInt("teacherId")) {
    	        		if (prevTeacherId > 0) {
    	        			totalActions = currentActionTotal;
    	        			updateCohortSliceActivity(conn, cohortId, teacherId, teacherUsername, classId, tsFromDate, week_nbr, totalLogins, totalActions, totalLogouts);
    	                	currentActionTotal = 0;
    	                    totalLogins = 0;
    	                    totalActions = 0;;
    	                    totalLogouts = 0;;
    	        		}
            			teacherUsername = rs.getString("teacherUsername");
    	            	teacherId = rs.getInt("teacherId");
    	            	prevTeacherId = rs.getInt("teacherId");
    	        	}
    	        	switch (rs.getString("action")) {
    	        	case "login":
    	        		totalLogins = rs.getInt("total");
    	        		break;
    	        	case "logout":
    	        		totalLogouts = rs.getInt("total");
    	        		break;
    	        	default:
    	        		currentActionTotal += rs.getInt("total");
    	        	}
                }
                // finish up last set
                if (found) {
                	totalActions =  currentActionTotal;
                	updateCohortSliceActivity(conn, cohortId, teacherId, teacherUsername, classId, tsFromDate, week_nbr, totalLogins, totalActions, totalLogouts);
                }
                stmt.close();
                rs.close();
            } finally {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
            }
    		
    		
    		
	        tsFromDate=xDaysFromStartDate(tsFromDate,intDuration);    	
	        tsToDate=xDaysFromStartDate(tsFromDate,intDuration);    	
    	}

        return "Teacher Activity Slices Updated Successfully"; 
    }
    
    public int getClassProblemsSeen(Connection conn, int classId, Timestamp tsFromDate, Timestamp tsToDate) throws SQLException {
    	int result = 0;
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "Select sh.problemBeginTime, count(problemId) AS noOfProblemsSeen from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=? and sh.mode != 'demo' and sh.problemBeginTime BETWEEN ? AND ? ;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            stmt.setTimestamp(2, tsFromDate);
            stmt.setTimestamp(3, tsToDate);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	result = rs.getInt("noOfProblemsSeen");
            }
            stmt.close();
            rs.close();
            return result;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }

    public int getClassProblemsSolved(Connection conn, int classId, Timestamp tsFromDate, Timestamp tsToDate) throws SQLException {
    	int result = 0;
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "Select sh.problemBeginTime, count(problemId) AS noOfProblemsSolved from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=? and sh.isSolved = 1 and sh.mode != 'demo' and sh.problemBeginTime BETWEEN ? AND ? ;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            stmt.setTimestamp(2, tsFromDate);
            stmt.setTimestamp(3, tsToDate);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	result = rs.getInt("noOfProblemsSolved");
            }
            stmt.close();
            rs.close();
            return result;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }


    public int getClassActiveStudentCount(Connection conn, int classId, Timestamp tsFromDate, Timestamp tsToDate) throws SQLException {
    	int result = 0;
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "Select count(distinct s.id) AS noOfActiveStudents from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=? and sh.mode != 'demo' and sh.problemBeginTime BETWEEN ? AND ? ;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            stmt.setTimestamp(2, tsFromDate);
            stmt.setTimestamp(3, tsToDate);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	result = rs.getInt("noOfActiveStudents");
            }
            stmt.close();
            rs.close();
            return result;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }


    public String insertCohorClassStudentSlice(Connection conn, int cohortId, int teacherId, String uname, int classId, int schoolYear, Timestamp week_startdate, int week_nbr, int nbr_problems_solved, int nbr_problems_seen, int nbr_active_students) throws SQLException {
    	
    	int result = 0;
    	String resultMessage = "Success";
        PreparedStatement stmt = null;
        try {
            String q = "INSERT into teacher_class_slices (cohortId,teacherId,teacherUsername,classId, schoolYear, week_startdate, week_nbr, nbr_problems_solved,nbr_problems_seen,nbr_active_students) VALUES (?,?,?,?,?,?,?,?,?,?);";

            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, teacherId);
            stmt.setString(3,uname);
            stmt.setInt(4, classId);
            stmt.setInt(5, schoolYear);
            stmt.setTimestamp(6, week_startdate);
            stmt.setInt(7, week_nbr);
            stmt.setInt(8, nbr_problems_solved);
            stmt.setInt(9, nbr_problems_seen);
            stmt.setInt(10, nbr_active_students);
            
            result = stmt.executeUpdate();
            
            if (result == 0) {
            	return "SQL Insert error"; 
            }
            	
        	stmt.close();

        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        } finally {
            if (stmt != null)
                stmt.close();
        }    
        return resultMessage;
    }
    
    
    public String updateAllCohortClassStudentSlices(Connection conn, int cohortId, String filter) throws SQLException {
    	

    	String[] splitter = filter.split("~");
    	
    	String startDate = splitter[0];
    	int intDuration = Integer.valueOf(splitter[1]);
    	int weeksToReport = Integer.valueOf(splitter[2]);

        String teacherClasses = getCohortTeachersClassIds(cohortId);
        
        String[] teacherClassSplitter  = teacherClasses.split(",");
        
        for (int tc=0;tc<teacherClassSplitter.length;tc++) {
        	
        	String teacherClassSpilitter = teacherClassSplitter[tc];
        	String[] tcSplitter = teacherClassSpilitter.split("~");
        	int teacherId = Integer.valueOf(tcSplitter[0]);
        	String teacherUsername = tcSplitter[1];
        	int classId = Integer.valueOf(tcSplitter[2]);
        	int schoolYear = Integer.valueOf(tcSplitter[3]);

        	Timestamp tsFromDate = convertStartDate(startDate);
            Timestamp tsToDate = xDaysFromStartDate(tsFromDate,intDuration);
            
        	
	    	for (int week_nbr = 1;week_nbr<=weeksToReport;week_nbr++) {
               	int nbr_problems_seen = getClassProblemsSeen(conn, classId, tsFromDate,tsToDate);
               	int nbr_problems_solved = getClassProblemsSolved(conn, classId, tsFromDate,tsToDate);
               	int nbr_active_students = getClassActiveStudentCount(conn, classId, tsFromDate,tsToDate);
//               	if ((nbr_problems_seen + nbr_problems_solved) > 0) {
               		insertCohorClassStudentSlice(conn, cohortId, teacherId, teacherUsername, classId, schoolYear, tsFromDate, week_nbr, nbr_problems_solved,nbr_problems_seen,nbr_active_students);
//               	}
	            tsFromDate=xDaysFromStartDate(tsFromDate,intDuration);    	
		        tsToDate=xDaysFromStartDate(tsFromDate,intDuration);    	

	    	}
    	}

        return "Class Slices Updated Successfully"; 
    }
    
    
    
    public String updateCohortSliceTeacherActivity(Connection conn, int cohortId, String filter) throws SQLException {

/*
    	
    	String[] splitter = filter.split("~");
    	
    	String startDate = splitter[0];
    	int intDuration = Integer.valueOf(splitter[1]);
 
    	
        Timestamp tsFromDate = convertStartDate(startDate);
        Timestamp tsToDate = xDaysFromStartDate(tsFromDate,intDuration);
    	
        int currentActionTotal = 0;

        int prevTeacherId = 0;
        int teacherId = 0;
        String teacherUsername = "";
        int classId = 0;
        
        int totalLogins;
        int totalActions;
        int totalLogouts;
        
        boolean found = false;
        
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "SELECT teacherlog.teacherId as teacherId, teacher.UserName as teacherUsername, teacherlog.action as action, count(*) as total  FROM teacher, teacher_map_cohorts, teacherlog where teacherlog.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and teacher.id = teacherlog.teacherId and teacherlog.time BETWEEN ? AND ? GROUP BY teacherId,action;";
//            String q = "SELECT YEAR(teacherlog.time) year_no, WEEK(teacherlog.time) week_no, time, teacherlog.teacherId as teacherId, userName as teachername, COUNT(*) AS logins FROM teacher, teacherlog, teacher_map_cohorts where teacherlog.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and teacher.id = teacherlog.teacherId and action = 'login' and YEAR(teacherlog.time) = ? and WEEK(teacherlog.time) = ? GROUP BY year_no, week_no, teacherId;";

//and e.time BETWEEN (:tsFromDate) AND (:tsToDate)

            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setTimestamp(2, tsFromDate);
            stmt.setTimestamp(3, tsToDate);
            rs = stmt.executeQuery();

            totalLogins = 0;
            totalActions = 0;
            totalLogouts = 0;
            found = false;
            while (rs.next()) {
            	found = true;
	        	if (prevTeacherId != rs.getInt("teacherId")) {
	        		if (prevTeacherId > 0) {
	        			totalActions = currentActionTotal;
	        			updateCohortSlice(conn, cohortId, teacherId, teacherUsername, classId, tsFromDate, totalLogins, totalActions, totalLogouts);
	                	currentActionTotal = 0;
	                    totalLogins = 0;
	                    totalActions = 0;;
	                    totalLogouts = 0;;
	        		}
        			teacherUsername = rs.getString("teacherUsername");
	            	teacherId = rs.getInt("teacherId");
	            	prevTeacherId = rs.getInt("teacherId");
	        	}
	        	switch (rs.getString("action")) {
	        	case "login":
	        		totalLogins = rs.getInt("total");
	        		break;
	        	case "logout":
	        		totalLogouts = rs.getInt("total");
	        		break;
	        	default:
	        		currentActionTotal += rs.getInt("total");
	        	}
            }
            // finish up last set
            if (found) {
            	totalActions =  currentActionTotal;
            	updateCohortSlice(conn, cohortId, teacherId, teacherUsername, classId, tsFromDate, totalLogins, totalActions, totalLogouts);
            }
            stmt.close();
            rs.close();
            return "Success";    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
*/
    	 return "Deprecated"; 
    }


    
    
    
    
    
    public String getCohortSlice(Connection conn, int cohortId, String filter) throws SQLException {

    	String[] splitter = filter.split("~");
    	
    	int year = Integer.valueOf(splitter[0]);
    	int week = Integer.valueOf(splitter[1]);
 
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            //String q = "SELECT YEAR(?) year_no, WEEK(?) week_no, tls.teacherId as teacherId, userName as uname, tls.logins as logins, tls.actions as actions, tls.logouts as logouts, time FROM teacher, teacherlog, teacher_map_cohorts where tls.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and YEAR(teacherlog.time) = ? and WEEK(teacherlog.time) = ? GROUP BY year_no, week_no, teacherId;";
            String q = "SELECT tls.year_no as year_no, tls.week_no as week_no,tls.teacherId as teacherId, tls.teacherUsername as uname, tls.logins as logins, tls.actions as actions, tls.logouts as logouts FROM teacher_log_slices as tls, teacher, teacherlog, teacher_map_cohorts where tls.teacherId = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and tls.year_no = ? and tls.week_no = ? GROUP BY year_no, week_no, teacherId;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, year);
            stmt.setInt(3, week);
            rs = stmt.executeQuery();

        	JSONArray resultArr = new JSONArray();
            while (rs.next()) {
                JSONObject resultJson = new JSONObject();
            	resultJson = new JSONObject();
               	resultJson.put("year_no", year);
               	resultJson.put("week_no", week);
            	resultJson.put("teacherId", rs.getString("teacherId"));
            	resultJson.put("username", rs.getString("uname"));
        		resultJson.put("logins", rs.getInt("logins"));
    			resultJson.put("actions", rs.getInt("actions"));
        		resultJson.put("logouts", rs.getInt("logouts"));
            	resultArr.add(resultJson);
        	}
        	stmt.close();
            rs.close();
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }
    
    public String getCohortRangeActivitySlices(Connection conn, int cohortId, String filter) throws SQLException {

    	String teacherId = "";
    	String populate = "Show All";
    	
    	String[] splitter = filter.split("~");
    	
    	int startWeek = Integer.valueOf(splitter[0]);
    	int intDuration = Integer.valueOf(splitter[1]);
    	int weeksToReport = Integer.valueOf(splitter[2]);
    	if (splitter.length > 3) {
    		teacherId = (String) splitter[3];
    	}
    	if (splitter.length > 4) {
    		populate = (String) splitter[4];
    	}
    	
    	int loopCounter = 1;
    	
    	JSONArray rangeArr = new JSONArray();

//    	for (int week_nbr = startWeek;week_nbr<=(startWeek+weeksToReport);week_nbr++) {
//       	for (int week_nbr = weeksToReport;week_nbr>0;week_nbr--) {
    	
       	for (int week_nbr = startWeek; week_nbr < startWeek+weeksToReport;week_nbr++) {
    		    	
	    	ResultSet rs = null;
	        PreparedStatement stmt = null;

        	JSONArray weekArr = new JSONArray();
        	JSONObject weekJson = new JSONObject();
	        try {
	            String q = "SELECT tls.cohortId as cohortid, tls.week_nbr as week_nbr, tls.teacherId as teacherId, tls.teacherUsername as uname, tls.logins as logins, tls.week_startdate as week_date, tls.actions as actions, tls.logouts as logouts  FROM teacher_log_slices as tls where cohortId = ? and tls.week_nbr = ? order by week_nbr, tls.teacherId;";

	            stmt = conn.prepareStatement(q);
	            stmt.setInt(1, cohortId);
	            stmt.setInt(2, week_nbr);
	            rs = stmt.executeQuery();
	
	            while (rs.next()) {
	            	if ((teacherId.length() == 0) 
	            		|| (populate.equals("showAll"))
	            		|| (populate.equals("showSingleWithAnonymous"))
	            		|| (teacherId.equals(rs.getString("teacherId")) && (populate.equals("showSingleOnly")))
	            		) {
		                JSONObject resultJson = new JSONObject();
		            	resultJson = new JSONObject();
		            	resultJson.put("teacherId", rs.getString("teacherId"));
		            	resultJson.put("username", rs.getString("uname"));
	                	Timestamp ts = rs.getTimestamp("week_date");
	                	long dt =  ts.getTime();
	                	Date da = new Date(dt);
	                	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	                	String fd = formatter.format(da);
	                	resultJson.put("week_date", (String) fd);		            	
		            	resultJson.put("logins", rs.getInt("logins"));
		    			resultJson.put("actions", rs.getInt("actions"));
		        		resultJson.put("logouts", rs.getInt("logouts"));
		            	weekArr.add(resultJson);;
	            	}
	        	}
	        	stmt.close();
	            rs.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
    		weekJson.put("Week" + String.valueOf(loopCounter++), weekArr);
	        rangeArr.add(weekJson);
	    }
        return rangeArr.toString();    	

    }

    public String getCohortRangeTeacherClassSlices(Connection conn, int cohortId, String filter) throws SQLException {

    	String teacherId = "";
    	String populate = "Show All";
    	
    	String[] splitter = filter.split("~");
    	
    	int startWeek = Integer.valueOf(splitter[0]);
    	int intDuration = Integer.valueOf(splitter[1]);
    	int weeksToReport = Integer.valueOf(splitter[2]);
    	if (splitter.length > 3) {
    		teacherId = (String) splitter[3];
    	}
    	if (splitter.length > 4) {
    		populate = (String) splitter[4];
    	}

    	int loopCounter = 1;
    	
    	JSONArray rangeArr = new JSONArray();

//    	for (int week_nbr = startWeek;week_nbr<=(startWeek+weeksToReport);week_nbr++) {
//       	for (int week_nbr = weeksToReport;week_nbr>0;week_nbr--) {
    	
       	for (int week_nbr = startWeek; week_nbr < startWeek+weeksToReport;week_nbr++) {
    		    	
	    	ResultSet rs = null;
	        PreparedStatement stmt = null;

        	JSONArray weekArr = new JSONArray();
        	JSONObject weekJson = new JSONObject();
	        try {
	            String q = "SELECT tlc.cohortId as cohortid, tlc.week_nbr as week_nbr, tlc.teacherId as teacherId, tlc.teacherUsername as uname, tlc.nbr_problems_seen as problems_seen, tlc.nbr_problems_solved as problems_solved, tlc.week_startdate as week_date  FROM teacher_class_slices as tlc where cohortId = ? and tlc.week_nbr = ? order by week_nbr, tlc.teacherId;";

	            stmt = conn.prepareStatement(q);
	            stmt.setInt(1, cohortId);
	            stmt.setInt(2, week_nbr);
	            rs = stmt.executeQuery();
	
	            while (rs.next()) {
		            if ((teacherId.length() == 0) 
		            		|| (populate.equals("showAll"))
		            		|| (populate.equals("showSingleWithAnonymous"))
		            		|| (teacherId.equals(rs.getString("teacherId")) && (populate.equals("showSingleOnly")))
		            		) {	                
		            	JSONObject resultJson = new JSONObject();
		            	resultJson = new JSONObject();
		            	resultJson.put("teacherId", rs.getString("teacherId"));
		            	resultJson.put("username", rs.getString("uname"));
	
	                	Timestamp ts = rs.getTimestamp("week_date");
	                	long dt =  ts.getTime();
	                	Date da = new Date(dt);
	                	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	                	String fd = formatter.format(da);
	                	resultJson.put("week_date", (String) fd);
		            	
		            	resultJson.put("problems_seen", rs.getInt("problems_seen"));
		    			resultJson.put("problems_solved", rs.getInt("problems_solved"));
		            	weekArr.add(resultJson);;
		            }
		        }
	            stmt.close();
	            rs.close();

	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
    		weekJson.put("Week" + String.valueOf(loopCounter++), weekArr);
	        rangeArr.add(weekJson);
	    }
        return rangeArr.toString();    	

    }

    private Timestamp xDaysFromStartDate(Timestamp startDateTimestamp, int xDays) {
		Timestamp ts = null;

		if (xDays == 0) {
			xDays = 365;
		}
		Date currentDate = startDateTimestamp;
		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DAY_OF_MONTH, xDays);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		// convert calendar to date
		Date xDate = c.getTime();
		ts = new Timestamp(xDate.getTime());
		return ts;
    }

    
    private Timestamp convertStartDate(String filter) {
		Timestamp ts = null;

		try {
			Date fDate=new SimpleDateFormat("MM/dd/yyyy").parse(filter);
			Calendar c = Calendar.getInstance();
			c.setTime(fDate);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			// convert calendar to date
			Date xDate = c.getTime();
			ts = new Timestamp(xDate.getTime());
		}
		catch (Exception e) {
			
		}
		return ts;
    }
    
    
}