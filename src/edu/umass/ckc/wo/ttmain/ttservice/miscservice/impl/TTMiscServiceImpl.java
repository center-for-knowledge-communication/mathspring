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
import com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl.JAXPSAXParser;

import edu.umass.ckc.wo.beans.StudentDetails;
import edu.umass.ckc.wo.beans.Teacher;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassLandingReportStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassLiveDashboard;
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
import net.sf.json.*;

/**
 * Created by Frank 
 * Frank 	06-17-20	Issue #149
 */
@Service
public class TTMiscServiceImpl implements TTMiscService {
    private static Logger logger = Logger.getLogger(TTMiscServiceImpl.class);

	private ResourceBundle rb = null;
	private ResourceBundle rwrb = null;
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
        		loc = new Locale("es","US");	
        	}        	
    		ploc = loc;

    		try {
        		rb = ResourceBundle.getBundle("MathSpring",loc);
    			rwrb = ResourceBundle.getBundle("MSResearcherWorkbench",loc);
    		}
    		catch (Exception e) {
    			logger.error(e.getMessage());	
    		}
    		
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
        	
        	String q = "select tmc.researchcohortid as cohortId, coh.name as cohortName, t.ID as teacherId, t.username as username, fname as firstName, lname as lastName, c.id as classId, c.name as className from teacher t, teacher_map_cohorts tmc, class_map_cohorts as cmc, class c, research_cohort as coh where t.ID = tmc.teacherid and  tmc.researchcohortid = coh.researchcohortid and cmc.researchcohortid = coh.researchcohortid and cmc.classid = c.ID and c.teacherId = t.ID order by tmc.researchcohortid,lname asc;";

//             String q = "select tmc.researchcohortid as cohortId, coh.name as cohortName, t.ID as teacherId, t.username as username, fname as firstName ,lname as lastName, c.id as classId, c.name as className from teacher t, teacher_map_cohorts tmc, class c, research_cohort as coh where t.ID = tmc.teacherid and  tmc.researchcohortid = coh.researchcohortid and c.teacherId = t.ID order by tmc.researchcohortid,lname asc;";
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
//	            		System.out.println(colName + " " + colType + " " +  String.valueOf(rs.getInt(column)));
	            	}
	            	else {
	            		colValue = rs.getString(column);
//	        			System.out.println(colName + " " + colType + " " +  rs.getString(column));	 
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
        	String q = "select distinct tch.ID as teacherId, tch.userName as uname, tch.fname as fname, tch.lname as lname, cmc.classid, cls.name as className from class as cls, teacher as tch,  teacher_map_cohorts as tmc, class_map_cohorts as cmc where tch.ID = tmc.teacherid and tmc.researchcohortid = ? and cmc.researchcohortid = ? and cls.teacherId = tch.ID and cmc.classid = cls.ID order by tch.ID;";

        	//String q = "select distinct tch.ID as teacherId, tch.userName as uname, cls.ID as classId from class as cls, teacher as tch,  teacher_map_cohorts where tch.ID = teacher_map_cohorts.teacherid and teacher_map_cohorts.researchcohortid = ? and cls.teacherId = tch.ID order by tch.ID;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	if (!first) {
            		teacherClasses += ",";
            	}
            	else {
            		first = false;
            	}
            	String fullName = rs.getString("fname") + " " + rs.getString("lname");
            	String line = String.valueOf(rs.getInt("teacherId")) + "~" + rs.getString("uname") + "~" + fullName  + "~" + String.valueOf(rs.getInt("classId")) + "~" + rs.getString("className");
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
            String q = "SELECT tcs.teacherId AS teacherId, tcs.teacherUsername as uname, tcs.classId, tcs.className, sum(tcs.nbr_problems_seen) as sumProblemsSeen, sum(tcs.nbr_problems_solved) as sumProblemsSolved FROM teacher_class_slices as tcs, teacher_map_cohorts as tmc where tcs.teacherId = tmc.teacherid and tcs.cohortId = ? and tmc.researchcohortid = ? GROUP BY teacherId,classId";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            rs = stmt.executeQuery();
        	JSONArray bodyArr = new JSONArray();
        	JSONArray footerArr = new JSONArray();
        	JSONArray resultArr = new JSONArray();

        	int studyProblemsSeen = 0;
        	int studyProblemsSolved = 0;
        	int  studyPercentSolved = 0;
        	
            while (rs.next()) {
       			int sumProblemsSeen = rs.getInt("sumProblemsSeen");
       			int sumProblemsSolved = rs.getInt("sumProblemsSolved");
       			int pctProblemsSolved = 0;
       			if (sumProblemsSeen > 0) {
       				pctProblemsSolved = (sumProblemsSolved * 100) / sumProblemsSeen;
       			}
       			studyProblemsSeen += sumProblemsSeen;
       			studyProblemsSolved += sumProblemsSolved;
       			
       			String teacherName = rs.getString("uname");
       			if (teacherName.length() > 12) {
       				teacherName = teacherName.substring(0,11);
       			}
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("Teacher", teacherName);
        		resultJson.put("Class", rs.getString("className"));
        		resultJson.put("TID", rs.getString("teacherId"));
        		resultJson.put("CID", rs.getString("classId"));
           		resultJson.put("Seen", rs.getInt("sumProblemsSeen"));
       			resultJson.put("Solved", rs.getInt("sumProblemsSolved"));
       			resultJson.put("pctSolved", pctProblemsSolved);
            	bodyArr.add(resultJson);
            }            
            stmt.close();
            rs.close();           

            //// Create the footer array and add it to the resultArr
        	JSONObject footerJson  = new JSONObject();
    		footerJson.put("Teacher", "Study Total");
    		footerJson.put("Class", "");
    		footerJson.put("TID", "");
    		footerJson.put("CID", "");
       		footerJson.put("Seen", studyProblemsSeen);
   			footerJson.put("Solved", studyProblemsSolved);
   			if (studyProblemsSeen > 0) {
   				studyPercentSolved = (studyProblemsSolved * 100) / studyProblemsSeen;
   			}
   			footerJson.put("pctSolved", studyPercentSolved);
        	footerArr.add(footerJson);

        	resultArr.add(bodyArr);
   			resultArr.add(footerArr);
            
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public String getTeacherProblems(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        
        try {
            String q = "SELECT tcs.teacherId AS teacherId, tcs.teacherUsername as uname, sum(tcs.nbr_problems_seen) as sumProblemsSeen, sum(tcs.nbr_problems_solved) as sumProblemsSolved FROM teacher_class_slices as tcs, teacher_map_cohorts as tmc where tcs.teacherId = tmc.teacherid and tcs.cohortId = ? and tmc.researchcohortid = ? GROUP BY teacherId order by sumProblemsSolved DESC";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            rs = stmt.executeQuery();
        	JSONArray bodyArr = new JSONArray();
        	JSONArray footerArr = new JSONArray();
        	JSONArray resultArr = new JSONArray();

        	int studyProblemsSeen = 0;
        	int studyProblemsSolved = 0;
        	int  studyPercentSolved = 0;

        	while (rs.next()) {
        		
            	
       			int sumProblemsSeen = rs.getInt("sumProblemsSeen");
       			int sumProblemsSolved = rs.getInt("sumProblemsSolved");
       			int pctProblemsSolved = 0;
       			if (sumProblemsSeen > 0) {
       				pctProblemsSolved = (sumProblemsSolved * 100) / sumProblemsSeen;
       			}
       			studyProblemsSeen += sumProblemsSeen;
       			studyProblemsSolved += sumProblemsSolved;
       			
       			String teacherName = rs.getString("uname");
       			if (teacherName.length() > 12) {
       				teacherName = teacherName.substring(0,11);
       			}
            	            	
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("Teacher", teacherName);
        		resultJson.put("Id", rs.getString("teacherId"));
           		resultJson.put("Seen", rs.getInt("sumProblemsSeen"));
       			resultJson.put("Solved", sumProblemsSolved);
       			resultJson.put("pctSolved", pctProblemsSolved);
            	bodyArr.add(resultJson);
            }            
            stmt.close();
            rs.close();

            //// Create the footer array and add it to the resultArr
        	JSONObject footerJson  = new JSONObject();
    		footerJson.put("Teacher", "Study Total");
    		footerJson.put("Id", "");
       		footerJson.put("Seen", studyProblemsSeen);
   			footerJson.put("Solved", studyProblemsSolved);
   			if (studyProblemsSeen > 0) {
   				studyPercentSolved = (studyProblemsSolved * 100) / studyProblemsSeen;
   			}
   			footerJson.put("pctSolved", studyPercentSolved);
        	footerArr.add(footerJson);

        	resultArr.add(bodyArr);
   			resultArr.add(footerArr);

            
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }
        
    public String getTeacherProblemsStudentAverages(Connection conn, int cohortId) throws SQLException {

    	
    	String avg_problems_student =    "Problems per Active Student";
    	String avg_minutes_student =     "Actual Minutes per Student";
    	String avg_minutes_per_problem = "Actual Minutes per Problem";
    	String minutes_to_solve =        "Total Minutes to Solve";
    	String students_not_active =     "# Students NOT using MS";
    	String students_not_active_pct = "% Students NOT using MS";

    	String need_to_spend_more_time = "Need to spend more time on MS";
    	
    	int fppsLowLimit = 20;
    	float fminsLowLimit = (float) 1.0;
    	float fminsHighLimit = (float) 1.75;
    	int fmpsLowLimit = 30;
    	int studentCountDiffLowLimit = 15;
    	int studentCountDiffHighLimit = 30;
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        int currentActionTotal = 0;
        try {
            String q = "SELECT tcs.teacherId AS teacherId, tcs.teacherUsername as uname, tcs.teacherFullname as fullname, sum(tcs.nbr_problems_solved) as sumProblemsSolved, sum(tcs.time_problems_solved) as time_problems_solved, tmc.expectedStudentCount FROM teacher_class_slices as tcs, teacher_map_cohorts as tmc where tcs.teacherId = tmc.teacherid and tcs.cohortId = ? and tmc.researchcohortid = ? GROUP BY teacherId order by sumProblemsSolved DESC";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            rs = stmt.executeQuery();
        	JSONArray bodyArr = new JSONArray();
        	JSONArray footerArr = new JSONArray();
        	JSONArray resultArr = new JSONArray();

        	int studyProblemsSolved = 0;
        	int studentCount = 0;
        	int studyStudentCount=0;
        	int studyMinutesToSolve=0;
        	int studyExpectedStudentCount=0;
        	
        	while (rs.next()) {

        		studentCount = getDistinctStudentsWhoSolvedProblems(conn, Integer.valueOf(rs.getString("teacherId")));

       			int sumProblemsSolved = rs.getInt("sumProblemsSolved");
       			studyProblemsSolved += sumProblemsSolved;
       			studyStudentCount += studentCount;
       			
       			String comments = "";
       			
       			String teacherName = rs.getString("uname");
       			if (teacherName.length() > 12) {
       				teacherName = teacherName.substring(0,11);
       			}

       			String fullName = rs.getString("fullname");
       			
       			int minutesToSolve =  rs.getInt("time_problems_solved");
   				minutesToSolve = minutesToSolve / 60000;
       			studyMinutesToSolve += minutesToSolve;

            	JSONObject resultJson = new JSONObject();
       			resultJson.put("Id", rs.getString("teacherId"));
        		resultJson.put("Teacher", fullName);
        		int expectedStudentCount = rs.getInt("expectedStudentCount");
        		if (expectedStudentCount == 0) {
        			expectedStudentCount = studentCount;
        		}
        		studyExpectedStudentCount += expectedStudentCount;
       			resultJson.put("Actual # Students", expectedStudentCount);
       			resultJson.put("Active Students", studentCount);
       			int studentCountDiff = expectedStudentCount - studentCount;
       			int StudentCountDiffPct = 0;
       			if (expectedStudentCount > 0) {
           			StudentCountDiffPct = (studentCountDiff * 100) / expectedStudentCount;       				
       			}
       			String StudentCountDiffColor = "white";
       			if (studentCountDiff >= 0) {;
	       			if (StudentCountDiffPct > studentCountDiffHighLimit) {
	       				StudentCountDiffColor = "red" + "~" + "Below acceptable student usage";      			
	       			}
	       			else {
	       				if (StudentCountDiffPct > studentCountDiffLowLimit) {
	       					StudentCountDiffColor = "yellow" + "~" + "Below average student usage";;	
	       				}       				
	       			}
       	       		resultJson.put(students_not_active, studentCountDiff);
       	       		resultJson.put(students_not_active_pct, StudentCountDiffPct + "%" +"~" + StudentCountDiffColor);
       			}
       			else {
       	       		resultJson.put(students_not_active, (studentCountDiff * -1) + " extra");       				
       	       		resultJson.put(students_not_active_pct, 0);
       			}
       			resultJson.put("Total # Problems", sumProblemsSolved);
       			resultJson.put(minutes_to_solve, minutesToSolve);
       			if (studentCount == 0) {
           			resultJson.put(avg_problems_student, 0);
           			resultJson.put(avg_minutes_per_problem, 0);
       				resultJson.put(avg_minutes_student,  0);
       			}
       			else {
       				float fpps = 0;
       				if (studentCount > 0) {
       					fpps = sumProblemsSolved / (float)studentCount;
       				}
       				String strpps = String.format("%.02f", fpps);
       				if (fpps < fppsLowLimit) {
       					strpps = strpps + "~" + "orange" + "~" + "Below average";
       				} 
       				else {
   						strpps = strpps + "~" + "white";  
       				}      				
       				resultJson.put(avg_problems_student, strpps);
       				  
       				float fmins = 0;
       				if (sumProblemsSolved > 0) {
       					fmins = (float)minutesToSolve / (float)sumProblemsSolved;
       				}
       				String strMins = String.format("%.02f", fmins);
       				if (fmins < fminsLowLimit) {
       					strMins = strMins + "~" + "orange" + "~" + "Below average";
       				} 
       				else {
       					if (fmins > fminsHighLimit) {
       						strMins = strMins + "~" + "lightgreen" + "~" + "Above average";       					
       					}
       					else {
       						strMins = strMins + "~" + "white";  
       					}
       				}
       				resultJson.put(avg_minutes_per_problem, strMins);
           			
       				float fmps = 0;
       				if (studentCount > 0) {
       					fmps =  minutesToSolve / (float)studentCount;
       				}
       				String strmps = String.format("%.02f", fmps);
       				if (fmps < fmpsLowLimit) {
       					strmps = strmps + "~" + "yellow" + "~" + need_to_spend_more_time;
       					comments += need_to_spend_more_time;
       				} 
       				else {
   						strmps = strmps + "~" + "white";  
       				}      				
           			resultJson.put(avg_minutes_student,  strmps);
           			resultJson.put("Comments",  comments);
           			
       			}
       			bodyArr.add(resultJson);
            	
            }            
            stmt.close();
            rs.close();

            //// Create the footer array and add it to the resultArr
        	JSONObject footerJson  = new JSONObject();
    		footerJson.put("Id", "");
    		footerJson.put("Teacher", "Study Total");
    		footerJson.put("Actual # Students", studyExpectedStudentCount);
    		footerJson.put("Active Students", studyStudentCount);
    		
    		int studyStudentCountDiffPct = 0;
   			int studyStudentCountDiff = studyExpectedStudentCount - studyStudentCount;
   			
   			if (studyExpectedStudentCount > 0) {
   				studyStudentCountDiffPct = (studyStudentCountDiff * 100) / studyExpectedStudentCount;
   			}
/*
   			String studyStudentCountDiffColor = "white";
   			if (studyStudentCountDiff >= 0) {;
       			if (studyStudentCountDiffPct > studentCountDiffHighLimit) {
       				studyStudentCountDiffColor = "red";      			
       			}
       			else {
       				if (studyStudentCountDiffPct > studentCountDiffLowLimit) {
       					studyStudentCountDiffColor = "yellow";	
       				}       				
       			}
   	       		footerJson.put(students_not_active, studyStudentCountDiff);
   	       		footerJson.put(students_not_active_pct, studyStudentCountDiffPct + "%" +"~" + studyStudentCountDiffColor);
   			}
   			else {
   				footerJson.put(students_not_active, (studyStudentCountDiff * -1) + " extra");       				
   				footerJson.put(students_not_active_pct, 0);
   			}    		
*/    		
			footerJson.put(students_not_active, " ");       				
			footerJson.put(students_not_active_pct, " ");
   			
   			
    		footerJson.put("Total # Problems", studyProblemsSolved);
   			footerJson.put(minutes_to_solve, studyMinutesToSolve);
   			if (studyStudentCount == 0) {
   				footerJson.put(avg_problems_student, 0);
       			footerJson.put(avg_minutes_per_problem, 0);
   				footerJson.put(avg_minutes_student,  0);
   			}
   			else {
   				if (studyStudentCount > 0)
   					footerJson.put(avg_problems_student, studyProblemsSolved / studyStudentCount);
   				else
   					footerJson.put(avg_problems_student, 0);
   				float fmins = 0;
   				if (studyProblemsSolved > 0) {
   					fmins = (float)studyMinutesToSolve / (float)studyProblemsSolved;
   				}
   				String strMins = String.format("%.02f", fmins);   				
   				footerJson.put(avg_minutes_per_problem, strMins);
   				
   				fmins = 0;
   				if (studyStudentCount > 0)
   					fmins = (float)studyMinutesToSolve / (float)studyStudentCount;
   				strMins = String.format("%.02f", fmins);   				
   				footerJson.put(avg_minutes_student,  strMins);
   			}
   			
        	footerArr.add(footerJson);

        	resultArr.add(bodyArr);
   			resultArr.add(footerArr);

            
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }
        
    public String getStudentCensus(Connection conn, int cohortId) throws SQLException {

        ResultSet ev_rs = null;
        PreparedStatement ev_stmt = null;
    	JSONObject evJson = new JSONObject();
    	
        try {

        	String ev_q = "select ev.studid, COUNT(ev.id) as eventCount  from eventlog ev where studid > 45000 group by studId;";
        	ev_stmt = conn.prepareStatement(ev_q);
            ev_rs = ev_stmt.executeQuery();

            while (ev_rs.next()) {
            	evJson.put(ev_rs.getString("studId"), ev_rs.getInt("eventCount"));
            }
            ev_stmt.close();
            ev_rs.close();
            
        } finally {
            if (ev_stmt != null)
            	ev_stmt.close();
            if (ev_rs != null)
            	ev_rs.close();
        }

        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
        	
        	String q = "select st.id, st.userName, st.fname, st.lname, st.trialUser, st.keepData, st.keepUser, st.pedagogyId, cls.id as classId, cls.name as 'w_class_main::name', cls.teacherid as 'w_teacher_main::ID', tch.lname as 'w_teacher_main::lname', COUNT(DISTINCT(sh.problemId)) as c_numProblemsinHist from student st, studentproblemhistory sh, class cls, teacher tch, teacher_map_cohorts tmc, class_map_cohorts cmc, research_cohort coh where st.id = sh.studId and st.classId = cls.id and st.id and tch.id = cls.teacherid and cls.id = cmc.classId and tch.id = tmc.teacherid and cls.id = cmc.classid and tmc.researchcohortid = coh.researchcohortid and cmc.researchcohortid = coh.researchcohortid  and coh.researchcohortid = ? group by st.id;";

        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            
            rs = stmt.executeQuery();
        	JSONObject headerJson = new JSONObject();
        	headerJson.put("id", "id");
        	headerJson.put("userName", "userName");
        	headerJson.put("fname", "fname");
        	headerJson.put("lname", "lname");
        	headerJson.put("w_class_main::name", "w_class_main::name");
        	headerJson.put("w_teacher_main::ID", "w_teacher_main::ID");
        	headerJson.put("w_teacher_main::lname", "w_teacher_main::lname");
        	headerJson.put("c_RealStudent", "c_RealStudent");
        	headerJson.put("c_numProblemsinHist", "c_numProblemsinHist");
        	headerJson.put("c_numEvents", "c_numEvents");
        	headerJson.put("c_gender", "c_gender");
        	headerJson.put("class", "class");
        	headerJson.put("trialUser", "trialUser");
        	headerJson.put("keepData", "keepData");
        	headerJson.put("keepUser", "keepUser");
        	headerJson.put("pedagogyId", "pedagogyId");
        	
        	JSONArray resultArr = new JSONArray();
        	resultArr.add(headerJson);
        	
            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
            	resultJson.put("id", rs.getString("id"));
            	resultJson.put("userName", rs.getString("userName"));
            	resultJson.put("fname", rs.getString("fname"));
            	resultJson.put("lname", rs.getString("lname"));
            	resultJson.put("w_class_main::name", rs.getString("w_class_main::name"));
            	resultJson.put("w_teacher_main::ID", rs.getString("w_teacher_main::ID"));
            	resultJson.put("w_teacher_main::lname", rs.getString("w_teacher_main::lname"));
            	resultJson.put("c_RealStudent", " ");
            	resultJson.put("c_numProblemsinHist", rs.getString("c_numProblemsinHist"));
            	resultJson.put("c_numEvents", evJson.get(rs.getString("id")));
            	resultJson.put("c_gender", " ");
            	resultJson.put("class", " ");
            	resultJson.put("trialUser", rs.getString("trialUser"));
            	resultJson.put("keepData", rs.getString("keepData"));
            	resultJson.put("keepUser", rs.getString("keepUser"));
            	resultJson.put("pedagogyId", rs.getString("pedagogyId"));
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

    
    public String getTeacherProblemsEffort(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        int currentActionTotal = 0;
        try {
            String q = "SELECT tcs.teacherId AS teacherId, tcs.teacherUsername as uname, sum(tcs.SOF) as SOF, sum(tcs.ATT) as ATT, sum(tcs.SHINT) as SHINT, sum(tcs.SHELP) as SHELP, sum(tcs.GUESS) as GUESS, sum(tcs.NOTR) as NOTR, sum(tcs.SKIP) as SKIP,sum(tcs.GIVEUP) as GIVEUP, sum(tcs.NODATA) as NODATA FROM teacher_class_slices as tcs, teacher_map_cohorts as tmc where tcs.teacherId = tmc.teacherid and tcs.cohortId = ? and tmc.researchcohortid = ? GROUP BY teacherId";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            rs = stmt.executeQuery();
        	JSONArray bodyArr = new JSONArray();
        	JSONArray footerArr = new JSONArray();
        	JSONArray resultArr = new JSONArray();

        	while (rs.next()) {
            	
       			String teacherName = rs.getString("uname");
       			if (teacherName.length() > 12) {
       				teacherName = teacherName.substring(0,11);
       			}
            	            	
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("teacherName", teacherName);
        		resultJson.put("teacherId", rs.getString("teacherId"));
           		resultJson.put("SOF", rs.getInt("SOF"));
           		resultJson.put("ATT", rs.getInt("ATT"));
           		resultJson.put("SHINT", rs.getInt("SHINT"));
           		resultJson.put("SHELP", rs.getInt("SHELP"));
           		resultJson.put("GUESS", rs.getInt("GUESS"));
           		resultJson.put("NOTR", rs.getInt("NOTR"));
           		resultJson.put("SKIP", rs.getInt("SKIP"));
           		resultJson.put("GIVEUP", rs.getInt("GIVEUP"));
           		resultJson.put("NODATA", rs.getInt("NODATA"));
            	bodyArr.add(resultJson);
            }            
            stmt.close();
            rs.close();

            q= "SELECT tcs.cohortid as cohortid, coh.name as cohortName, sum(tcs.SOF) as SOF, sum(tcs.ATT) as ATT, sum(tcs.SHINT) as SHINT, sum(tcs.SHELP) as SHELP, sum(tcs.GUESS) as GUESS, sum(tcs.NOTR) as NOTR, sum(tcs.SKIP) as SKIP,sum(tcs.GIVEUP) as GIVEUP, sum(tcs.NODATA) as NODATA FROM teacher_class_slices as tcs, teacher_map_cohorts as tmc, research_cohort as coh where tcs.teacherId = tmc.teacherid and tcs.cohortId = ? and tmc.researchcohortid = ? and coh.researchcohortid = ?;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            stmt.setInt(3, cohortId);
            rs = stmt.executeQuery();

        	while (rs.next()) {
            	
           	            	
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("cohortName", rs.getString("cohortName"));
           		resultJson.put("SOF", rs.getInt("SOF"));
           		resultJson.put("ATT", rs.getInt("ATT"));
           		resultJson.put("SHINT", rs.getInt("SHINT"));
           		resultJson.put("SHELP", rs.getInt("SHELP"));
           		resultJson.put("GUESS", rs.getInt("GUESS"));
           		resultJson.put("NOTR", rs.getInt("NOTR"));
           		resultJson.put("SKIP", rs.getInt("SKIP"));
           		resultJson.put("GIVEUP", rs.getInt("GIVEUP"));
           		resultJson.put("NODATA", rs.getInt("NODATA"));
            	footerArr.add(resultJson);
            }            
            stmt.close();
            rs.close();
            
        	resultArr.add(bodyArr);
   			resultArr.add(footerArr);

            
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public String getClassProblemsSolvedDashboard(Connection conn, int cohortId, String filter) throws SQLException {
    	String result = "";
    	
    	int classId = Integer.valueOf(filter);
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "Select sh.problemBeginTime, count(problemId) AS noOfProblemsSolved from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=? and sh.isSolved = 1 and sh.mode != 'demo';";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	int t = rs.getInt("noOfProblemsSolved");
            	result = String.valueOf(t);
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

    
    
    public String getClassProblemsEffortDashboard(Connection conn, int cohortId, String filter) throws SQLException {
        
    	int classId = Integer.valueOf(filter);
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        int currentActionTotal = 0;
        try {
            String q = "SELECT tcs.classId AS classId, tcs.className as className, sum(tcs.SOF) as SOF, sum(tcs.ATT) as ATT, sum(tcs.SHINT) as SHINT, sum(tcs.SHELP) as SHELP, sum(tcs.GUESS) as GUESS, sum(tcs.NOTR) as NOTR, sum(tcs.SKIP) as SKIP,sum(tcs.GIVEUP) as GIVEUP, sum(tcs.NODATA) as NODATA FROM teacher_class_slices as tcs, class_map_cohorts as cmc where tcs.classId = cmc.classid and tcs.cohortId = ? and cmc.researchcohortid = ? and tcs.classId = ? GROUP BY classId";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            stmt.setInt(3, classId);
            
            rs = stmt.executeQuery();
        	JSONArray bodyArr = new JSONArray();
        	JSONArray resultArr = new JSONArray();

        	while (rs.next()) {
            	          	            	
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("className", rs.getString("className"));
        		resultJson.put("classId", rs.getInt("classId"));
           		resultJson.put("SOF", rs.getInt("SOF"));
           		resultJson.put("ATT", rs.getInt("ATT"));
           		resultJson.put("SHINT", rs.getInt("SHINT"));
           		resultJson.put("SHELP", rs.getInt("SHELP"));
           		resultJson.put("GUESS", rs.getInt("GUESS"));
           		resultJson.put("NOTR", rs.getInt("NOTR"));
           		resultJson.put("SKIP", rs.getInt("SKIP"));
           		resultJson.put("GIVEUP", rs.getInt("GIVEUP"));
           		resultJson.put("NODATA", rs.getInt("NODATA"));
            	bodyArr.add(resultJson);
            }            
            stmt.close();
            rs.close();
            
        	resultArr.add(bodyArr);
            
            return resultArr.toString();    	
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public String getClassProblemsEffortRpt(Connection conn, int cohortId, String filter) throws SQLException {
        
    	int classId = Integer.valueOf(filter);
    	
    	String effortStr = getClassProblemsEffortSet(conn,classId);
    	String effortSplitter[] = effortStr.split(",");

    	JSONArray bodyArr = new JSONArray();
        JSONArray resultArr = new JSONArray();

            	          	            	
        JSONObject resultJson = new JSONObject();
    	resultJson.put("SOF", effortSplitter[0]);
     	resultJson.put("ATT", effortSplitter[1]);
       	resultJson.put("SHINT", effortSplitter[2]);
       	resultJson.put("SHELP", effortSplitter[3]);
       	resultJson.put("GUESS", effortSplitter[4]);
       	resultJson.put("NOTR", effortSplitter[5]);
       	resultJson.put("SKIP", effortSplitter[6]);
       	resultJson.put("GIVEUP", effortSplitter[7]);
       	resultJson.put("NODATA", effortSplitter[8]);
        bodyArr.add(resultJson);
            
        resultArr.add(bodyArr);
            
        return resultArr.toString();    	
    }

    
    public String teacherClassCount(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;

         try {
        	
        	String q = "select count(cls.id) as classCount, tch.lname as teacherName, tch.userName as userName, tch.Id as teacherid from class as cls, teacher as tch, teacher_map_cohorts as tmc, class_map_cohorts as cmc where tch.ID = tmc.teacherid and tmc.researchcohortid = ? and cmc.researchcohortid = ? and cls.teacherId = tch.ID and cmc.classid = cls.ID group by teacherId order by tch.lname;";

        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("userName", rs.getString("userName"));
        		resultJson.put("teacherId", rs.getString("teacherId"));
           		resultJson.put("classCount", rs.getInt("classCount"));
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
    
    public String teacherStudentCount(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;

         try {
        	
        	String q = "select count(s.id) as studentCount, tch.lname as teacherName, tch.userName as userName, tch.Id as teacherid,  cls.id as classId, cls.name as className from student as s, class as cls, teacher as tch, teacher_map_cohorts as tmc, class_map_cohorts as cmc where tch.ID = tmc.teacherid and tmc.researchcohortid = ? and cmc.researchcohortid = ? and cls.teacherId = tch.ID and cmc.classid = cls.ID and s.classId = cls.id group by teacherId order by tch.lname;";

        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("userName", rs.getString("userName"));
        		resultJson.put("teacherId", rs.getString("teacherId"));
           		resultJson.put("studentCount", rs.getInt("studentCount"));
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
    
    public String teacherClassStudentCount(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;

         try {
        	
        	String q = "select count(s.id) as studentCount, tch.lname as teacherName, tch.userName as userName, tch.Id as teacherid,  cls.id as classId, cls.name as className from student as s, class as cls, teacher as tch, teacher_map_cohorts as tmc, class_map_cohorts as cmc where tch.ID = tmc.teacherid and tmc.researchcohortid = ? and cmc.researchcohortid = ? and cls.teacherId = tch.ID and cmc.classid = cls.ID and s.classId = cls.id group by teacherId, classId order by tch.lname;";

        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("userName", rs.getString("userName"));
        		resultJson.put("className", rs.getString("className"));
        		resultJson.put("teacherId", rs.getString("teacherId"));
        		resultJson.put("classId", rs.getString("classId"));
           		resultJson.put("studentCount", rs.getInt("studentCount"));
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
    
    public String teacherStudentEffort(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;

         try {
        	
        	String q = "select count(s.id) as studentCount, tch.lname as teacherName, tch.userName as userName, tch.Id as teacherid,  cls.id as classId, cls.name as className from student as s, class as cls, teacher as tch, teacher_map_cohorts as tmc, class_map_cohorts as cmc where tch.ID = tmc.teacherid and tmc.researchcohortid = ? and cmc.researchcohortid = ? and cls.teacherId = tch.ID and cmc.classid = cls.ID and s.classId = cls.id group by teacherId, classId order by tch.lname;";

        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("userName", rs.getString("userName"));
        		resultJson.put("className", rs.getString("className"));
        		resultJson.put("teacherId", rs.getString("teacherId"));
        		resultJson.put("classId", rs.getString("classId"));
           		resultJson.put("studentCount", rs.getInt("studentCount"));
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
    
    public String reportedProblemErrors(Connection conn, int cohort, String filter) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
    	JSONArray resultArr = new JSONArray();
    	
    	int adjacentRows = 3;
    	
    	String fromDate = "2020-09-01 00:00:00";

    	String q_date =    "select evt.studId as sid, evt.sessNum as sessId, evt.id as evtid, evt.problemId as probId, evt.userInput as comment, evt.time, sph.isProbBroken as probStatus from eventlog as evt  INNER JOIN studentproblemhistory as sph ON evt.probHistoryID = sph.ID and action = 'ReportError' and sph.isProbBroken < 3 and time > ? order by evt.sessNum DESC, sph.problemBeginTime DESC;";
    	String q_class =   "select stu.classId as classId, evt.studId as sid, evt.sessNum as sessId, evt.id as evtid, evt.problemId as probId, evt.userInput as comment, evt.time, sph.isProbBroken as probStatus from eventlog as evt  INNER JOIN studentproblemhistory as sph ON evt.probHistoryID = sph.ID INNER JOIN student as stu ON evt.studId=stu.id and action = 'ReportError' and sph.isProbBroken < 3 and time > ? order by stu.classId, evt.sessNum DESC, sph.problemBeginTime DESC;";
    	String q_problem = "select evt.problemId as probId, evt.studId as sid, evt.sessNum as sessId, evt.id as evtid, evt.problemId as probId, evt.userInput as comment, evt.time, evt.curTopicId, sph.isProbBroken as probStatus from eventlog as evt  INNER JOIN studentproblemhistory as sph ON evt.probHistoryID = sph.ID INNER JOIN student as stu ON evt.studId=stu.id and action = 'ReportError' and sph.isProbBroken < 3 and time > ? order by evt.problemId, evt.sessNum DESC, sph.problemBeginTime DESC;";
    	
    	if (filter.equals("date")) {
        
	         try {
	        	
	        	stmt = conn.prepareStatement(q_date);
	            stmt.setString(1, fromDate);       	
	            
	            rs = stmt.executeQuery();
	            
	            while (rs.next()) {
	            	JSONObject resultJson = new JSONObject();
	        		resultJson.put("Session Id", String.valueOf(rs.getInt("sessId")));
//	        		resultJson.put("Student Id", String.valueOf(rs.getInt("sid")));
	        		resultJson.put("Problem Id", String.valueOf(rs.getInt("probId")));
	        		resultJson.put("probStatus", String.valueOf(rs.getInt("probStatus")));
	        		String comment = rs.getString("comment");
	        		if (comment == null) {
	        			comment = " ";
	        		}
	        		else {
	        			comment = comment.trim();
	        		}
	        		resultJson.put("Comment", comment);
	        		resultJson.put("Event Id", String.valueOf(rs.getInt("evtid")));
	            	resultArr.add(resultJson);
	            }            
	            stmt.close();
	            rs.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
    	}

    	if (filter.equals("classId")) {
            
	         try {
	        	
	        	stmt = conn.prepareStatement(q_class);
	            stmt.setString(1, fromDate);
	            
	            rs = stmt.executeQuery();
	
	            while (rs.next()) {
	            	JSONObject resultJson = new JSONObject();
	        		resultJson.put("ClassId Id", String.valueOf(rs.getInt("classId")));
	        		resultJson.put("Student Id", String.valueOf(rs.getInt("sid")));
	        		resultJson.put("Session Id", String.valueOf(rs.getInt("sessId")));
	        		resultJson.put("Problem Id", String.valueOf(rs.getInt("probId")));
	        		resultJson.put("probStatus", String.valueOf(rs.getInt("probStatus")));
	        		String comment = rs.getString("comment");
	        		if (comment == null) {
	        			comment = " ";
	        		}
	        		else {
	        			comment = comment.trim();
	        		}
	        		resultJson.put("Comment", comment);
	        		resultJson.put("Event Id", String.valueOf(rs.getInt("evtid")));

	            	resultArr.add(resultJson);
	            }            
	            stmt.close();
	            rs.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
    	}
    	
    	if (filter.equals("problemId")) {
            
	         try {
	        	
	        	stmt = conn.prepareStatement(q_problem);
	            stmt.setString(1, fromDate);
	            
	            rs = stmt.executeQuery();
	
	            while (rs.next()) {
	            	JSONObject resultJson = new JSONObject();
	        		resultJson.put("Problem Id", String.valueOf(rs.getInt("probId")));
	        		resultJson.put("Topic Id", String.valueOf(rs.getInt("curTopicId")));
	        		resultJson.put("Session Id", String.valueOf(rs.getInt("sessId")));
	        		resultJson.put("Student Id", String.valueOf(rs.getInt("sid")));
	        		resultJson.put("probStatus", String.valueOf(rs.getInt("probStatus")));
	        		String comment = rs.getString("comment");
	        		if (comment == null) {
	        			comment = " ";
	        		}
	        		else {
	        			comment = comment.trim();
	        		}
	        		resultJson.put("Comment", comment);
	        		resultJson.put("Event Id", String.valueOf(rs.getInt("evtid")));

	            	resultArr.add(resultJson);
	            }            
	            stmt.close();
	            rs.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
    	}

    	
    	return resultArr.toString();    	
   	}
    
    public String sessionProblems(Connection conn, int cohort, String filter) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
    	JSONArray resultArr = new JSONArray();
    	
    	int adjacentRows = 3;
    	

    	//String q_session = "select distinct evt.sessNum as sessId, evt.probkemId as problemId from eventlog as evt  INNER JOIN studentproblemhistory as sph ON evt.ID = sph.ID order by evt.sessNum DESC, sph.problemBeginTime DESC;";
    	String q_session = "select * from studentproblemhistory where sessionId = ?";
    	
        	int sessionId = Integer.valueOf(filter);
	        try {
	        	
	        	stmt = conn.prepareStatement(q_session);
	            stmt.setInt(1, sessionId);	            
	            rs = stmt.executeQuery();
	
	            while (rs.next()) {
	            	JSONObject resultJson = new JSONObject();
	        		resultJson.put("Problem Id", String.valueOf(rs.getInt("problemId")));
	        		resultJson.put("Effort", rs.getString("effort"));
	        		resultJson.put("Attempts to Solve", String.valueOf(rs.getInt("numAttemptsToSolve")));
	        		resultJson.put("Mistakes", String.valueOf(rs.getInt("numMistakes")));
	        		resultJson.put("Hints seen", String.valueOf(rs.getInt("numHints")));
	            	resultArr.add(resultJson);
	            }            
	            stmt.close();
	            rs.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
    	
    	return resultArr.toString();    	
   	}

    

    
    public String getProblemHistoryErrorData(Connection conn, int cohort, String filter) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
    	JSONArray resultArr = new JSONArray();
    	JSONObject resultJson = new JSONObject();   	
    	

    	//String q_session = "select distinct evt.sessNum as sessId, evt.probkemId as problemId from eventlog as evt  INNER JOIN studentproblemhistory as sph ON evt.ID = sph.ID order by evt.sessNum DESC, sph.problemBeginTime DESC;";
    	String q_session = "select evt.probHistoryId as historyId, evt.userInput as comment, evt.problemId as problemId, sph.isProbBroken as probStatus from eventlog as evt, studentproblemhistory as sph where evt.Id = ? and evt.probHistoryId = sph.ID";
    	
        	int eventId = Integer.valueOf(filter);
	        try {
	        	
	        	stmt = conn.prepareStatement(q_session);
	            stmt.setInt(1, eventId);	            
	            rs = stmt.executeQuery();
	
	            while (rs.next()) {
	            		
	        		resultJson.put("problemId", String.valueOf(rs.getInt("problemId")));
	        		resultJson.put("comment", rs.getString("comment"));
	        		resultJson.put("probStatus", String.valueOf(rs.getInt("probStatus")));
	        		resultJson.put("historyId", String.valueOf(rs.getInt("historyId")));
	            }            
	            stmt.close();
	            rs.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
    	
    	return resultJson.toString();    	
   	}
    
    public String updateProblemHistoryErrorData(Connection conn, int cohort, String filter) throws SQLException {
    	int result = 0;
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
    	
        String[] splitter = filter.split("~");
        if (!(splitter.length == 4) ) {
        	return("must send 4 params");
        }
        else {
	        int eventId = Integer.valueOf(splitter[0]);
	        int historyId = Integer.valueOf(splitter[1]);
	        int probStatus = Integer.valueOf(splitter[2]);
	        String comment = splitter[3];
	    	
	    	String q_evt = "update eventlog set userInput = ? where id = ?;";
	        try {
	        	
	        	stmt = conn.prepareStatement(q_evt);
	            stmt.setString(1, comment);	            
	            stmt.setInt(2, eventId);	            
	            result = stmt.executeUpdate();		
	            stmt.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	        }
	
	    	String q_sph = "update studentproblemhistory set isProbBroken =  ? where ID = ?;";
	        try {
	        	
	        	stmt = conn.prepareStatement(q_sph);
	            stmt.setInt(1, probStatus);	            
	            stmt.setInt(2, historyId);	            
	            result = stmt.executeUpdate();		
	            stmt.close();
	        } finally {
	            if (stmt != null)
	                stmt.close();
	        }        
        }
    	return "Success";    	
   	}
    
    
    
    public String teacherClassTableSlices(Connection conn, int cohortId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;

         try {
        	
        	String q = "select * from teacher_class_slices  where cohortId = ?;";

        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("Teacher Id", rs.getString("teacherId"));
        		resultJson.put("Teacher Username", rs.getString("teacherUserName"));
        		resultJson.put("Teacher FullName", rs.getString("teacherFullName"));
        		resultJson.put("Class Id", rs.getString("classId"));
        		resultJson.put("Class Name", rs.getString("className"));
        		
            	Timestamp ts = rs.getTimestamp("week_startdate");
            	long dt =  ts.getTime();
            	Date da = new Date(dt);
            	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            	String fd = formatter.format(da);

           		resultJson.put("Week startdate", (String) fd);
           		resultJson.put("Week Nbr", rs.getInt("week_nbr"));
           		resultJson.put("Prob Solved", rs.getInt("nbr_problems_solved"));
           		resultJson.put("Prob Seen", rs.getInt("nbr_problems_seen"));
           		resultJson.put("Stud Active", rs.getInt("nbr_active_students"));
           		resultJson.put("SOF", rs.getInt("SOF"));
           		resultJson.put("ATT", rs.getInt("ATT"));
           		resultJson.put("SHINT", rs.getInt("SHINT"));
           		resultJson.put("SHELP", rs.getInt("SHELP"));
           		resultJson.put("GUESS", rs.getInt("GUESS"));
           		resultJson.put("NOTR", rs.getInt("NOTR"));
           		resultJson.put("SKIP", rs.getInt("SKIP"));
           		resultJson.put("GIVEUP", rs.getInt("GIVEUP"));
           		resultJson.put("NODATA", rs.getInt("NODATA"));
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

        System.out.println(filter);
    	String[] splitter = filter.split("~");
    	
    	int startWeek = Integer.valueOf(splitter[0]);
    	int intDuration = Integer.valueOf(splitter[1]);
    	int weeksToReport = Integer.valueOf(splitter[2]);

    		
        try {
        	
        	String q = "SELECT tcs.teacherId AS teacherId, tcs.teacherUsername as uname, tcs.classId, tcs.className as className, sum(tcs.nbr_active_students) as sumActiveStudents FROM teacher_class_slices as tcs, teacher_map_cohorts as tmc where tcs.teacherId = tmc.teacherid and tcs.cohortId = ? and tmc.researchcohortid = ? and tcs.week_nbr BETWEEN ? and ? GROUP BY teacherId,classId";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, cohortId);
            stmt.setInt(3, startWeek);
            stmt.setInt(4, startWeek+weeksToReport);
            
            rs = stmt.executeQuery();
        	JSONArray resultArr = new JSONArray();

            while (rs.next()) {
            	JSONObject resultJson = new JSONObject();
        		resultJson.put("teacherId", rs.getString("teacherId"));
        		resultJson.put("classId", rs.getString("classId"));
        		resultJson.put("username", rs.getString("uname"));
        		resultJson.put("className", rs.getString("className"));
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
    
    
/*
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
  */  
    
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
    		case "getTeacherClassCount":
				result = teacherClassCount(conn, Integer.valueOf(cohortId));  
				break;
    		case "getTeacherStudentCount":
				result = teacherStudentCount(conn, Integer.valueOf(cohortId));  
				break;
    		case "getTeacherClassStudentCount":
				result = teacherClassStudentCount(conn, Integer.valueOf(cohortId));  
				break;    			
    		case "getTeacherProblemsEffort":
				result = getTeacherProblemsEffort(conn, Integer.valueOf(cohortId));  
				break;
    		case "getClassProblemsEffortDashboard":
				result = getClassProblemsEffortDashboard(conn, Integer.valueOf(cohortId), filter);  
				break;
    		case "getClassProblemsSolvedDashboard":
    			result = getClassProblemsSolvedDashboard(conn, Integer.valueOf(cohortId), filter);
    			break;
    		case "getClassProblemsEffortRpt":
				result = getClassProblemsEffortRpt(conn, Integer.valueOf(cohortId), filter);  
				break;
    			
    		case "teacherProblems":
				result = getTeacherProblems(conn, Integer.valueOf(cohortId));  
				break;
    		case "teacherProblemsStudentAverages":
				result = getTeacherProblemsStudentAverages(conn, Integer.valueOf(cohortId));  
				break;

    		case "getStudentCensus":
				result = getStudentCensus(conn, Integer.valueOf(cohortId));  
				break;

    		case "teacherClassActiveStudentCount":
				result = getTeacherActiveStudentCount(conn, Integer.valueOf(cohortId), filter);  
				break;

    		case "getTeacherClassTableSlices":
				result = teacherClassTableSlices(conn, Integer.valueOf(cohortId));  
				break;
    		
    		case "getReportedProblemErrors":
				result = reportedProblemErrors(conn, Integer.valueOf(cohortId), filter);  
				break;
    		
    		case "getSessionProblems":
				result = sessionProblems(conn, Integer.valueOf(cohortId), filter);  
				break;

    		case "getProblemHistoryErrorData":
				result = getProblemHistoryErrorData(conn, Integer.valueOf(cohortId), filter);  
				break;
				
    		case "updateProblemHistoryErrorData":
				result = updateProblemHistoryErrorData(conn, Integer.valueOf(cohortId), filter);  
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
    		case "adminCohortInfo":
    			result = adminCohortInfo(conn, Integer.valueOf(cohortId),filter);
    			break;
    		case "adminCohortTeachers":
    			result = adminCohortTeachers(conn, Integer.valueOf(cohortId),filter);
    			break;
    		case "adminCohortClasses":
    			result = adminCohortClasses(conn, Integer.valueOf(cohortId),filter);
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
    	
    	int result = 0;
        PreparedStatement stmt_del = null;
        try {
            String q_del = "delete from teacher_log_slices where cohortid = ?;";
            stmt_del = conn.prepareStatement(q_del);
            stmt_del.setInt(1, cohortId);          
            result = stmt_del.executeUpdate();
        	stmt_del.close();
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        } finally {
            if (stmt_del != null)
                stmt_del.close();
        }    

        if (result == 0) {
        	return "SQL Insert error"; 
        }

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

    public int getClassProblemsTimeToSolve(Connection conn, int classId, Timestamp tsFromDate, Timestamp tsToDate) throws SQLException {

    	int result = 0;
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "Select sh.problemBeginTime, sum(sh.timeToSolve) AS timeToSolve from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=? and sh.isSolved = 1 and sh.mode != 'demo' and sh.problemBeginTime BETWEEN ? AND ? ;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            stmt.setTimestamp(2, tsFromDate);
            stmt.setTimestamp(3, tsToDate);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	result = rs.getInt("timeToSolve");            	
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


    
    public int getDistinctStudentsWhoSolvedProblems(Connection conn, int teacherId) throws SQLException {
    	int result = 0;
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
        	String q = "select count(distinct studId) AS studs from teacher as tch, class as cls, student s, studentproblemhistory sh where s.id=sh.studId and teacherId = ? and s.classId = cls.id and cls.teacherId = teacherId  and sh.isSolved = 1 and sh.mode != 'demo' ;";
        	//String q = "Select sh.problemBeginTime, count(problemId) AS noOfProblemsSolved from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=? and sh.isSolved = 1 and sh.mode != 'demo' and sh.problemBeginTime BETWEEN ? AND ? ;";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, teacherId);
            //stmt.setTimestamp(2, tsFromDate);
            //stmt.setTimestamp(3, tsToDate);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	result = rs.getInt("studs");
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
    
    String effortLabels[] = {"SOF","ATT","SHINT","SHELP", "GUESS","NOTR","SKIP","GIVEUP","NODATA"};
    
    public String getClassProblemsEffort(Connection conn, int classId, String targetEffort) throws SQLException {
    	String result = "";
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {           
        	String q = "Select count(sh.effort) as effort from student s,studentproblemhistory sh where s.id=sh.studId and s.classId = ? and sh.mode != 'demo' and sh.effort = ?;";
        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            stmt.setString(2, targetEffort);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	result = rs.getString("effort");
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
  
    public String getClassProblemsEffortSet(Connection conn, int classId) throws SQLException {

    	String effortValues = "";
    	for(int i=0;i<effortLabels.length;i++) {
    		if (effortValues.length() > 0) {
    			effortValues += ",";
    		}
    		effortValues += getClassProblemsEffort(conn, classId, effortLabels[i]);
		}
    	//System.out.println(effortValues);
    	return effortValues;
    }

    
    
    public String getClassProblemsEffortSlice(Connection conn, int classId, String targetEffort, Timestamp tsFromDate, Timestamp tsToDate) throws SQLException {
    	String result = "";
    	
    	ResultSet rs = null;
        PreparedStatement stmt = null;
        try {           
        	String q = "Select count(sh.effort) as effort from student s,studentproblemhistory sh where s.id=sh.studId and s.classId = ? and sh.mode != 'demo' and sh.effort = ? and sh.problemBeginTime BETWEEN ? AND ?;";
        	stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            stmt.setString(2, targetEffort);
            stmt.setTimestamp(3, tsFromDate);
            stmt.setTimestamp(4, tsToDate);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	result = rs.getString("effort");
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
    
    public String getClassProblemsEffortSetSlice(Connection conn, int classId, Timestamp tsFromDate, Timestamp tsToDate) throws SQLException {

    	String effortValues = "";
    	for(int i=0;i<effortLabels.length;i++) {
    		if (effortValues.length() > 0) {
    			effortValues += ",";
    		}
    		effortValues += getClassProblemsEffortSlice(conn, classId, effortLabels[i], tsFromDate, tsToDate);
		}
    	//System.out.println(effortValues);
    	return effortValues;
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


    public String insertCohorClassStudentSlice(Connection conn, int cohortId, int teacherId, String uname, String teacherFullname, int classId, String className, Timestamp week_startdate, int week_nbr, int nbr_problems_solved, int nbr_problems_seen, int time_problems_solved, int nbr_active_students, String effort) throws SQLException {
    	
    	
    	String[] efforts = effort.split(",");
    	int result = 0;
    	String resultMessage = "Success";
        PreparedStatement stmt = null;
        try {
            String q = "INSERT into teacher_class_slices (cohortId,teacherId,teacherUsername,teacherFullname,classId, className, week_startdate, week_nbr, nbr_problems_solved,nbr_problems_seen,time_problems_solved,nbr_active_students, SOF, ATT, SHINT, SHELP, GUESS, NOTR, SKIP, GIVEUP, NODATA) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, cohortId);
            stmt.setInt(2, teacherId);
            stmt.setString(3,uname);
            stmt.setString(4,teacherFullname);
            stmt.setInt(5, classId);
            stmt.setString(6, className);
            stmt.setTimestamp(7, week_startdate);
            stmt.setInt(8, week_nbr);
            stmt.setInt(9, nbr_problems_solved);
            stmt.setInt(10, nbr_problems_seen);
            stmt.setInt(11, time_problems_solved);
            stmt.setInt(12, nbr_active_students);
            stmt.setInt(13, Integer.valueOf(efforts[0]));
            stmt.setInt(14, Integer.valueOf(efforts[1]));
            stmt.setInt(15, Integer.valueOf(efforts[2]));
            stmt.setInt(16, Integer.valueOf(efforts[3]));
            stmt.setInt(17, Integer.valueOf(efforts[4]));
            stmt.setInt(18, Integer.valueOf(efforts[5]));
            stmt.setInt(19, Integer.valueOf(efforts[6]));
            stmt.setInt(20, Integer.valueOf(efforts[7]));
            stmt.setInt(21, Integer.valueOf(efforts[8]));
           
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
    	

    	int result = 0;
        PreparedStatement stmt_del = null;
        try {
            String q_del = "delete from teacher_class_slices where cohortid = ?;";
            stmt_del = conn.prepareStatement(q_del);
            stmt_del.setInt(1, cohortId);          
            result = stmt_del.executeUpdate();
        	stmt_del.close();
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        } finally {
            if (stmt_del != null)
                stmt_del.close();
        }    

        if (result == 0) {
        	return "SQL Insert error"; 
        }
    	
    	String[] splitter = filter.split("~");
    	
    	String startDate = splitter[0];
    	int intDuration = Integer.valueOf(splitter[1]);
    	int weeksToReport = Integer.valueOf(splitter[2]);

        String teacherClasses = getCohortTeachersClassIds(cohortId);
        
        String[] teacherClassSplitter  = teacherClasses.split(",");
        
        for (int tc=0;tc<teacherClassSplitter.length;tc++) {
        	
        	String teacherClassStr = teacherClassSplitter[tc];
        	String[] tcSplitter = teacherClassStr.split("~");
        	int teacherId = Integer.valueOf(tcSplitter[0]);
        	String teacherUsername = tcSplitter[1];
        	String teacherFullname = tcSplitter[2];
        	int classId = Integer.valueOf(tcSplitter[3]);
        	String className = tcSplitter[4];

        	Timestamp tsFromDate = convertStartDate(startDate);
            Timestamp tsToDate = xDaysFromStartDate(tsFromDate,intDuration);
            
        	
	    	for (int week_nbr = 1;week_nbr<=weeksToReport;week_nbr++) {
               	int nbr_problems_seen = getClassProblemsSeen(conn, classId, tsFromDate,tsToDate);
               	int nbr_problems_solved = getClassProblemsSolved(conn, classId, tsFromDate,tsToDate);
               	int time_problems_solved = getClassProblemsTimeToSolve(conn, classId, tsFromDate,tsToDate);
               	int nbr_active_students = getClassActiveStudentCount(conn, classId, tsFromDate,tsToDate);
               	String effort = getClassProblemsEffortSetSlice(conn, classId, tsFromDate,tsToDate);
//               	if ((nbr_problems_seen + nbr_problems_solved) > 0) {
               		insertCohorClassStudentSlice(conn, cohortId, teacherId, teacherUsername, teacherFullname, classId, className, tsFromDate, week_nbr, nbr_problems_solved,nbr_problems_seen,time_problems_solved,nbr_active_students, effort);
//               	}
	            tsFromDate=xDaysFromStartDate(tsFromDate,intDuration);    	
		        tsToDate=xDaysFromStartDate(tsFromDate,intDuration);    	

	    	}
    	}

        return "Class Slices Updated Successfully"; 
    }
    

    public String adminCohortInfo(Connection conn, int cohortId, String filter) throws SQLException {
    	

    	String[] splitter = filter.split("~");
    	
    	String startDate = splitter[0];
    	
    	return "adminCohortInfo Status";
    	
    }

    public String adminCohortTeachers(Connection conn, int cohortId, String filter) throws SQLException {
    	
    	System.out.println("filter = " + filter);
    	String[] splitter = filter.split("~");
    	
    	String cmd = splitter[0];
    	int tid = Integer.valueOf(splitter[1]);
    	String lname = splitter[2];

    	String result = "";
    	
    	ResultSet rs1 = null;
        PreparedStatement stmt1 = null;
    	ResultSet rs2 = null;
        PreparedStatement stmt2 = null;
    	if (cmd.equals("add")) {
    		try {
    	
	            String q1 = "select tch.ID, tch.lname from teacher as tch, teacher_map_cohorts as tmc where tmc.researchcohortid = ? and not tch.ID = tmc.teacherId and tch.ID = ? and tch.lname = ? ;";
	            stmt1 = conn.prepareStatement(q1);
	            stmt1.setInt(1, cohortId);
	            stmt1.setInt(2, tid);
	            stmt1.setString(3, lname);
	            rs1 = stmt1.executeQuery();
	            if (rs1.next()) {
	            	if (!rs1.getString("tch.lname").equals(lname)) {
	            		result  = "Teacher id found, but last name does not match";
	            	}
	            	else {
	            		result  = "Cannot add teacher";
	            	}
	            }
	        	stmt1.close();
	            rs1.close();
	        } finally {
	            if (stmt1 != null)
	                stmt1.close();
	            if (rs1 != null)
	                rs1.close();
	        }
    		if (result.length() == 0) {
		        try {
		            String q2 = "insert into teacher_map_cohorts (researchcohortid, teacherid, expectedStudentCount) values (?,?,?);";
		            stmt2 = conn.prepareStatement(q2);
		            stmt2.setInt(1, cohortId);
		            stmt2.setInt(2, tid);
		            stmt2.setInt(3, 0);
		            int sqlresult = stmt2.executeUpdate();
		            
		            if (sqlresult == 0) {
		            	result = "SQL error - Teacher not added"; 
		            }
		            else {
		            	result = "Teacher added to cohort";
		            }
	        	
		        	stmt2.close();
		            return result;    	
		        } finally {
		            if (stmt2 != null)
		                stmt2.close();
		        }
        	}
    	}
        if (cmd.equals("remove")) {
    		try {
    	    	
	            String q1 = "select tch.ID, tch.lname from teacher as tch, teacher_map_cohorts as tmc where tmc.researchcohortid = ? and tch.ID = tmc.teacherId and tch.ID = ? and tch.lname = ? ;";
	            stmt1 = conn.prepareStatement(q1);
	            stmt1.setInt(1, cohortId);
	            stmt1.setInt(2, tid);
	            stmt1.setString(3, lname);
	            rs1 = stmt1.executeQuery();
	            if (rs1.next()) {
	            	if (!rs1.getString("tch.lname").equals(lname)) {
	            		result  = "Teacher id found, but last name does not match";
	            	}

	            }
	        	stmt1.close();
	            rs1.close();
	        } finally {
	            if (stmt1 != null)
	                stmt1.close();
	            if (rs1 != null)
	                rs1.close();
	        }
    		if (result.length() == 0) {
		        try {
		            String q2 = "delete from teacher_map_cohorts where researchcohortid = ? and teacherid = ?;";
		            stmt2 = conn.prepareStatement(q2);
		            stmt2.setInt(1, cohortId);
		            stmt2.setInt(2, tid);
		            int sqlresult = stmt2.executeUpdate();
		            
		            if (sqlresult == 0) {
		            	result = "SQL error - Teacher not removed"; 
		            }
		            else {
		            	result = "Teacher removed from cohort";
		            }
	        	
		        	stmt2.close();
		            return result;    	
		        } finally {
		            if (stmt2 != null)
		                stmt2.close();
		        }
        	}
    	}
    	
    	return result;
    	
    }
    public String adminCohortClasses(Connection conn, int cohortId, String filter) throws SQLException {
    	

    	System.out.println("filter = " + filter);
    	String[] splitter = filter.split("~");
    	
    	String cmd = splitter[0];
    	int cid = Integer.valueOf(splitter[1]);
    	String lname = splitter[2];

    	String result = "";
    	
    	ResultSet rs1 = null;
        PreparedStatement stmt1 = null;
    	ResultSet rs2 = null;
        PreparedStatement stmt2 = null;
        
    	if (cmd.equals("add")) {
    		try {
	            String q1 = "select cls.id, tch.lname from class as cls, teacher as tch, class_map_cohorts as cmc where cmc.researchcohortid = ? and cls.id = cmc.classid and cls.id = ? and tch.ID = cls.teacherId";
	            stmt1 = conn.prepareStatement(q1);
	            stmt1.setInt(1, cohortId);
	            stmt1.setInt(2, cid);
	            rs1 = stmt1.executeQuery();
	            if (rs1.next()) {
	            	if (!rs1.getString("tch.lname").equals(lname)) {
	            		result  = "Class id found, but last name does not match";
	            	}
	            	else {
	            		result  = "Cannot add class";
	            	}
	            }
	        	stmt1.close();
	            rs1.close();
	        } finally {
	            if (stmt1 != null)
	                stmt1.close();
	            if (rs1 != null)
	                rs1.close();
	        }
    		if (result.length() == 0) {
		        try {
		            String q2 = "insert into class_map_cohorts (researchcohortid, classid) values (?,?);";
		            stmt2 = conn.prepareStatement(q2);
		            stmt2.setInt(1, cohortId);
		            stmt2.setInt(2, cid);
		            int sqlresult = stmt2.executeUpdate();
		            
		            if (sqlresult == 0) {
		            	result = "SQL error - class not added"; 
		            }
		            else {
		            	result = "Class added to cohort";
		            }
	        	
		        	stmt2.close();
		            return result;    	
		        } finally {
		            if (stmt2 != null)
		                stmt2.close();
		        }
        	}
    	}
        if (cmd.equals("remove")) {
    		try {
    	    	
	            String q1 = "select cls.id, tch.lname from class as cls, teacher as tch, class_map_cohorts as cmc where cmc.researchcohortid = ? and cls.id = cmc.classid and cls.id = ? and tch.ID = cls.teacherId";
	            stmt1 = conn.prepareStatement(q1);
	            stmt1.setInt(1, cohortId);
	            stmt1.setInt(2, cid);
	            rs1 = stmt1.executeQuery();
	            if (!rs1.next()) {
            		result  = "Class id not found";
	            }
    			else {
    				if (!rs1.getString("tch.lname").equals(lname)) {
    					result  = "Class id not found, but teacher name does not match" ;
    				}
    			}
	        	stmt1.close();
	            rs1.close();
	        } finally {
	            if (stmt1 != null)
	                stmt1.close();
	            if (rs1 != null)
	                rs1.close();
	        }
    		if (result.length() == 0) {
		        try {
		            String q2 = "delete from class_map_cohorts where researchcohortid = ? and classid = ?;";
		            stmt2 = conn.prepareStatement(q2);
		            stmt2.setInt(1, cohortId);
		            stmt2.setInt(2, cid);
		            int sqlresult = stmt2.executeUpdate();
		            
		            if (sqlresult == 0) {
		            	result = "SQL error - class not removed"; 
		            }
		            else {
		            	result = "Class removed from cohort";
		            }
	        	
		        	stmt2.close();
		            return result;    	
		        } finally {
		            if (stmt2 != null)
		                stmt2.close();
		        }
        	}
    	}

    	return result;
    	
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