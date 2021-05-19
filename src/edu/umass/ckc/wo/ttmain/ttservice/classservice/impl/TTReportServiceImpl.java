package edu.umass.ckc.wo.ttmain.ttservice.classservice.impl;

import edu.umass.ckc.wo.beans.StudentDetails;
import edu.umass.ckc.wo.beans.SurveyQuestionDetails;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.login.PasswordAuthentication;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.TeacherLogEntry;
import edu.umass.ckc.wo.ttmain.ttmodel.TeacherListEntry;
import edu.umass.ckc.wo.ttmain.ttmodel.TeacherClassListEntry;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassLandingReportStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.datamapper.ClassLandingReportStudentsMapper;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassLandingReportEvents;
import edu.umass.ckc.wo.ttmain.ttmodel.datamapper.ClassLandingReportEventsMapper;
import edu.umass.ckc.wo.ttmain.ttmodel.datamapper.ClassStudentsMapper;
import edu.umass.ckc.wo.ttmain.ttmodel.datamapper.TeacherLogEntryMapper;
import edu.umass.ckc.wo.ttmain.ttmodel.datamapper.TeacherListEntryMapper;
import edu.umass.ckc.wo.ttmain.ttmodel.datamapper.TeacherClassListEntryMapper;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTReportService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TTUtil;
import edu.umass.ckc.wo.ttmain.ttservice.util.TeacherLogger;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.studmod.StudentProblemHistory;
import edu.umass.ckc.wo.util.StringUtils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
/**
 * Created by nsmenon on 5/19/2017.
 * 
 * Frank 	10-15-19	Issue #7 perStudentperProblemReport report
 * Frank 	10-22-19	Issue #14 remove debugging
 * Frank 	11-25-19	Issue #13 add standards filter for per student per problem report
 * Frank	12-21-19	Issue #21 this file is being re-released with issue 21 to correct EOL characters which were inadvertently changed to unix style
 *						  The entire file should be replaced during 'pull request & comparison' process.
 * Frank 	01-14-20	Issue #45 & #21 add log report
 * Frank    03-02-2020  Added teacherList case: 
 * Frank    04-30-2020  Issue #96 missing locale 
 * Frank 	06-17-20	Issue #149
 * Frank	07-08-20	issue #153 added access code checker
 * Frank	07-28-20	issue #74 valid classId is valid or this teacherId
 * Frank	08-15-20	Issue #148 added time period (days) filter for perStudentPerProblemSet report
 * FRank	10-27-20	Issue #149R2 report_type parameter sdded
 * Frank	11-12-20	Issue #276 suppress logging when logged in as Master
 * Frank	11-12-20	Issue #299 Class Landing Page report
 * Frank	11-23-20	Issue #148R3 add lastXdays filter to perCluster Report
 * Frank	12-03-20	fix xdays computation to go back to just after midnight to include the entire day
 * Frank	12-03-20	Issue #388 Landing Report Two - by date range
 * Frank 	03-15-21  	Issue #398 New feature to move student from one class to another added
 * Frank 	03-22-21  	Issue #391 change date selection to BETWEEN date range 
 * Frank 	03-31-21  	Issue #418 add student selection filter for to perStudentPerProblem report 
 * Frank 	03-31-21  	Issue #418R4 add paging, padding -->
 * Frank 	04-30-21  	Fix filter parsing
 *  Frank 	04-30-21  	Send problem nickname to perStudentPerproblem report
 *  Frank 	05-11-21  	Issue #463 add report filters
 */


@Service
public class TTReportServiceImpl implements TTReportService {
    @Autowired
    private TTConfiguration connection;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired
    private TeacherLogger tLogger;
    
    private static Logger logger = Logger.getLogger(TTReportServiceImpl.class);
	private ResourceBundle rb = null;
	private String showNames = "Y";
	private String selectedStudent = "";
	private Locale ploc;

    @Override
    public String generateTeacherReport(String teacherId, String classId, String reportType, String lang, String filter, String teacherLoginType) throws TTCustomException {

        try {
        	
    		// Multi=lingual enhancement

        	Locale loc = new Locale("en","US");	
        	if (lang.substring(0,2).equals("es")) {
        		loc = new Locale("es","AR");	
        	}        	
    		ploc = loc;
    		rb = ResourceBundle.getBundle("MathSpring",loc);

        	// Hack - using classId to hold 'Target teacher ID' for teacher activities reports so use URL param not session variable
        	if ( (!reportType.equals("teacherList")) && (!reportType.equals("perTeacherReport")) ) {
        		try {
    	    		if (!DbClass.validateClassTeacher(connection.getConnection(),Integer.valueOf(classId),Integer.valueOf(teacherId))) {
    	    			return ("FAIL - Invalid Request");
    	    		}
        		}
        		catch(SQLException e) {
        			
        		}
        	}
        	// end Hack


    		
        	switch (reportType) {
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

            		if ("Normal".equals(teacherLoginType)) {
	                	try {
	               			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, classId, rb.getString(reportType), logMsg);
	                	}
	                	catch (Exception e) {
	                		System.out.println("TeacherLogger error " + e.getMessage());
	                	}
            		}
                    List<ClassStudents> classStudents = generateClassReportPerStudent(teacherId, classId, filter);
                    String[][] levelOneData = classStudents.stream().map(classStudents1 -> new String[]{classStudents1.getStudentId(), classStudents1.getStudentName(), classStudents1.getUserName(), classStudents1.getNoOfProblems()}).toArray(String[][]::new);
                    Map<String, String> studentIdMap = classStudents.stream().collect(Collectors.toMap(studMap -> studMap.getStudentId(), studMap -> studMap.getNoOfProblems()));
                    Map<String, Map<String, List<String>>> effortValues = generateEfortMapValues(studentIdMap, classId, filter);
                    Map<String, List<Document>> genemotioMap   = generateEmotionMapValues(studentIdMap);
                    Map<String,Map<String,int[]>> fullstudentEmotionsMap = new HashMap<>();
                    Map<String,Map<String,List<String>>> fullstudentEmotionsComments = new HashMap<>();
                    String[] barLabels = {rb.getString("not_at_all"), rb.getString("a_little"), rb.getString("somewhat"), rb.getString("quite_a_bit"), rb.getString("extremely")};
                    genemotioMap.forEach((studentId, xmlDocumentList) -> {
                        Map<String,List<String>> eachEmotionMap = new HashMap<>();
                        int[] frustrationValues = new int[6];
                        List<String> frustrationComments = null;
                        int[] confidenceValues = new int[6];
                        List<String> confidenceComments = null;
                        int[] excitementValues = new int[6];
                        List<String> excitementComments = null;
                        int[] interestValues = new int[6];
                        List<String> interestComments = null;
                        
                        Integer frustrationCount=0,confidenceCount=0,excitementCount=0,interestCount = 0;
                        Map<String,int[]> emotionsValuesMap = new HashMap<>();
                        for (Document doc : xmlDocumentList) {
                            Node emotionNode = doc.getFirstChild().getFirstChild();
                            Element emotionElement = (Element)emotionNode;
                            String emotion = emotionElement.getAttribute("name");
                            String emotionLevel = emotionElement.getAttribute("level");
                            int integerValue;
                            switch(emotion){
                                case "Frustration":
                                    frustrationCount++;
                                    integerValue = Integer.valueOf(emotionLevel);
                                    frustrationValues[integerValue - 1] = frustrationValues[integerValue - 1] + 1;
                                    frustrationValues[5] = frustrationCount;
                                    emotionsValuesMap.put("Frustration", frustrationValues);
                                    String studentCommentsFrustration = getCharacterDataFromElement(emotionElement);
                                    if(!"".equals(studentCommentsFrustration))
                                        studentCommentsFrustration = barLabels[integerValue-1]+": "+studentCommentsFrustration;
                                    if (frustrationComments == null) {
                                            frustrationComments = new ArrayList<>();
                                    }
                                    frustrationComments.add(studentCommentsFrustration);
                                    eachEmotionMap.put("Frustration",frustrationComments);
                                    break;

                                case "Confidence":
                                    confidenceCount++;
                                    integerValue = Integer.valueOf(emotionLevel);
                                    confidenceValues[integerValue - 1] = confidenceValues[integerValue - 1] + 1;
                                    confidenceValues[5] = confidenceCount;
                                    emotionsValuesMap.put("Confidence", confidenceValues);
                                    String studentCommentsConfidence = getCharacterDataFromElement(emotionElement);
                                    if(!"".equals(studentCommentsConfidence))
                                        studentCommentsConfidence = barLabels[integerValue-1]+": "+studentCommentsConfidence;
                                    if (confidenceComments == null) {
                                        confidenceComments = new ArrayList<>();
                                    }
                                    confidenceComments.add(studentCommentsConfidence);
                                    eachEmotionMap.put("Confidence", confidenceComments);
                                    break;

                                case "Excitement":
                                    excitementCount++;
                                    integerValue = Integer.valueOf(emotionLevel);
                                    excitementValues[integerValue - 1] = excitementValues[integerValue - 1] + 1;
                                    excitementValues[5] = excitementCount;
                                    emotionsValuesMap.put("Excitement", frustrationValues);
                                    String studentCommentsExcitement = getCharacterDataFromElement(emotionElement);
                                    if(!"".equals(studentCommentsExcitement))
                                        studentCommentsExcitement = barLabels[integerValue-1]+": "+studentCommentsExcitement;
                                    if (excitementComments == null) {
                                        excitementComments = new ArrayList<>();
                                    }
                                    excitementComments.add(studentCommentsExcitement);
                                    eachEmotionMap.put("Excitement", excitementComments);
                                    break;

                                case "Interest":
                                    interestCount++;
                                    integerValue = Integer.valueOf(emotionLevel);
                                    interestValues[integerValue - 1] = interestValues[integerValue - 1] + 1;
                                    interestValues[5] = interestCount;
                                    emotionsValuesMap.put("Interest", interestValues);
                                    String studentCommentsInterest = getCharacterDataFromElement(emotionElement);
                                    if(!"".equals(studentCommentsInterest))
                                        studentCommentsInterest = barLabels[integerValue-1]+": "+studentCommentsInterest;

                                    if (interestComments == null) {
                                        interestComments = new ArrayList<>();
                                    }
                                    interestComments.add(studentCommentsInterest);
                                    eachEmotionMap.put("Interest", interestComments);
                                    break;
                            }

                        }
                        fullstudentEmotionsComments.put(studentId,eachEmotionMap);
                        fullstudentEmotionsMap.put(studentId,emotionsValuesMap);
                    });
                	
                    ObjectMapper objMapper = new ObjectMapper();
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("levelOneData", levelOneData);
                    dataMap.put("effortChartValues", effortValues.get("effortMap"));
                    dataMap.put("eachStudentDataValues", effortValues);
                    dataMap.put("fullstudentEmotionsMap", fullstudentEmotionsMap);
                    dataMap.put("fullstudentEmotionsComments", fullstudentEmotionsComments);
                    return objMapper.writeValueAsString(dataMap);

                case "perProblemReport":
            		if ("Normal".equals(teacherLoginType)) {
	                   	try {
	               			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, classId, reportType, "");
	                	}
	                	catch (Exception e) {
	                		System.out.println("TeacherLogger error " + e.getMessage());
	                	}
            		}
                    Map<String, PerProblemReportBean> resultBean = generatePerProblemReportForClass(classId);
                    ObjectMapper perStudentPerProblemReportMapper = new ObjectMapper();
                    Map<String, Object> dataMapper = new HashMap<>();
                    dataMapper.put("levelOneDataPerProblem", resultBean);
                    return perStudentPerProblemReportMapper.writeValueAsString(dataMapper);

                case "commonCoreClusterReport":
                    Map<String, PerClusterObjectBean> resultsPerStandard = generatePerCommonCoreClusterReport(teacherId, classId, filter, teacherLoginType);
                    ObjectMapper commonCoreClusterReportMapper = new ObjectMapper();
                    return commonCoreClusterReportMapper.writeValueAsString(resultsPerStandard);

                case "perStudentPerProblemSetReport":
                    Map<String, Object> result = generateClassReportPerStudentPerProblemSet(teacherId, classId, filter, teacherLoginType);
                    ObjectMapper perStudentPerProblemSetReport = new ObjectMapper();
                    return perStudentPerProblemSetReport.writeValueAsString(result);
                    
                case "perStudentPerProblemReport":
                    Map<String, Object> result2 = generateClassReportPerStudentPerProblem(teacherId, classId, filter, teacherLoginType);
                    ObjectMapper perStudentPerProblemReportMapper2 = new ObjectMapper();
                    return perStudentPerProblemReportMapper2.writeValueAsString(result2);
                    
                case "summarySurveyReport":
            		if ("Normal".equals(teacherLoginType)) {
	                   	try {
	               			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, classId, rb.getString(reportType), "");
	                	}
	                	catch (Exception e) {
	                		System.out.println("TeacherLogger error " + e.getMessage());
	                	}
            		}
                	Map<String, Map<Integer, StudentDetails>> result1 = generateSurveyReport(classId);
                    ObjectMapper surveyReportMapper1 = new ObjectMapper();
                    return surveyReportMapper1.writeValueAsString(result1);

                case "classLandingReportOne":
                    List<ClassLandingReportStudents> landingReportOne_Students = generateClassLandingReportOne(teacherId, classId, filter);
                    String[][] landingReportOne_levelOneData = landingReportOne_Students.stream().map(classStudents1 -> new String[]{classStudents1.getStudentId(), classStudents1.getStudentName(), classStudents1.getUserName(), classStudents1.getNoOfProblems(), classStudents1.getTimeInMS(), classStudents1.getLatestLogin() }).toArray(String[][]::new) ;
                                    	
                    ObjectMapper landingReportOne_objMapper = new ObjectMapper();
                    Map<String, Object> landingReportOne_dataMap = new HashMap<>();
                    landingReportOne_dataMap.put("levelOneData", landingReportOne_levelOneData);
                    return landingReportOne_objMapper.writeValueAsString(landingReportOne_dataMap);
                    
                case "classLandingReportTwo":
                    List<ClassLandingReportStudents> landingReportTwo_Students = generateClassLandingReportTwo(teacherId, classId, filter);
                    String[][] landingReportTwo_levelOneData = landingReportTwo_Students.stream().map(classStudents1 -> new String[]{classStudents1.getStudentId(), classStudents1.getStudentName(), classStudents1.getUserName(), classStudents1.getNoOfProblems(), classStudents1.getTimeInMS(), classStudents1.getLatestLogin() }).toArray(String[][]::new) ;
                                    	
                    ObjectMapper landingReportTwo_objMapper = new ObjectMapper();
                    Map<String, Object> landingReportTwo_dataMap = new HashMap<>();
                    landingReportTwo_dataMap.put("levelOneData", landingReportTwo_levelOneData);
                    return landingReportTwo_objMapper.writeValueAsString(landingReportTwo_dataMap);

                case "perTeacherReport":
                	// Note: classId parameter is used to communicate target teacherId for this report only
                	if ("Normal".equals(teacherLoginType)) {
	                   	try {
	                   		String logmsg = "{ \"teacherId\" : \"" + classId + "\" }";
	               			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, "0", rb.getString("view_teacher_activities"), logmsg);
	                	}
	                	catch (Exception e) {
	                		System.out.println("TeacherLogger error " + e.getMessage());
	                	}
                	}
                    List<TeacherLogEntry> TeacherLogEntries = generateTeacherLogReport(classId);
                    String[][] teacherData = TeacherLogEntries.stream().map(TeacherLogEntry1 -> new String[]{TeacherLogEntry1.getTimestampString(lang.substring(0,2)),TeacherLogEntry1.getTeacherId(), TeacherLogEntry1.getTeacherName(), TeacherLogEntry1.getUserName(), TeacherLogEntry1.getAction(), TeacherLogEntry1.getClassId(), TeacherLogEntry1.getActivityName(reportType)}).toArray(String[][]::new);
                    ObjectMapper teacherMapper = new ObjectMapper();
                    Map<String, Object> teacherMap = new HashMap<>();
                    teacherMap.put("levelOneData", teacherData);
                    return teacherMapper.writeValueAsString(teacherMap);
                case "teacherList":
                	try {
                		int test = DbTeacher.getTeacherId(connection.getConnection(), "SuperFly", filter);
                		if (test == -1) {
                			return "InvalidAccessCode";
                		}
                		else {
    	                	// Note: classId parameter is used to communicate target teacherId for this report only
	                		List<TeacherListEntry> TeacherListEntries = generateTeacherList(classId);
		                    String[][] tchData = TeacherListEntries.stream().map(TeacherLogEntry1 -> new String[]{TeacherLogEntry1.getTeacherId(), TeacherLogEntry1.getUserName()}).toArray(String[][]::new);
		                    ObjectMapper tchMapper = new ObjectMapper();
		                    Map<String, Object> tchMap = new HashMap<>();
		                    tchMap.put("levelOneData", tchData);
		                    return tchMapper.writeValueAsString(tchMap);       
                		}
                		
                	}
                	catch (Exception e) {
            			return "Unexpected Error";
                	}
                case "teacherClassList":

                	// Note: classId parameter is used to communicate target teacherId for this report only
            		List<TeacherClassListEntry> ClassListEntries = generateTeacherClassList(teacherId);
                    String[][] clsData = ClassListEntries.stream().map(TeacherLogEntry1 -> new String[]{TeacherLogEntry1.getTeacherId(), TeacherLogEntry1.getClassName(),TeacherLogEntry1.getClassId()}).toArray(String[][]::new);
                    ObjectMapper clsMapper = new ObjectMapper();
                    Map<String, Object> clsMap = new HashMap<>();
                    clsMap.put("levelOneData", clsData);
                    return clsMapper.writeValueAsString(clsMap);       


        	}
        } catch (IOException e) {
           logger.error(e.getMessage());
           throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
        }
        catch (MissingResourceException e) {
        	logger.error(e.getMessage());
        	throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
        }
    	System.out.println("Unknown report type: " + reportType);
        return null;
    }

    
    @Override
    public Map<String, Map<Integer, StudentDetails>> generateSurveyReport(String classId){
		
    	Map<String, Map<Integer, StudentDetails>> surveyMap = new HashMap<>();
    	SqlParameterSource namedParameters = new MapSqlParameterSource("classId", classId);
    	namedParameterJdbcTemplate.query(TTUtil.SUM_SUR_REPORT, namedParameters, (ResultSet mappedrow) -> {
            while (mappedrow.next()) {
            	
            	String surveyName = mappedrow.getString("surveyName");
            	
            	Integer studentId = mappedrow.getInt("studentId");
            	
            	Map<Integer,StudentDetails> sDetails = surveyMap.get(surveyName); 
            	if(sDetails!=null) {
            	
            		StudentDetails sDetail = sDetails.get(studentId);
            		
                  	 SurveyQuestionDetails sqd = getSurveyQuestionDetails(mappedrow);
                  	            		
            		if(sDetail!=null) {
            		
                   	 	sDetail.getQuestionset().add(sqd);
                   	 
            		} else {
            			
            			
            			sDetail = getStudentDetail(mappedrow, studentId, sqd);
                    	
                    	sDetails.put(studentId, sDetail);
                    	
                    	surveyMap.put(surveyName, sDetails);
            		}
            	} else {
            		
            		SurveyQuestionDetails sqd = getSurveyQuestionDetails(mappedrow);
            		StudentDetails sDetail = getStudentDetail(mappedrow, studentId, sqd);
                	
            		sDetails = new HashMap<>();
            		
                	sDetails.put(studentId, sDetail);
                	
                	surveyMap.put(surveyName, sDetails);
            	}
            	
             }
          return surveyMap;
        });
    	
    	
    	return surveyMap;
    	
    }
    
    private StudentDetails getStudentDetail(ResultSet mappedrow, Integer studentId, SurveyQuestionDetails sqd) throws SQLException {
    	
    	String studentName = mappedrow.getString("studentName");
    	String studentUserName = mappedrow.getString("userName");
    	Integer studentPedagogyId = mappedrow.getInt("pedagogyId");
    	Integer studentAge = mappedrow.getInt("age");
    	String studentGender = mappedrow.getString("gender");
    	Set<SurveyQuestionDetails> questionset = new HashSet<>();
    	
    	questionset.add(sqd);
    	
    	StudentDetails sDetail = new StudentDetails();
    	sDetail.setStudentId(studentId);
    	sDetail.setStudentName(studentName);
    	sDetail.setStudentUserName(studentUserName);
    	sDetail.setStudentGender(studentGender);
    	sDetail.setStudentAge(studentAge);
    	sDetail.setStudentPedagogyId(studentPedagogyId);
    	sDetail.setQuestionset(questionset);
    	
    	return sDetail;
    }

    private SurveyQuestionDetails getSurveyQuestionDetails(ResultSet mappedrow) throws SQLException {
    	
    	String questionName = mappedrow.getString("questionName");
     	 String description = mappedrow.getString("description");
     	 Integer problemSet = mappedrow.getInt("problemSet");
     	 Integer ansType = mappedrow.getInt("ansType");
     	 Integer skipped = mappedrow.getInt("skipped");
     	 String studentAnswer = mappedrow.getString("studentAnswer");
     	 
     	             	 
     	 SurveyQuestionDetails sqd = new SurveyQuestionDetails();
     	 sqd.setQuestionName(questionName);
     	 sqd.setDescription(description);
     	 sqd.setProblemSet(problemSet);
     	 sqd.setAnsType(ansType);
     	 sqd.setSkipped(skipped);
     	 sqd.setStudentAnswer(studentAnswer);
     	 
		return sqd;
	}

	@Override
    public Map<String, List<String[]>> generateEmotionsReportForDownload(String teacherId, String classId, String filter) throws TTCustomException {

    	String filters[] = filter.split("~");
    	String modFilter = "%";
    	String selectedStudent = "";
    	
    	if (filters.length > 0) {
    		modFilter = filters[0].trim() + "%";
    	}
    	
   		
    	if (filters.length > 3) {
    		selectedStudent = filters[3].trim();
    	}
    	else {
    		selectedStudent = "";
    	}
    		
		
		List<ClassStudents> classStudents = generateClassReportPerStudent(teacherId, classId, filter);
        Map<String, List<String[]>> finalMapValues = new HashMap<>();
        classStudents.forEach( classStudent ->{
        	Timestamp tsFromDate = null;
        	Timestamp tsToDate = null;
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
        	}
        	else {
       			tsFromDate = defaultFromDate();
       			tsToDate = defaultToDate();    		    			    		
        	}

//       	SqlParameterSource namedParameters = new MapSqlParameterSource("studId", classStudent.getStudentId());
 
            Map<String, Object> params = new HashMap<String, Object>();
    
            params.put("studId", classStudent.getStudentId());
            params.put("tsFromDate", tsFromDate);
            params.put("tsToDate",tsToDate);
            SqlParameterSource namedParameters = new MapSqlParameterSource(params);        
                  	
        	
        	List<String[]> emotionReportValues =   namedParameterJdbcTemplate.query(TTUtil.EMOTION_REPORT_DOWNLOAD, namedParameters, (ResultSet mappedrow) -> {
                List<String[]> addList = new ArrayList<>();
                while (mappedrow.next()) {
                    String[] finalValues = new String[13];
                    finalValues[0] = (mappedrow.getString("studId"));
                    finalValues[1] = (mappedrow.getString("userName"));
                    finalValues[2] = (mappedrow.getString("problemId"));
                    finalValues[3] = (mappedrow.getString("curTopicId"));
                    finalValues[4] = (mappedrow.getString("description"));
                    finalValues[5] = (mappedrow.getString("time"));
                    finalValues[6] = (mappedrow.getString("name"));
                    finalValues[7] = (mappedrow.getString("nickname"));
                    finalValues[8] = (mappedrow.getString("standardID"));
                    finalValues[9] = (mappedrow.getString("diff_level"));
                    parseEmotionValues(mappedrow.getString("userInput"),finalValues);
                    addList.add(finalValues);
                }
                return addList;
            });
            finalMapValues.put(classStudent.getStudentId(),emotionReportValues);
        });
        return finalMapValues;
    }


    @Override
    public Map<String,PerClusterObjectBean> generatePerCommonCoreClusterReport(String teacherId, String classId, String filter, String teacherLoginType) {

       	     

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
    	}
    	else {
   			tsFromDate = defaultFromDate();
   			tsToDate = defaultToDate();    		    			    		
    	}

		
       	String logMsg = "{ \"dates\" : \"" +  filters[1].trim() + "\" }";     

		if ("Normal".equals(teacherLoginType)) {
	       	try {
	   			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, classId, rb.getString("commonCoreClusterReport"), logMsg);
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
		
		
    	Map<String,PerClusterObjectBean> completeDataMap = new LinkedHashMap<>();
//        SqlParameterSource namedParameters = new MapSqlParameterSource("classId", classId);

        Map<String, Object> params = new HashMap<String, Object>();
  //      params.put("filter", modFilter);
        params.put("classId", classId);
        params.put("tsFromDate", tsFromDate);
        params.put("tsToDate",tsToDate);
        SqlParameterSource namedParameters = new MapSqlParameterSource(params);        
        
        Map<String,PerClusterObjectBean> resultantValues = namedParameterJdbcTemplate.query(TTUtil.PER_STANDARD_QUERY_FIRST, namedParameters, (ResultSet mappedrow) -> {
            while (mappedrow.next()) {
                String clusterID = mappedrow.getString("clusterId");
                int noOfProblemsInCluster = mappedrow.getInt("noOfProblemsInCluster");
                int totalHintsViewedPerCluster = mappedrow.getInt("totalHintsViewedPerCluster");
                double avgtotalHintsViewedPerCluster =  Math.round(((double)totalHintsViewedPerCluster/(double)noOfProblemsInCluster)*100.0)/100.0;
                String categoryCode = mappedrow.getString("categoryCode");
                String clusterCCName = mappedrow.getString("clusterCCName");
                String displayName = mappedrow.getString("displayName");
                PerClusterObjectBean resultObj = new PerClusterObjectBean(clusterID,noOfProblemsInCluster,avgtotalHintsViewedPerCluster);
                resultObj.setClusterCCName(clusterCCName);
                resultObj.setCategoryCodeAndDisplayCode(categoryCode+" { "+displayName+" } ");
                completeDataMap.put(clusterID,resultObj);
             }
          return completeDataMap;
        });
        Map<String,PerClusterObjectBean> resultFirstAttemptValues = namedParameterJdbcTemplate.query(TTUtil.PER_STANDARD_QUERY_FOURTH, namedParameters, (ResultSet mappedrow) -> {
            while (mappedrow.next()) {
                String clusterID = mappedrow.getString("clusterId");
                int noOfTotalEfforts = mappedrow.getInt("totalSOFLogged");
                int noOfSOFLogged = mappedrow.getInt("totoaleffortlogged");
                PerClusterObjectBean result = completeDataMap.get(clusterID);
                int firstAttempt = (int)Math.round(100.0 / noOfSOFLogged * noOfTotalEfforts);
                result.setNoOfProblemsonFirstAttempt(firstAttempt);
            }
            return completeDataMap;
        });
        return completeDataMap;
    }


    @Override
    public Map<String, List<Document>> generateEmotionMapValues(Map<String, String> studentIds) throws TTCustomException {
        Map<String, List<Document>> studentEmotionMap = new HashMap<>();
        studentIds.forEach((studentId, noOfProblems) -> {
            SqlParameterSource namedParameters = new MapSqlParameterSource("studId", studentId);
            List<String> studentEmotions = namedParameterJdbcTemplate.query(TTUtil.EMOTION_REPORT, namedParameters, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    return resultSet.getString("userInput");
                }
            });
            List<Document> documentXmlEmotion = new ArrayList<>();
            try {
                if (studentEmotions.isEmpty()) {
                    documentXmlEmotion.add(parseXmlFromString("<interventionInput class='AskEmotionIS'><emotion name='NoEmotionReported' level='-1'><![CDATA[]]></emotion></interventionInput>"));
                } else {

                    for (String strEmo : studentEmotions)
                        documentXmlEmotion.add(parseXmlFromString(strEmo));
                }
            } catch (TTCustomException e) {
                logger.error(e.getErrorMessage());
                e.printStackTrace();
            }
            studentEmotionMap.put(studentId, documentXmlEmotion);
        });
        return studentEmotionMap;
    }

    private void parseEmotionValues(String userInput, String[] emotionMapValues) {
        try {
        Document doc = parseXmlFromString(userInput);
        Node emotionNode = doc.getFirstChild().getFirstChild();
        Element emotionElement = (Element) emotionNode;
        String emotion = emotionElement.getAttribute("name");
        String emotionLevel = emotionElement.getAttribute("level");
        String studentCommentsFrustration = getCharacterDataFromElement(emotionElement);
        emotionMapValues[10] = (emotion);
        emotionMapValues[11] = (emotionLevel);
        emotionMapValues[12] = (studentCommentsFrustration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Map<String, List<String>>> generateEfortMapValues(Map<String, String> studentIds, String classId, String filter) {

    	
    	String filters[] = filter.split("~");
    	String modFilter = "%";
    	
    	Map<String, Map<String, List<String>>> completeDataMap = new LinkedHashMap<>();
        Map<String, List<String>> effortValues = new LinkedHashMap<String, List<String>>();
        studentIds.forEach((studentId, noOfProblems) -> {
        	
        	Timestamp tsFromDate = null;
        	Timestamp tsToDate = null;


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
        	}
        	else {
       			tsFromDate = defaultFromDate();
       			tsToDate = defaultToDate();    		    			    		
        	}    	
            String[] effortvalues = new String[9];
            Integer noOfProb = Integer.valueOf(noOfProblems.trim());
            int SKIP = 0, NOTR = 0, GIVEUP = 0, SOF = 0, SHINT = 0, SHELP = 0, ATT = 0, GUESS = 0, NODATA = 0;
            Map<String, Object> selectParams = new LinkedHashMap<String, Object>();
            selectParams.put("classId", classId);
            selectParams.put("studId", studentId);
            selectParams.put("tsFromDate", tsFromDate);
            selectParams.put("tsToDate", tsToDate);

            
            List<String> studentEfforts = namedParameterJdbcTemplate.query(TTUtil.PER_STUDENT_QUERY_SECOND, selectParams, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    if ("".equals(resultSet.getString("effort")) || resultSet.getString("effort") == null)
                        return "";
                    return resultSet.getString("effort");
                }
            });

            Map<String, List<String>> perstudentRecords = namedParameterJdbcTemplate.query(TTUtil.PER_STUDENT_QUERY_SECOND, selectParams, (ResultSet mappedRow) -> {
                Map<String, List<String>> studentData = new LinkedHashMap<>();
                while (mappedRow.next()) {
                    List<String> studentRecordValues = new ArrayList<>();
                    studentRecordValues.add(mappedRow.getString("name"));
                    studentRecordValues.add(mappedRow.getString("nickname"));

                    if ("".equals(mappedRow.getString("statementHTML")) || mappedRow.getString("statementHTML") == null)
                        studentRecordValues.add("The problem does not have a description");
                    else
                        studentRecordValues.add(mappedRow.getString("statementHTML"));

                    studentRecordValues.add(mappedRow.getString("screenShotURL"));
                    studentRecordValues.add(mappedRow.getString("isSolved"));
                    studentRecordValues.add(mappedRow.getString("numMistakes"));
                    studentRecordValues.add(mappedRow.getString("numHints"));
                    studentRecordValues.add(mappedRow.getString("numAttemptsToSolve"));

                    if ("".equals(mappedRow.getString("effort")) || "unknown".equals(mappedRow.getString("effort")) || mappedRow.getString("effort") == null)
                        studentRecordValues.add("NO DATA");
                    else
                        studentRecordValues.add(mappedRow.getString("effort"));


                    studentRecordValues.add(mappedRow.getString("description"));


                    if ("".equals(mappedRow.getString("problemEndTime")) || mappedRow.getString("problemEndTime") == null)
                        studentRecordValues.add("Problem was not completed");
                    else
                        studentRecordValues.add(mappedRow.getString("problemEndTime"));

                    studentRecordValues.add(mappedRow.getString("problemId"));
                    studentRecordValues.add(mappedRow.getString("videoSeen"));
                    studentRecordValues.add(mappedRow.getString("exampleSeen"));
                    studentRecordValues.add(mappedRow.getString("standardID"));
                    studentRecordValues.add(mappedRow.getString("diff_level"));
                    studentRecordValues.add(mappedRow.getString("mastery"));
                    studentRecordValues.add(mappedRow.getString("topicId"));
                    studentRecordValues.add(mappedRow.getString("description"));
                    studentData.put(mappedRow.getString("id"), studentRecordValues);

                }

                return studentData;

            });

            // Calculate Effort Percentages
            for (String effortVal : studentEfforts) {
                switch (effortVal) {
                    case "SKIP":
                        SKIP++;
                        break;
                    case "NOTR":
                        NOTR++;
                        break;
                    case "GIVEUP":
                        GIVEUP++;
                        break;
                    case "SOF":
                        SOF++;
                        break;
                    case "ATT":
                        ATT++;
                        break;
                    case "GUESS":
                        GUESS++;
                        break;
                    case "SHINT":
                        SHINT++;
                        break;
                    case "SHELP":
                        SHELP++;
                        break;
                    case "NODATA":
                        NODATA++;
                        break;
                    default:
                        NODATA++;
                        break;
                }
            }
            effortvalues[0] = Double.toString((double) ((SKIP * 100) / noOfProb));
            effortvalues[1] = Double.toString((double) ((NOTR * 100) / noOfProb));
            effortvalues[2] = Double.toString((double) ((GIVEUP * 100) / noOfProb));
            effortvalues[3] = Double.toString((double) ((SOF * 100) / noOfProb));
            effortvalues[4] = Double.toString((double) ((ATT * 100) / noOfProb));
            effortvalues[5] = Double.toString((double) ((GUESS * 100) / noOfProb));
            effortvalues[6] = Double.toString((double) ((SHINT * 100) / noOfProb));
            effortvalues[7] = Double.toString((double) ((SHELP * 100) / noOfProb));
            effortvalues[8] = Double.toString((double) ((NODATA * 100) / noOfProb));

            effortValues.put(studentId, Arrays.asList(effortvalues));
            completeDataMap.put("effortMap", effortValues);
            completeDataMap.put(studentId, perstudentRecords);

        });

        return completeDataMap;
    }

    private Document parseXmlFromString(String xmlString) throws TTCustomException{
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));
            Document document = builder.parse(is);
            NodeList emotionNode = document.getElementsByTagName("emotion");
            return document;
        }catch (Exception excep){
            excep.printStackTrace();
            logger.error(excep.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.FAILED_TO_LOAD_PROBLEMS);
        }
    }

    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    @Override
    public List<ClassStudents> generateClassReportPerStudent(String teacherId, String classId, String filter) {
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
    	}
    	else {
   			tsFromDate = defaultFromDate();
   			tsToDate = defaultToDate();    		    			    		
    	}
    	if (filters.length > 3) {
    		selectedStudent = filters[3].trim();
    	}
    	else {
    		selectedStudent = "";
    	}
//         SqlParameterSource namedParameters = new MapSqlParameterSource("classId", classId);

         Map<String, Object> studentFirstParams = new HashMap<String, Object>();
         studentFirstParams.put("classId", classId);
         studentFirstParams.put("tsFromDate", tsFromDate);
         studentFirstParams.put("tsToDate", tsToDate);
         SqlParameterSource studentFirstParamsParameters = new MapSqlParameterSource(studentFirstParams);
         
         List<ClassStudents> classStudents = (List) namedParameterJdbcTemplate.query(TTUtil.PER_STUDENT_QUERY_FIRST, studentFirstParamsParameters, new ClassStudentsMapper());
                 
         if (selectedStudent.length() > 0) {
	         Iterator<ClassStudents> i = classStudents.iterator();
	         while (i.hasNext()) {
	        	 ClassStudents stud = i.next();
	        	 if (!stud.getStudentId().equals(selectedStudent)) {
	        		 i.remove();
	        	 }
	         }         
         }
         return classStudents;
    }

    @Override
    public List<ClassLandingReportStudents> generateClassLandingReportOne(String teacherId, String classId, String filter) {


    	Timestamp ts = xDaysAgoToDate(filter);
		
        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("classId", classId);
        eventParams.put("ts", ts);
        SqlParameterSource eventNamedParameters = new MapSqlParameterSource(eventParams);

        List<ClassLandingReportEvents> classLandingReportEvents = namedParameterJdbcTemplate.query(TTUtil.LANDING_REPORT_EVENTS_QUERY, eventNamedParameters, new ClassLandingReportEventsMapper());

        Map<String, String> probElapsedMap = new HashMap<String, String>();
        Map<String, String> latestLoginMap = new HashMap<String, String>();
        String prevStudent = "";
        int probElapsedTime = 0;
        String latestLogin = "";
        boolean countable = false;
        for (ClassLandingReportEvents event : classLandingReportEvents) {
        	switch (event.getAction()) {
        		case "EndProblem":
        			countable = true;
        			break;
        		case "Student Access":
            		latestLoginMap.put(event.getStudentId(), event.getTimestampString("en"));
        			break;
        		default:
        			countable = false;
        	}
        	if (event.getStudentId().equals(prevStudent)) {
            	if (countable) {
            		probElapsedTime += Integer.valueOf(event.getProbElapsed());
            	}
        	}
        	else {
            	if (prevStudent.length() > 0) {
//                	System.out.println("probElapsedTime = " + String.valueOf(probElapsedTime));
            		probElapsedMap.put(prevStudent, String.valueOf(probElapsedTime));
            		probElapsedTime = 0;
            	}
            	if (countable) {
            		probElapsedTime += Integer.valueOf(event.getProbElapsed());
            	}
          	}
           	prevStudent = event.getStudentId();
        }

    	if (prevStudent.length() > 0) {
//        	System.out.println("Last student - probElapsedTime = " + String.valueOf(probElapsedTime));
    		probElapsedMap.put(prevStudent, String.valueOf(probElapsedTime));
    	}
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        params.put("ts", ts);
        SqlParameterSource namedParameters = new MapSqlParameterSource(params);

        List<ClassLandingReportStudents> classLandingReportStudents = namedParameterJdbcTemplate.query(TTUtil.LANDING_REPORT_STUDENTS_QUERY, namedParameters, new ClassLandingReportStudentsMapper());
        
        for (ClassLandingReportStudents stu : classLandingReportStudents) {
        	//System.out.println("StudentID=" + stu.getStudentId());
        	String timeInMS = probElapsedMap.get(stu.getStudentId());
        	//System.out.println("timeInMS=" + timeInMS);
        	String sMinutes = "0";
        	int iMinutes = 0;
        	if (timeInMS != null) {
	        	int tMinutes = Integer.parseInt(timeInMS);
	        	if (tMinutes > 0) {
	        		iMinutes = tMinutes / 60000;
	        	}
	        	int iSeconds = tMinutes % 60000;
	        	if (iMinutes == 0) {
	        		if (iSeconds > 10000) {
	        			iMinutes++;
	        		}
	        	}
	        	sMinutes = String.valueOf(iMinutes);
        	}
        	//System.out.println("ElapsedTime=" + sMinutes);
        	stu.setTimeInMS(sMinutes);
        	//String test = latestLoginMap.get(stu.getStudentId());
        	//System.out.println("latestLogin=" + test);
        	stu.setLatestLogin(latestLoginMap.get(stu.getStudentId()));
        }
        	
//        for (ClassLandingReportStudents stu : classLandingReportStudents) {
//        	System.out.println("StudentID=" + stu.getStudentId() + " ElapsedTime=" + stu.getTimeInMS());
//        }

        return classLandingReportStudents;
    }


    @Override
    public List<ClassLandingReportStudents> generateClassLandingReportTwo(String teacherId, String classId, String filter) {

    	Timestamp tsFromDate = null;
    	Timestamp tsToDate = null;
    	
    	String filters[] = filter.split("~");
    	if (filters.length > 1) {    		
    		String dateFilters[] = filters[1].split("thru");
    		tsFromDate = convertFromDate(dateFilters[0].trim());
    		tsToDate = convertToDate(dateFilters[1].trim());
    	}
    	else {
    		tsFromDate = defaultFromDate();
    		tsToDate = defaultToDate();    		
    	}
		
        Map<String, Object> eventParams = new HashMap<String, Object>();
        eventParams.put("classId", classId);
        eventParams.put("tsFromDate", tsFromDate);
        eventParams.put("tsToDate", tsToDate);
        SqlParameterSource eventNamedParameters = new MapSqlParameterSource(eventParams);

        List<ClassLandingReportEvents> classLandingReportEvents = namedParameterJdbcTemplate.query(TTUtil.LANDING_REPORT2_EVENTS_QUERY, eventNamedParameters, new ClassLandingReportEventsMapper());

        Map<String, String> probElapsedMap = new HashMap<String, String>();
        Map<String, String> latestLoginMap = new HashMap<String, String>();
        String prevStudent = "";
        int probElapsedTime = 0;
        String latestLogin = "";
        boolean countable = false;
        for (ClassLandingReportEvents event : classLandingReportEvents) {
        	switch (event.getAction()) {
        		case "EndProblem":
        			countable = true;
        			break;
        		case "Student Access":
            		latestLoginMap.put(event.getStudentId(), event.getTimestampString("en"));
        			break;
        		default:
        			countable = false;
        	}
        	if (event.getStudentId().equals(prevStudent)) {
            	if (countable) {
            		probElapsedTime += Integer.valueOf(event.getProbElapsed());
            	}
        	}
        	else {
            	if (prevStudent.length() > 0) {
//                	System.out.println("probElapsedTime = " + String.valueOf(probElapsedTime));
            		probElapsedMap.put(prevStudent, String.valueOf(probElapsedTime));
            		probElapsedTime = 0;
            	}
            	if (countable) {
            		probElapsedTime += Integer.valueOf(event.getProbElapsed());
            	}
          	}
           	prevStudent = event.getStudentId();
        }

    	if (prevStudent.length() > 0) {
//        	System.out.println("Last student - probElapsedTime = " + String.valueOf(probElapsedTime));
    		probElapsedMap.put(prevStudent, String.valueOf(probElapsedTime));
    	}
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classId", classId);
        params.put("tsFromDate", tsFromDate);
        params.put("tsToDate", tsToDate);
        SqlParameterSource namedParameters = new MapSqlParameterSource(params);

        List<ClassLandingReportStudents> classLandingReportStudents = namedParameterJdbcTemplate.query(TTUtil.LANDING_REPORT2_STUDENTS_QUERY, namedParameters, new ClassLandingReportStudentsMapper());
        
        for (ClassLandingReportStudents stu : classLandingReportStudents) {
        	//System.out.println("StudentID=" + stu.getStudentId());
        	String timeInMS = probElapsedMap.get(stu.getStudentId());
        	//System.out.println("timeInMS=" + timeInMS);
        	String sMinutes = "0";
        	int iMinutes = 0;
        	if (timeInMS != null) {
	        	int tMinutes = Integer.parseInt(timeInMS);
	        	if (tMinutes > 0) {
	        		iMinutes = tMinutes / 60000;
	        	}
	        	int iSeconds = tMinutes % 60000;
	        	if (iMinutes == 0) {
	        		if (iSeconds > 10000) {
	        			iMinutes++;
	        		}
	        	}
	        	sMinutes = String.valueOf(iMinutes);
        	}
        	//System.out.println("ElapsedTime=" + sMinutes);
        	stu.setTimeInMS(sMinutes);
        	//String test = latestLoginMap.get(stu.getStudentId());
        	//System.out.println("latestLogin=" + test);
        	stu.setLatestLogin(latestLoginMap.get(stu.getStudentId()));
        }
        	
//        for (ClassLandingReportStudents stu : classLandingReportStudents) {
//        	System.out.println("StudentID=" + stu.getStudentId() + " ElapsedTime=" + stu.getTimeInMS());
//        }

        return classLandingReportStudents;
    }

 
    
    @Override
    public Map<String, Object> generateClassReportPerStudentPerProblemSet(String teacherId, String classId, String filter, String teacherLoginType) throws TTCustomException {
    

    	System.out.println("generateClassReportPerStudentPerProblemSet for class " + classId + "filter= " + filter);
    	Timestamp tsFromDate = null;
    	Timestamp tsToDate = null;
    	
    	String filters[] = filter.split("~");
    	String modFilter = "%";
    	if (filters.length > 0) {
    		modFilter = filters[0].trim() + "%";
    	}

    	if (filters.length >= 2) {
    		if (filters[2].trim().equals("N")) {
    			showNames = "N";
    		}
    		else {
    			showNames = "Y";   			
    		}
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
    	}
    	else {
   			tsFromDate = defaultFromDate();
   			tsToDate = defaultToDate();    		    			    		
    	}    	
    	
       	String logMsg = "{ \"dates\" : \"" +  filters[1].trim() + "\" }";     

		if ("Normal".equals(teacherLoginType)) {
	       	try {
	   			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, classId, rb.getString("perStudentPerProblemSetReport"), logMsg);
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
		
    	logger.debug("generateClassReportPerStudentPerProblem for class " + classId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("filter", modFilter);
        params.put("classId", classId);
        params.put("tsFromDate", tsFromDate);
        params.put("tsToDate", tsToDate);
        SqlParameterSource namedParameters = new MapSqlParameterSource(params);

        Map<String, List<String>> finalMapLevelOne = new LinkedHashMap<>();
        Map<String, List<String>> finalMapLevelOneTemp = new LinkedHashMap<>();
        Map<String, String> columnNamesMap = new LinkedHashMap<>();
        Map<String, Object> allResult = new HashMap<>();
        Map<String, List<String>> resultantValues = namedParameterJdbcTemplate.query(TTUtil.PER_TOPIC_QUERY_FIRST, namedParameters, (ResultSet mappedrow) -> {
            while (mappedrow.next()) {
                String studentId = mappedrow.getString("studentId");
                Integer problemSetId = mappedrow.getInt("topicId");
                List<String> studentValuesList = null;
                List<String> tempTopicDescriptionList = null;
                StudentProblemHistory shHistory = null;
                try {
                    shHistory = new StudentProblemHistory(connection.getConnection(), Integer.valueOf(studentId));

                } catch (TTCustomException e) {
                    logger.error(e.getErrorMessage());
                }
                List<String> noOfProblemsSolved = shHistory.getTopicProblemsSolved(problemSetId);
                List<String> noOfProblemsSolvedOnFirstAttempt = shHistory.getTopicProblemsSolvedOnFirstAttempt(problemSetId);
                if (finalMapLevelOne.containsKey(studentId)) {
                    studentValuesList = finalMapLevelOne.get(studentId);
                    tempTopicDescriptionList = finalMapLevelOneTemp.get(studentId);
                    studentValuesList.add(mappedrow.getString("description").trim().replace(" ", "") + "~~~" + "[" + noOfProblemsSolvedOnFirstAttempt.size() + "/" + noOfProblemsSolved.size() + "]" + "---" + mappedrow.getString("mastery") + "---" + noOfProblemsSolved.size() + "---" + mappedrow.getString("topicId"));
                    tempTopicDescriptionList.add(mappedrow.getString("description").trim().replace(" ", ""));
                } else {
                    studentValuesList = new ArrayList<>();
                    tempTopicDescriptionList = new ArrayList<>();

                    studentValuesList.add("studentName" + "~~~" + mappedrow.getString("studentName"));
                    studentValuesList.add("userName" + "~~~" + mappedrow.getString("userName"));
                    studentValuesList.add(mappedrow.getString("description").trim().replace(" ", "") + "~~~" + "[" + noOfProblemsSolvedOnFirstAttempt.size() + "/" + noOfProblemsSolved.size() + "]" + "---" + mappedrow.getString("mastery") + "---" + noOfProblemsSolved.size() + "---" + mappedrow.getString("topicId"));
                    tempTopicDescriptionList.add(mappedrow.getString("description").trim().replace(" ", ""));
                }
                columnNamesMap.put(mappedrow.getString("topicId"), mappedrow.getString("description"));
                finalMapLevelOne.put(studentId, studentValuesList);
                finalMapLevelOneTemp.put(studentId, tempTopicDescriptionList);

            }
            return finalMapLevelOne;
        });
        finalMapLevelOne.forEach((studentId, studentDetails) -> {
            Map<String, String> selectParams = new LinkedHashMap<String, String>();
            selectParams.put("classId", classId);
            selectParams.put("studId", studentId);

            List<String> tempTopicDescriptionList = finalMapLevelOneTemp.get(studentId);
            List<String> columnList = new ArrayList<String>(columnNamesMap.values());
            List<String> columnListFinal = new ArrayList<String>();

            for (String columnNames : columnList)
                columnListFinal.add(columnNames.trim().replaceAll(" ", ""));

            if (!tempTopicDescriptionList.isEmpty()) {
                columnListFinal.removeAll(tempTopicDescriptionList);
                for (String columnNames : columnListFinal) {
                    studentDetails.add(columnNames + "~~~" + "");

                }
            }
        });
        allResult.put("levelOneData", finalMapLevelOne);
        allResult.put("columns", columnNamesMap);
        return allResult;
    }

    @Override
    public Map<String, Object> generateClassReportPerStudentPerProblem(String teacherId, String classId, String filter, String teacherLoginType) throws TTCustomException {

    	System.out.println("generateClassReportPerStudentPerProblem for class " + classId + "filter= " + filter);
    	
    	Timestamp tsFromDate = null;
    	Timestamp tsToDate = null;
    	String filters[] = filter.split("~");
    	String modFilter = "%";

    	
    	if (filters.length > 0) {
    		modFilter = filters[0].trim() + "%";
    	}
    	
    	if (filters.length >= 2) {
    		if (filters[2].trim().equals("N")) {
    			showNames = "N";
    		}
    		else {
    			showNames = "Y";   			
    		}
    	}
    		
    	if (filters.length > 3) {
    		selectedStudent = filters[3].trim();
    	}
    	else {
    		selectedStudent = "";
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
    	}
    	else {
   			tsFromDate = defaultFromDate();
   			tsToDate = defaultToDate();    		    			    		
    	}
    	
       	String logMsg = "{ \"standard\" : \"" + filters[0].trim() + "\", \"dates\" : \"" +  filters[1].trim() + "\", \"shownames\" : \"" + showNames + "\", \"student\" : \"" + selectedStudent + "\" }";     

		if ("Normal".equals(teacherLoginType)) {
	       	try {
	   			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, classId, rb.getString("perStudentPerProblemReport"), logMsg);
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("filter", modFilter);
        params.put("classId", classId);
        params.put("tsFromDate", tsFromDate);
        params.put("tsToDate", tsToDate);
        SqlParameterSource namedParameters = new MapSqlParameterSource(params);

        
        
        Map<String, List<String>> finalMapLevelOne = new LinkedHashMap<>();
        Map<String, List<String>> finalMapLevelOneTemp = new LinkedHashMap<>();
        Map<String, String> columnNamesMap = new LinkedHashMap<>();
        Map<String, String> descriptionIdMap = new LinkedHashMap<>();
        Map<String, Object> allResult = new HashMap<>();
        

        Map<String, List<String>> resultantValues = namedParameterJdbcTemplate.query(TTUtil.PER_STUDENT_PER_PROBLEM, namedParameters, (ResultSet mappedrow) -> {
            while (mappedrow.next()) {
            	String studentId = ((String) mappedrow.getString("studId")).trim();
                if ((!selectedStudent.equals("")) && (!selectedStudent.equals(studentId) )) {
                	continue;
                }
                Integer problemId = mappedrow.getInt("problemId");
                String strProblemId = String.valueOf(problemId);
                String effort = mappedrow.getString("effort");
                if ((effort == null) || (effort.equals("unknown")) || (effort.equals("null")) || (effort.equals("undefined"))) {
                	effort = "NoData";                
                }
                else {
                	 if (effort.length() > 0) {
                     	effort = effort.trim();                		 
                	 }
                	 else {
                		 effort = "NoData"; 
                	 }
                }
                if (effort.equals("NoData") ) {
                	continue;
                }

                String description = ((String) mappedrow.getString("description")).trim();
                description = description.replace(".", "_");

                String nickname = ((String) mappedrow.getString("nickname"));
                if (nickname == null) {
                	nickname = "???";
                }
                if (nickname.length() > 0) {
                	nickname = nickname.trim();
                	nickname = nickname.replace(".", "_");                }
                else {
                	nickname = description;
                }
                
                Timestamp beginTime = mappedrow.getTimestamp("problemBeginTime");
                
        		Locale tloc = new Locale("en","US");

                long t = beginTime.getTime();
                String strBeginTime = String.valueOf(t);
                
            	String tsFormat = "dd-MMM-YY";
                Date dd = StringUtils.timestampToDate(beginTime);
                SimpleDateFormat formatter =  new SimpleDateFormat (tsFormat, tloc) ;                
                String problemDate = formatter.format(dd);              


                String URI = Settings.probPreviewerPath;
                String html5ProblemURI = Settings.html5ProblemURI;

                String prob_nickname = strProblemId + "^" + nickname;
                descriptionIdMap.put(description,prob_nickname);
                
                
                List<String> studentValuesList = null;
                List<String> tempProblemDescriptionList = null;
                
                
                /*
                StudentProblemHistory shHistory = null;
                try {
                    shHistory = new StudentProblemHistory(connection.getConnection(), Integer.valueOf(studentId));

                } catch (TTCustomException e) {
                    logger.error(e.getErrorMessage());
                }
                //List<String> noOfProblemsSolved = shHistory.getTopicProblemsSolved(problemSetId);
                //List<String> noOfProblemsSolvedOnFirstAttempt = shHistory.getTopicProblemsSolvedOnFirstAttempt(problemSetId);
                */

                if (finalMapLevelOne.containsKey(studentId)) {
                    studentValuesList = finalMapLevelOne.get(studentId);
                    tempProblemDescriptionList = finalMapLevelOneTemp.get(studentId);
//                    studentValuesList.add(mappedrow.getString("description").trim().replace(" ", "") + "~~~" + "[" + noOfProblemsSolvedOnFirstAttempt.size() + "/" + noOfProblemsSolved.size() + "]" + "---" + mappedrow.getString("mastery") + "---" + noOfProblemsSolved.size() + "---" + mappedrow.getString("topicId"));
                    studentValuesList.add(description.trim().replace(" ", "") + "~~~" + effort + "^" + problemDate + "^" + strProblemId );
                    tempProblemDescriptionList.add(description.trim().replace(" ", ""));
                } else {
                    studentValuesList = new ArrayList<>();
                    tempProblemDescriptionList = new ArrayList<>();
                    if (showNames.equals("N")) {
                        studentValuesList.add("studentName" + "~~~" + "XXXXXXXXX");
                        studentValuesList.add("userName" + "~~~" +"XXXXXX-XX");

                    }
                    else {
                    	studentValuesList.add("studentName" + "~~~" + mappedrow.getString("studentName"));
                    	studentValuesList.add("userName" + "~~~" + mappedrow.getString("userName"));
                    }
//                    studentValuesList.add(mappedrow.getString("description").trim().replace(" ", "") + "~~~" + "[" + noOfProblemsSolvedOnFirstAttempt.size() + "/" + noOfProblemsSolved.size() + "]" + "---" + mappedrow.getString("mastery") + "---" + noOfProblemsSolved.size() + "---" + mappedrow.getString("topicId"));
                    studentValuesList.add(description.trim().replace(" ", "") + "~~~" + effort + "^" + problemDate + "^" + strProblemId );
                    tempProblemDescriptionList.add(description.trim().replace(" ", ""));
                }

                String strDebug = "[" + mappedrow.getString("studentName") + " " + studentId + " " + strProblemId + " " + description + " " + effort + " " + strBeginTime + " " + problemDate + " " + strProblemId  + "]";
            	System.out.println(strDebug);

                columnNamesMap.put(problemId.toString(), description);
                finalMapLevelOne.put(studentId, studentValuesList);
                finalMapLevelOneTemp.put(studentId, tempProblemDescriptionList);

            }
            return finalMapLevelOne;
        });
        int finalMapLevelOneCount = finalMapLevelOne.size();

        List<String> dummyStudentValuesList = null;
        List<String> dummyTempProblemDescriptionList = null;
        int padCount = (finalMapLevelOneCount % 16);
        if (padCount < 16) {
    		dummyStudentValuesList = new ArrayList<>();
            dummyTempProblemDescriptionList = new ArrayList<>();
        	for (int i=0; i < (16 - padCount); i++) {
                dummyStudentValuesList.add("studentName" + "~~~" + "_");
                dummyStudentValuesList.add("userName" + "~~~" +" ");
                dummyStudentValuesList.add("padEffort^0^0");
                dummyTempProblemDescriptionList.add("padEffort^0^0");
                String dummyId = String.valueOf(i+999990);
                finalMapLevelOne.put(dummyId, dummyStudentValuesList);
                finalMapLevelOneTemp.put(dummyId, dummyTempProblemDescriptionList);        		
        	}
        }
        finalMapLevelOne.forEach((studentId, studentDetails) -> {
            //Map<String, String> selectParams = new LinkedHashMap<String, String>();
            //selectParams.put("classId", classId);
            //selectParams.put("studId", studentId);

            List<String> tempProblemDescriptionList = finalMapLevelOneTemp.get(studentId);
            List<String> columnList = new ArrayList<String>(columnNamesMap.values());
            List<String> columnListFinal = new ArrayList<String>();

            for (String columnNames : columnList) {
                columnListFinal.add(columnNames.trim().replaceAll(" ", ""));
            }
            if (!tempProblemDescriptionList.isEmpty()) {
                columnListFinal.removeAll(tempProblemDescriptionList);
                for (String columnNames : columnListFinal) {
                    studentDetails.add(columnNames + "~~~" + "");

                }
            }
        });

        allResult.put("levelOneData", finalMapLevelOne);
        allResult.put("columns", columnNamesMap);
        logger.debug(columnNamesMap);
        allResult.put("IdXref", descriptionIdMap);
        //logger.info(allResult.toString());
        return allResult;
    }
    
    

    @Override
    public String getMasterProjectionsForCurrentTopic(String classId, String studentId, String topicID, String filter) throws TTCustomException {

    	Timestamp tsFromDate = null;
    	Timestamp tsToDate = null;
    	String filters[] = filter.split("~");

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
    	}
    	else {
   			tsFromDate = defaultFromDate();
   			tsToDate = defaultToDate();    		    			    		
    	}
    	
    	try {
            ObjectMapper objMapper = new ObjectMapper();
/*
            Map<String, Object> selectParams = new LinkedHashMap<String, Object>();
            selectParams.put("classId", classId);
            selectParams.put("studId", studentId);
            selectParams.put("topicId", topicID);
            selectParams.put("ts", ts);
            SqlParameterSource namedParameters = new MapSqlParameterSource(selectParams);
*/
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("classId", classId);
            params.put("studId", studentId);
            params.put("topicId", topicID);
            params.put("tsFromDate", tsFromDate);
            params.put("tsToDate", tsToDate);
            SqlParameterSource namedParameters = new MapSqlParameterSource(params);


            
            List<String[]> eachMasteryStudentPerTopicResults = namedParameterJdbcTemplate.query(TTUtil.PER_TOPIC_QUERY_SECOND, namedParameters, (ResultSet mappedrow) -> {
                List<String[]> arrayRecords = new ArrayList<>();
                while (mappedrow.next()) {
                    String[] problemDetails = new String[10];
                    String beginTime = mappedrow.getString("problemBeginTime");
                    problemDetails[0] = mappedrow.getString("problemId");
                    problemDetails[1] = mappedrow.getString("name");
                    problemDetails[2] = mappedrow.getString("nickname");
                    problemDetails[3] = mappedrow.getString("statementHTML");
                    problemDetails[4] = mappedrow.getString("screenShotURL");
                    problemDetails[5] = mappedrow.getString("mastery");
                    problemDetails[6] = mappedrow.getString("problemEndTime");
                    if ("".equals(mappedrow.getString("effort")) || "unknown".equals(mappedrow.getString("effort")) || mappedrow.getString("effort") == null)
                        problemDetails[7] = "NO DATA";
                    else
                        problemDetails[7] = mappedrow.getString("effort");
                    arrayRecords.add(problemDetails);

                }
                return arrayRecords;
            });
            return objMapper.writeValueAsString(eachMasteryStudentPerTopicResults);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
        }
    }

    @Override
    public String getCompleteMasteryProjectionForStudent(String classId, String studentId, String chartType) throws TTCustomException{
        try {
            Map<String, String> selectParams = new LinkedHashMap<String, String>();
            ObjectMapper objMapper = new ObjectMapper();

            selectParams.put("classId", classId);
            selectParams.put("studId", studentId);

            String queryType = "";
            if (chartType.equals("avg"))
                queryType = TTUtil.PER_TOPIC_QUERY_COMPLETE_AVG;
            else if (chartType.equals("max"))
                queryType = TTUtil.PER_TOPIC_QUERY_COMPLETE_MAX;
            else
                queryType = TTUtil.PER_TOPIC_QUERY_COMPLETE_LATEST;

            List<String> resultantValues = namedParameterJdbcTemplate.query(queryType, selectParams, (ResultSet mappedrowAvg) -> {
                List<String> fieldValuesList = new ArrayList<>();
                while (mappedrowAvg.next()) {
                    String fieldValue = mappedrowAvg.getString("topicId") + "~~~" + mappedrowAvg.getString("mastery");
                    fieldValuesList.add(fieldValue);
                }
                return fieldValuesList;
            });
            return objMapper.writeValueAsString(resultantValues);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.DATABASE_CONNECTION_FAILED);
        }
    }

    @Override
    public String generateReportForProblemsInCluster(String teacherId, String classId, String clusterId, String filter, String teacherLoginType) throws TTCustomException {
        try {
       	
        	Timestamp tsFromDate = null;
        	Timestamp tsToDate = null;
        	String filters[] = filter.split("~");

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
        	}
        	else {
       			tsFromDate = defaultFromDate();
       			tsToDate = defaultToDate();    		    			    		
        	}
        	
           	String logMsg = "{ \"clusterId\" : \"" + clusterId + "\", \"dates\" : \"" +  filters[1].trim() + "\" }";     
    		if ("Normal".equals(teacherLoginType)) {
    	       	try {
    	   			tLogger.logEntryWorker((int) Integer.valueOf(teacherId), 0, classId, rb.getString("ProblemsInClusterReport"), logMsg);
    	    	}
    	    	catch (Exception e) {
    	    		System.out.println("TeacherLogger error " + e.getMessage());
    	    	}
    		}

    	    Map<String, Object>selectParams = new HashMap<String, Object>();
    	      
            Map<String, String> problemDescriptionMap = new LinkedHashMap<String, String>();
            selectParams.put("classId", classId);
            selectParams.put("clusterID", clusterId);
            selectParams.put("tsFromDate", tsFromDate);
            selectParams.put("tsToDate", tsToDate);
            List<String> problemIdsList = namedParameterJdbcTemplate.query(TTUtil.PER_STANDARD_QUERY_THIRD, selectParams, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    problemDescriptionMap.put(resultSet.getString("problemID"), resultSet.getString("name")
                            + "~~" + resultSet.getString("screenShotURL") + "~~" + resultSet.getString("standardID") + ":" + resultSet.getString("standardCategoryName")+ ":" + resultSet.getString("description"));
                    return resultSet.getString("problemID");
                }
            });
            Map<String, PerProblemReportBean> resultObjectPerCluster = generatePerProblemReportForGivenProblemID(classId, problemIdsList, problemDescriptionMap);
            ObjectMapper perStudentPerProblemClusterReportMapper = new ObjectMapper();
            return perStudentPerProblemClusterReportMapper.writeValueAsString(resultObjectPerCluster);
        } catch (IOException excep) {
            logger.error(excep.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.FAILED_TO_LOAD_PROBLEMS);
        }
    }

  /*  private Map<String, PerProblemReportBean> generatePerProblemReportForGivenProblemIDs(String classId, List<String> problemIdsList, Map<String, String> problemDescriptionMap)  {
        Map<String, PerProblemReportBean> perProblemReportBeanMap = new LinkedHashMap<String, PerProblemReportBean>();
        Map<String, String> selectParams = new LinkedHashMap<String, String>();
        String URI = Settings.probPreviewerPath;
        String html5ProblemURI = Settings.html5ProblemURI;
        selectParams.put("classId", classId);
        for (String problemId : problemIdsList) {
            selectParams.put("problemId",problemId);
            List<PerProblemReportBean> perProblemReportBean = namedParameterJdbcTemplate.query(TTUtil.PER_PROBLEM_QUERY_SECOND, selectParams, new RowMapper<PerProblemReportBean>() {
                @Override
                public PerProblemReportBean mapRow(ResultSet resultSet, int i) throws SQLException {
                    int studId = resultSet.getInt("e.studid");
                    String problemID = resultSet.getString("e.problemId");

                    PerProblemReportBean perProblemReportBeanObj = null;
                    if (perProblemReportBeanMap.containsKey(problemID))
                        perProblemReportBeanObj = perProblemReportBeanMap.get(problemID);
                    else
                        perProblemReportBeanObj = new PerProblemReportBean();

                    if (studId != perProblemReportBeanObj.lastStud) {
                        // gather stats on last student
                        if (perProblemReportBeanObj.lastStud != -1) {

                            perProblemReportBeanObj.nStudsSeen++;
                            int nStudsRepeated = perProblemReportBeanObj.getPercStudentsRepeated();
                            nStudsRepeated += (perProblemReportBeanObj.studEncounters > 1) ? 1 : 0;
                            int nStudsSkipped = perProblemReportBeanObj.getPercStudentsSkipped();
                            nStudsSkipped += (perProblemReportBeanObj.studEncounters == perProblemReportBeanObj.nSkips) ? 1 : 0;
                            int nStudsGiveUp = perProblemReportBeanObj.getPercStudentsGaveUp();
                            nStudsGiveUp += (perProblemReportBeanObj.studEncounters > perProblemReportBeanObj.nSkips && !perProblemReportBeanObj.solved) ? 1 : 0;
                            int nStudsGiveSolved = perProblemReportBeanObj.getGetPercStudentsSolvedEventually();
                            nStudsGiveSolved += perProblemReportBeanObj.solved ? 1 : 0;

                            perProblemReportBeanObj.setNoStudentsSeenProblem(perProblemReportBeanObj.nStudsSeen);

                            perProblemReportBeanObj.setPercStudentsRepeated(nStudsRepeated);
                            perProblemReportBeanObj.setPercStudentsSkipped(nStudsSkipped);
                            perProblemReportBeanObj.setPercStudentsGaveUp(nStudsGiveUp);
                            perProblemReportBeanObj.setGetPercStudentsSolvedEventually(nStudsGiveSolved);

                            if (perProblemReportBeanObj.nA > perProblemReportBeanObj.nB && perProblemReportBeanObj.nA > perProblemReportBeanObj.nC && perProblemReportBeanObj.nA > perProblemReportBeanObj.nD)
                                perProblemReportBeanObj.setMostIncorrectResponse("A");
                            else if (perProblemReportBeanObj.nB > perProblemReportBeanObj.nA && perProblemReportBeanObj.nB > perProblemReportBeanObj.nC && perProblemReportBeanObj.nB > perProblemReportBeanObj.nD)
                                perProblemReportBeanObj.setMostIncorrectResponse("B");
                            else if (perProblemReportBeanObj.nC > perProblemReportBeanObj.nA && perProblemReportBeanObj.nC > perProblemReportBeanObj.nB && perProblemReportBeanObj.nC > perProblemReportBeanObj.nD)
                                perProblemReportBeanObj.setMostIncorrectResponse("C");
                            else if (perProblemReportBeanObj.nD > perProblemReportBeanObj.nA && perProblemReportBeanObj.nD > perProblemReportBeanObj.nB && perProblemReportBeanObj.nD > perProblemReportBeanObj.nC)
                                perProblemReportBeanObj.setMostIncorrectResponse("D");
                            else perProblemReportBeanObj.setMostIncorrectResponse("-");

                        }

                        perProblemReportBeanObj.begTime = 0;
                        perProblemReportBeanObj.firstActTime = 0;
                        perProblemReportBeanObj.solved = false;
                        perProblemReportBeanObj.lastStud = studId;
                        perProblemReportBeanObj.studEncounters = 0;
                        perProblemReportBeanObj.nSkips = 0;
                        perProblemReportBeanObj.nA = perProblemReportBeanObj.nB = perProblemReportBeanObj.nC = perProblemReportBeanObj.nD = 0;
                    }

                    String action = resultSet.getString("e.action");
                    boolean isCorrect = resultSet.getBoolean("e.isCorrect");
                    long elapsedTime = resultSet.getLong("e.elapsedTime");
                    String userInput = resultSet.getString("e.userInput");
                    String activityName = resultSet.getString("e.activityName"); // on beginProblem will be either practice or demo

                    if (action.equals("BeginProblem") && activityName.equals("practice")) {
                        perProblemReportBeanObj.found = true;
                        perProblemReportBeanObj.isExample = false;
                        perProblemReportBeanObj.begTime = elapsedTime;
                        perProblemReportBeanObj.studEncounters++;
                        perProblemReportBeanObj.attemptIx = 0;
                        perProblemReportBeanObj.correctAttemptIx = 0;
                        perProblemReportBeanObj.nHints = 0;
                        perProblemReportBeanObj.firstActTime = 0;
                    } else if (action.equals("BeginProblem"))
                        perProblemReportBeanObj.isExample = true;
                    else if (action.equals("EndProblem") && !perProblemReportBeanObj.isExample) {
                        perProblemReportBeanObj.probTime = elapsedTime - perProblemReportBeanObj.begTime;
                        if (perProblemReportBeanObj.probTime < 6000 && perProblemReportBeanObj.attemptIx == 0 && perProblemReportBeanObj.nHints == 0)
                            perProblemReportBeanObj.nSkips++;
                    } else if (action.equals("Hint") && !perProblemReportBeanObj.isExample) {
                        perProblemReportBeanObj.nHints++;
                        if (perProblemReportBeanObj.firstActTime == 0)
                            perProblemReportBeanObj.firstActTime = elapsedTime - perProblemReportBeanObj.begTime;
                    } else if (action.equals("Attempt") && !perProblemReportBeanObj.isExample) {
                        perProblemReportBeanObj.attemptIx++;
                        if (perProblemReportBeanObj.firstActTime == 0)
                            perProblemReportBeanObj.firstActTime = elapsedTime - perProblemReportBeanObj.begTime;
                        if (isCorrect) {
                            perProblemReportBeanObj.solved = true;
                            if (perProblemReportBeanObj.correctAttemptIx == 0) {
                                perProblemReportBeanObj.correctAttemptIx = perProblemReportBeanObj.attemptIx;
                            }
                            if (perProblemReportBeanObj.studEncounters == 1) {
                                if (perProblemReportBeanObj.correctAttemptIx == 1) {
                                    perProblemReportBeanObj.getGetPercStudentsSolvedFirstTry++;

                                } else if (perProblemReportBeanObj.correctAttemptIx == 2) {
                                    perProblemReportBeanObj.getGetPercStudentsSolvedSecondTry++;
                                }
                            }
                        } else {
                            if (userInput.equalsIgnoreCase("a"))
                                perProblemReportBeanObj.nA++;
                            else if (userInput.equalsIgnoreCase("b"))
                                perProblemReportBeanObj.nB++;
                            else if (userInput.equalsIgnoreCase("c"))
                                perProblemReportBeanObj.nC++;
                            else if (userInput.equalsIgnoreCase("d"))
                                perProblemReportBeanObj.nD++;
                        }
                    }
                    Problem probDetails = ProblemMgr.getProblem(Integer.valueOf(problemID));
                    if(probDetails != null) {
                        if ("flash".equals(probDetails.getType())) {
                            perProblemReportBeanObj.setProblemURLWindow( URI + "?questionNum=" + probDetails.getProbNumber());
                        } else {
                            perProblemReportBeanObj.setProblemURLWindow(  html5ProblemURI + probDetails.getHTMLDir() + "/" + probDetails.getResource());
                        }
                    }else{
                        perProblemReportBeanObj.setProblemURLWindow( html5ProblemURI );
                    }
                    perProblemReportBeanObj.setSimilarproblems("Similar Problems");

                    perProblemReportBeanMap.put(problemID, perProblemReportBeanObj);
                    return perProblemReportBeanObj;
                }
            });
        }
        perProblemReportBeanMap.forEach((problemId, perProblemReportBeanObj) -> {
            int SKIP = 0, NOTR = 0, GIVEUP = 0, SOF = 0, SHINT = 0, SHELP = 0, ATT = 0, GUESS = 0, NODATA = 0;
            selectParams.put("problemId",problemId);
            List<String> combinedStudentEffortsOnProblem = namedParameterJdbcTemplate.query(TTUtil.PER_PROBLEM_QUERY_THIRD, selectParams, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet resultSet, int i) throws SQLException {
                    String effort = resultSet.getString("sh.effort");
                    return effort;
                }
            });
            // Calculate Effort Percentages
            for (String effortVal : combinedStudentEffortsOnProblem) {
                switch (effortVal) {
                    case "SKIP":
                        SKIP++;
                        break;
                    case "NOTR":
                        NOTR++;
                        break;
                    case "SOF":
                        SOF++;
                        break;
                    case "ATT":
                        ATT++;
                        break;
                    case "GIVEUP":
                        GIVEUP++;
                        break;
                    case "GUESS":
                        GUESS++;
                        break;
                    case "SHINT":
                        SHINT++;
                        break;
                    case "SHELP":
                        SHELP++;
                        break;
                    case "NODATA":
                        NODATA++;
                        break;
                    default:
                        NODATA++;
                        break;
                }
            }

            String[] problemDescriptionValues =  problemDescriptionMap.get(problemId).split("~~");
            perProblemReportBeanObj.setProblemName(problemDescriptionValues[0]);
            perProblemReportBeanObj.setImageURL(problemDescriptionValues[1]);
            perProblemReportBeanObj.setProblemStandardAndDescription(problemDescriptionValues[2]);

            if (perProblemReportBeanObj.found) {
                perProblemReportBeanObj.nStudsSeen++;
                int nStudsRepeated = perProblemReportBeanObj.getPercStudentsRepeated();
                nStudsRepeated += (perProblemReportBeanObj.studEncounters > 1) ? 1 : 0;
                int nStudsSkipped = perProblemReportBeanObj.getPercStudentsSkipped();
                nStudsSkipped += (perProblemReportBeanObj.studEncounters == perProblemReportBeanObj.nSkips) ? 1 : 0;
                int nStudsGiveUp = perProblemReportBeanObj.getPercStudentsGaveUp();
                nStudsGiveUp += (perProblemReportBeanObj.studEncounters > perProblemReportBeanObj.nSkips && !perProblemReportBeanObj.solved) ? 1 : 0;
                int nStudsGiveSolved = perProblemReportBeanObj.getGetPercStudentsSolvedEventually();
                nStudsGiveSolved += perProblemReportBeanObj.solved ? 1 : 0;

                perProblemReportBeanObj.setNoStudentsSeenProblem(perProblemReportBeanObj.nStudsSeen);
                String[] effortvalues = new String[9];
                effortvalues[0] = Double.toString((double) ((SKIP * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[1] = Double.toString((double) ((NOTR * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[2] = Double.toString((double) ((GIVEUP * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[3] = Double.toString((double) ((SOF * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[4] = Double.toString((double) ((ATT * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[5] = Double.toString((double) ((GUESS * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[6] = Double.toString((double) ((SHINT * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[7] = Double.toString((double) ((SHELP * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[8] = Double.toString((double) ((NODATA * 100) / perProblemReportBeanObj.nStudsSeen));

                perProblemReportBeanObj.setStudentEffortsPerProblem(effortvalues);
                perProblemReportBeanObj.setPercStudentsRepeated(nStudsRepeated);
                perProblemReportBeanObj.setPercStudentsSkipped(nStudsSkipped);
                perProblemReportBeanObj.setPercStudentsGaveUp(nStudsGiveUp);
                perProblemReportBeanObj.setGetPercStudentsSolvedEventually(nStudsGiveSolved);

                if (perProblemReportBeanObj.nA > perProblemReportBeanObj.nB && perProblemReportBeanObj.nA > perProblemReportBeanObj.nC && perProblemReportBeanObj.nA > perProblemReportBeanObj.nD)
                    perProblemReportBeanObj.setMostIncorrectResponse("A");
                else if (perProblemReportBeanObj.nB > perProblemReportBeanObj.nA && perProblemReportBeanObj.nB > perProblemReportBeanObj.nC && perProblemReportBeanObj.nB > perProblemReportBeanObj.nD)
                    perProblemReportBeanObj.setMostIncorrectResponse("B");
                else if (perProblemReportBeanObj.nC > perProblemReportBeanObj.nA && perProblemReportBeanObj.nC > perProblemReportBeanObj.nB && perProblemReportBeanObj.nC > perProblemReportBeanObj.nD)
                    perProblemReportBeanObj.setMostIncorrectResponse("C");
                else if (perProblemReportBeanObj.nD > perProblemReportBeanObj.nA && perProblemReportBeanObj.nD > perProblemReportBeanObj.nB && perProblemReportBeanObj.nD > perProblemReportBeanObj.nC)
                    perProblemReportBeanObj.setMostIncorrectResponse("D");
                else perProblemReportBeanObj.setMostIncorrectResponse("-");
            }
            *//* Round while converting to Percentage *//*
            perProblemReportBeanObj.setPercStudentsRepeated((int)Math.round(100.0 / perProblemReportBeanObj.getNoStudentsSeenProblem() * perProblemReportBeanObj.getPercStudentsRepeated()));
            perProblemReportBeanObj.setPercStudentsSkipped((int)Math.round(100.0 / perProblemReportBeanObj.getNoStudentsSeenProblem() * perProblemReportBeanObj.getPercStudentsSkipped()));
            perProblemReportBeanObj.setPercStudentsGaveUp((int)Math.round(100.0 / perProblemReportBeanObj.getNoStudentsSeenProblem() * perProblemReportBeanObj.getPercStudentsGaveUp()));
            perProblemReportBeanObj.setGetPercStudentsSolvedEventually((int)Math.round(100.0 / perProblemReportBeanObj.getNoStudentsSeenProblem() * perProblemReportBeanObj.getGetPercStudentsSolvedEventually()));
            perProblemReportBeanObj.setGetGetPercStudentsSolvedFirstTry((int)Math.round(100.0 / perProblemReportBeanObj.getNoStudentsSeenProblem() * perProblemReportBeanObj.getGetGetPercStudentsSolvedFirstTry()));

        });
        return perProblemReportBeanMap;
    }*/


    private Map<String, PerProblemReportBean> generatePerProblemReportForGivenProblemID(String classId, List<String> problemIdsList, Map<String, String> problemDescriptionMap){
        Map<String, PerProblemReportBean> perProblemReportBeanMap = new LinkedHashMap<String, PerProblemReportBean>();
        Map<String, String> selectParams = new LinkedHashMap<String, String>();
        String URI = Settings.probPreviewerPath;
        String html5ProblemURI = Settings.html5ProblemURI;
        selectParams.put("classId", classId);
        for (String problemId : problemIdsList) {
            selectParams.put("problemId", problemId);
            PerProblemReportBean perProblemReportBean = namedParameterJdbcTemplate.query(TTUtil.PER_PROBLEM_QUERY_FIFTH, selectParams, new ResultSetExtractor<PerProblemReportBean>() {
                @Override
                public PerProblemReportBean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                    PerProblemReportBean perProblemReportBean = new PerProblemReportBean();
                    while (resultSet.next()) {
                        perProblemReportBean.setNoStudentsSeenProblem(resultSet.getInt("noOfStudents"));
                    }
                    Problem probDetails = ProblemMgr.getProblem(Integer.valueOf(problemId));
                    if (probDetails != null) {
                        if ("flash".equals(probDetails.getType())) {
                            perProblemReportBean.setProblemURLWindow(URI + "?questionNum=" + probDetails.getProbNumber());
                        } else {
                            perProblemReportBean.setProblemURLWindow(html5ProblemURI + probDetails.getHTMLDir() + "/" + probDetails.getResource());
                        }
                    } else {
                        perProblemReportBean.setProblemURLWindow(html5ProblemURI);
                    }

                    return perProblemReportBean;
                }
            });
            perProblemReportBean.setSimilarproblems("Similar Problems");
            String[] problemDescriptionValues = problemDescriptionMap.get(problemId).split("~~");
            perProblemReportBean.setProblemName(problemDescriptionValues[0]);
            perProblemReportBean.setImageURL(problemDescriptionValues[1]);
            perProblemReportBean.setProblemStandardAndDescription(problemDescriptionValues[2]);
            perProblemReportBeanMap.put(problemId, perProblemReportBean);
        }
            perProblemReportBeanMap.forEach((problemID, perProblemReportBeanObj) -> {
                selectParams.put("problemId", problemID);
                int SKIPO = 0, NOTRO = 0, GIVEUPO = 0, SOFO = 0, SHINTO = 0, SHELPO = 0, ATTO = 0, GUESSO = 0, NODATAO = 0;
                List<PerProblemReportBean> perProblemReportBeans = namedParameterJdbcTemplate.query(TTUtil.PER_PROBLEM_QUERY_FOURTH, selectParams, new RowMapper<PerProblemReportBean>() {
                    int SKIP = 0, GIVEUP = 0, SOF = 0;
                    @Override
                    public PerProblemReportBean mapRow(ResultSet resultSet, int i) throws SQLException {
                        perProblemReportBeanObj.nStudsSeen++;
                        String effortGot = resultSet.getString("h.effort");
                        effortGot = effortGot == null ? "NODATA" : effortGot;
                        switch (effortGot) {
                            case "SKIP":
                                SKIP++;
                                perProblemReportBeanObj.setPercStudentsSkipped(SKIP);
                                break;
                            case "SOF":
                                SOF++;
                                perProblemReportBeanObj.setGetGetPercStudentsSolvedFirstTry(SOF);
                                break;
                            case "GIVEUP":
                                GIVEUP++;
                                perProblemReportBeanObj.setPercStudentsGaveUp(GIVEUP);
                                break;
                            default:
                                break;
                        }
                        return perProblemReportBeanObj;
                    }
                });
                perProblemReportBeanObj.setGetGetPercStudentsSolvedFirstTry((int)Math.round(100.0 / perProblemReportBeanObj.nStudsSeen * perProblemReportBeanObj.getGetGetPercStudentsSolvedFirstTry()));
                perProblemReportBeanObj.setPercStudentsSkipped((int)Math.round(100.0 / perProblemReportBeanObj.nStudsSeen * perProblemReportBeanObj.getPercStudentsSkipped()));
                perProblemReportBeanObj.setPercStudentsGaveUp((int)Math.round(100.0 / perProblemReportBeanObj.nStudsSeen * perProblemReportBeanObj.getPercStudentsGaveUp()));
                List<String> combinedStudentEffortsOnProblem = namedParameterJdbcTemplate.query(TTUtil.PER_PROBLEM_QUERY_THIRD, selectParams, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        String effort = resultSet.getString("sh.effort");
                        return effort;
                    }
                });
                // Calculate Effort Percentages
                for (String effortVal : combinedStudentEffortsOnProblem) {
                    effortVal = effortVal == null ? "NODATA" : effortVal;
                    switch (effortVal) {
                        case "SKIP":
                            SKIPO++;
                            break;
                        case "NOTR":
                            NOTRO++;
                            break;
                        case "SOF":
                            SOFO++;
                            break;
                        case "ATT":
                            ATTO++;
                            break;
                        case "GIVEUP":
                            GIVEUPO++;
                            break;
                        case "GUESS":
                            GUESSO++;
                            break;
                        case "SHINT":
                            SHINTO++;
                            break;
                        case "SHELP":
                            SHELPO++;
                            break;
                        case "NODATA":
                            NODATAO++;
                            break;
                        default:
                            NODATAO++;
                            break;
                    }
                }
                String[] effortvalues = new String[9];
                
                // Frank S. - Temporary fix for divide by zero exception
                if (perProblemReportBeanObj.nStudsSeen == 0) {
                	perProblemReportBeanObj.nStudsSeen = 1;
                	logger.debug("When nStudsSeen = 0");
                	logger.debug("SKIPO=" + String.valueOf(SKIPO));
                	logger.debug("NOTRO=" + String.valueOf(NOTRO));
                	logger.debug("GIVEUPO=" + String.valueOf(GIVEUPO));
                	logger.debug("SOFO=" + String.valueOf(SOFO));
                	logger.debug("ATTO=" + String.valueOf(ATTO));
                	logger.debug("GUESSO=" + String.valueOf(GUESSO));
                	logger.debug("SHINTO=" + String.valueOf(SHINTO));
                	logger.debug("SHELPO=" + String.valueOf(SHELPO));
                	logger.debug("(NODATAO=" + String.valueOf(NODATAO));
                }
            
                effortvalues[0] = Double.toString((double) ((SKIPO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[1] = Double.toString((double) ((NOTRO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[2] = Double.toString((double) ((GIVEUPO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[3] = Double.toString((double) ((SOFO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[4] = Double.toString((double) ((ATTO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[5] = Double.toString((double) ((GUESSO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[6] = Double.toString((double) ((SHINTO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[7] = Double.toString((double) ((SHELPO * 100) / perProblemReportBeanObj.nStudsSeen));
                effortvalues[8] = Double.toString((double) ((NODATAO * 100) / perProblemReportBeanObj.nStudsSeen));
                perProblemReportBeanObj.setStudentEffortsPerProblem(effortvalues);

                List<PerProblemReportBean> perProblemReportBean = namedParameterJdbcTemplate.query(TTUtil.PER_PROBLEM_QUERY_SECOND, selectParams, new RowMapper<PerProblemReportBean>() {
                    @Override
                    public PerProblemReportBean mapRow(ResultSet resultSet, int i) throws SQLException {
                        String action = resultSet.getString("e.action");
                        boolean isCorrect = resultSet.getBoolean("e.isCorrect");
                        long elapsedTime = resultSet.getLong("e.elapsedTime");
                        String userInput = resultSet.getString("e.userInput");
                        String activityName = resultSet.getString("e.activityName"); // on beginProblem will be either practice or demo
                        if (action.equals("BeginProblem") && activityName.equals("practice")) {
                            perProblemReportBeanObj.isExample = false;
                        } else if (action.equals("BeginProblem"))
                            perProblemReportBeanObj.isExample = true;
                        else if (action.equals("Attempt") && !perProblemReportBeanObj.isExample) {
                            if (!isCorrect) {
                                if (userInput.equalsIgnoreCase("a"))
                                    perProblemReportBeanObj.nA++;
                                    else if (userInput.equalsIgnoreCase("b"))
                                    perProblemReportBeanObj.nB++;
                                    else if (userInput.equalsIgnoreCase("c"))
                                    perProblemReportBeanObj.nC++;
                                    else if (userInput.equalsIgnoreCase("d"))
                                    perProblemReportBeanObj.nD++;
                            }
                        }
                        return perProblemReportBeanObj;
                    }
                });
                if (perProblemReportBeanObj.nA > perProblemReportBeanObj.nB && perProblemReportBeanObj.nA > perProblemReportBeanObj.nC && perProblemReportBeanObj.nA > perProblemReportBeanObj.nD)
                    perProblemReportBeanObj.setMostIncorrectResponse("A");
                else if (perProblemReportBeanObj.nB > perProblemReportBeanObj.nA && perProblemReportBeanObj.nB > perProblemReportBeanObj.nC && perProblemReportBeanObj.nB > perProblemReportBeanObj.nD)
                    perProblemReportBeanObj.setMostIncorrectResponse("B");
                else if (perProblemReportBeanObj.nC > perProblemReportBeanObj.nA && perProblemReportBeanObj.nC > perProblemReportBeanObj.nB && perProblemReportBeanObj.nC > perProblemReportBeanObj.nD)
                    perProblemReportBeanObj.setMostIncorrectResponse("C");
                else if (perProblemReportBeanObj.nD > perProblemReportBeanObj.nA && perProblemReportBeanObj.nD > perProblemReportBeanObj.nB && perProblemReportBeanObj.nD > perProblemReportBeanObj.nC)
                    perProblemReportBeanObj.setMostIncorrectResponse("D");
                else perProblemReportBeanObj.setMostIncorrectResponse("-");

            });

        return perProblemReportBeanMap;
    }



    @Override
    public Map<String, PerProblemReportBean> generatePerProblemReportForClass(String classId) {
        Map<String, String> selectParams = new LinkedHashMap<String, String>();
        Map<String, String> problemDescriptionMap = new LinkedHashMap<String, String>();
        selectParams.put("classId", classId);
        List<String> problemIdsList = namedParameterJdbcTemplate.query(TTUtil.PER_PROBLEM_QUERY_FIRST, selectParams, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                problemDescriptionMap.put(resultSet.getString("problemID"),resultSet.getString("name")
                        +"~~"+resultSet.getString("screenShotURL")+"~~"+resultSet.getString("standardID")+":"+resultSet.getString("standardCategoryName")+":"+resultSet.getString("description"));
                return resultSet.getString("problemID");
            }
        });
        return  generatePerProblemReportForGivenProblemID(classId,problemIdsList,problemDescriptionMap);
    }

    @Override
    public List<EditStudentInfoForm> printStudentTags(String studentPassword, String classId) throws TTCustomException {
        Map<String, String> selectParams = new LinkedHashMap<String, String>();
        selectParams.put("classId", classId);
        List<EditStudentInfoForm> studentInfoList = namedParameterJdbcTemplate.query(TTUtil.GET_STUDENTS_INFO_FOR_CLASS, selectParams, new RowMapper<EditStudentInfoForm>() {
            @Override
            public EditStudentInfoForm mapRow(ResultSet resultSet, int i) throws SQLException {
                EditStudentInfoForm editInfoForm = new EditStudentInfoForm(resultSet.getInt("id"),resultSet.getString("fname"),resultSet.getString("lname"),resultSet.getString("userName"));
                editInfoForm.setClassName(resultSet.getString("name"));
                editInfoForm.setClassPassword(studentPassword);
                return editInfoForm;
            }
        });
        return studentInfoList;
    }


    private boolean validateEnteredPassWordForClass(String studentPassword, String classId) {
        String token = studentPassword;
        String passWordFromTeacher = PasswordAuthentication.getInstance().hash(token.toCharArray());
        Map<String, String> selectParams = new LinkedHashMap<String, String>();
        selectParams.put("classId", classId);
        String passWordInDatabase = namedParameterJdbcTemplate.queryForObject(TTUtil.VALIDATE_STUDENT_PASSWORD_TO_DOWNLOAD, selectParams, String.class);
        if (passWordFromTeacher.equals(passWordInDatabase))
            return true;
        else
            return false;
    }
    

    @Override
    public List<TeacherLogEntry> generateTeacherLogReport(String targetId) {
    	// Note: Target teacherID is passed from requester in the ClassId parameter. 
        SqlParameterSource namedParameters = new MapSqlParameterSource("targetId", targetId);
        List<TeacherLogEntry> teacherLogEntries = (List) namedParameterJdbcTemplate.query(TTUtil.TEACHER_LOG_QUERY_FIRST, namedParameters, new TeacherLogEntryMapper());
        return teacherLogEntries;
    }
 
    private String formatTeacherLogAction() {
    	String formatted = "";
    	return formatted;
    }
    
    @Override
    public List<TeacherListEntry> generateTeacherList(String targetId) {
    	// Note: Target teacherID is passed from requester in the ClassId parameter. 
        SqlParameterSource namedParameters = new MapSqlParameterSource("targetId", targetId);
        List<TeacherListEntry> teacherListEntries = (List) namedParameterJdbcTemplate.query(TTUtil.TEACHER_LIST_QUERY_FIRST, namedParameters, new TeacherListEntryMapper());
        return teacherListEntries;
    }
    @Override
    public List<TeacherClassListEntry> generateTeacherClassList(String teacherId) {
    	// Note: Target teacherID is passed from requester in the ClassId parameter. 
        SqlParameterSource namedParameters = new MapSqlParameterSource("teacherId", teacherId);
        List<TeacherClassListEntry> teacherClassListEntries = (List) namedParameterJdbcTemplate.query(TTUtil.TEACHER_CLASSLIST_QUERY, namedParameters, new TeacherClassListEntryMapper());
        return teacherClassListEntries;
    }

    private Timestamp xDaysAgoToDate(String filter) {
		Timestamp ts = null;

		int xDays = 0;
		try {
			xDays = Integer.parseInt(filter.trim());
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (xDays == 0) {
			xDays = 365;
		}
		Date currentDate = new Date();
		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.DAY_OF_MONTH, (xDays * -1));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		// convert calendar to date
		Date xDate = c.getTime();
		ts = new Timestamp(xDate.getTime());
		return ts;
    }

    private Timestamp defaultFromDate() {
		Timestamp ts = null;
		
		Date currentDate = new Date();
		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);

		c.add(Calendar.DAY_OF_MONTH,-365);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		// convert calendar to date
		Date xDate = c.getTime();
		ts = new Timestamp(xDate.getTime());
		return ts;
    }

    private Timestamp defaultToDate() {
		Timestamp ts = null;
		
		int xDays = 365;
		Date currentDate = new Date();
		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 1);
		// convert calendar to date
		Date xDate = c.getTime();
		ts = new Timestamp(xDate.getTime());
		return ts;
    }

 
    private Timestamp convertFromDate(String filter) {
		Timestamp ts = null;

		try {
			Date fDate=new SimpleDateFormat("MM/dd/yyyy").parse(filter);
			Calendar c = Calendar.getInstance();
			c.setTime(fDate);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 1);
			// convert calendar to date
			Date xDate = c.getTime();
			ts = new Timestamp(xDate.getTime());
		}
		catch (Exception e) {
			
		}
		return ts;
    }

    private Timestamp convertToDate(String filter) {
		Timestamp ts = null;

		try {
			Date fDate=new SimpleDateFormat("MM/dd/yyyy").parse(filter);
			Calendar c = Calendar.getInstance();
			c.setTime(fDate);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			// convert calendar to date
			Date xDate = c.getTime();
			ts = new Timestamp(xDate.getTime());
		}
		catch (Exception e) {
			
		}
		return ts;
    }


    
}