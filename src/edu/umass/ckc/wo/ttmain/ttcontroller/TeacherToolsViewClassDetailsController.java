package edu.umass.ckc.wo.ttmain.ttcontroller;

import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.login.LoginParams;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.ProblemsView;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTCreateClassAssistService;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTProblemsViewService;
import edu.umass.ckc.wo.ttmain.ttservice.loggerservice.TTLoggerService;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.impl.TTLoginServiceImpl;
import edu.umass.ckc.wo.ttmain.ttservice.util.TeacherLogger;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Neeraj on 3/31/2017.
 * Frank	07-08-20	issue #134 & #156 added isClassInUse method
 * Frank	07-28-20	issue #74 Force logout if url tampered with
 * Frank	08-10-20	issue #196 add error checking
 * Frank	08-20-20	Issue #49 added method deleteInactiveStudents()
 * Frank	10-12-20	Issue #272 handle split of classDetails.jsp
 * Frank	10-27-20	Issue #149R2 teacher logging in JSO format
 * Frank	11-12-20    issue #276 suppress logging if logged in as Master
 * Frank 	12-02-20	issue #322 fixed and enhanced URL manipulation checking to include classId checking
 * Frank	01-17-21	issue #358 don't check classId 
 * Frank	01-03-21	issue #329R2 pass lang param to viewProblemSetsInGivenProblem()
 * Frank	01-17-21	issue #358R3 disallow multiple concurrent logins
 * Frank 	03-15-21  	Issue #398 New feature to move student from one class to another - added changeStudentClass method
 * Frank 	04-01-21  	Issue #418 getStudentList()
 * Frank 	05-17-21  	Issue #471 Pass teacherLoginType value in map to classReportCard
 */

@Controller
public class TeacherToolsViewClassDetailsController {

    private static Logger logger = Logger.getLogger(TTLoginServiceImpl.class);

    @Autowired
    private TTConfiguration connection;

    @Autowired
    private TTLoginService loginService;

    @Autowired
    private TTCreateClassAssistService ccService;

    @Autowired
    private TTProblemsViewService pvService;

    @Autowired
    private TeacherLogger tLogger;

    @RequestMapping(value = "/tt/viewClassDetails", method = RequestMethod.GET)
    public String viewClassDetails(ModelMap map, HttpServletRequest request, @RequestParam("classId") String classId,   @RequestParam("currentSelection") String currentSelection ) throws TTCustomException {

    	Locale loc = request.getLocale(); 
    	String lang = loc.getLanguage();

    	if (lang.equals("es")) {
    		loc = new Locale("es","AR");	
    	}
    	else {
    		loc = new Locale("en","US");	
    	}	
    	
    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}

    	HttpSession session = request.getSession();
    	int sTeacherId = (int) session.getAttribute("teacherId");
    	if (currentSelection.equals("classHomePage")) {
			try {
				int teacherId = 0;
    			teacherId = (int) session.getAttribute("teacherId");

    			if (!DbClass.validateClassTeacher(connection.getConnection(),Integer.valueOf(classId),sTeacherId)) {
	    			if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    				tLogger.logEntryWorker(teacherId, 0, "logout", "Multiple teacher logins detected");
	    			}
	    	    	session.removeAttribute("tLogger");
	    	    	session.removeAttribute("teacherUsername");
	    	    	session.removeAttribute("teacherId");
	    			session.invalidate();
	    			String msg = rb.getString("multiple_teacher_logins_detected");
	                request.setAttribute("message",msg);
	    	        return "login/loginK12_teacher";
	    		}
			}
			catch(SQLException e) {
				logger.debug(e.getMessage());
				String msg = rb.getString("system_error") + " " + rb.getString("log_in_and_try_again");
	            request.setAttribute("message",msg);
		        return "login/loginK12_teacher";			
			}
			catch(java.lang.NullPointerException ne) {
				logger.debug(ne.getMessage());
				String msg = rb.getString("system_error") + " " + rb.getString("log_in_and_try_again");
	            request.setAttribute("message",msg);
		        return "login/loginK12_teacher";			
			}
    	}
    	else {
        	if (currentSelection.equals("")) {    		
				int teacherId = (int) session.getAttribute("teacherId");
				if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
					tLogger.logEntryWorker(teacherId, 0, "logout", "Multiple teacher logins detected");
				}
		    	session.removeAttribute("tLogger");
		    	session.removeAttribute("teacherUsername");
		    	session.removeAttribute("teacherId");
				session.invalidate();
				String msg = rb.getString("system_error") + " " + rb.getString("log_in_and_try_again");
	            request.setAttribute("message",msg);
		        return "login/loginK12_teacher";
        	}
    	}

    	ccService.setTeacherInfo(map,String.valueOf(sTeacherId),classId);
   		ccService.changeDefaultProblemSets(map,Integer.valueOf(classId));
    	map.addAttribute("createClassForm", new CreateClassForm());
        map.addAttribute("teacherId", String.valueOf(sTeacherId));
        session.setAttribute("classId",classId);
        session.setAttribute("currentSelection",currentSelection);
        return "teacherTools/classManager";
    }
    
    @RequestMapping(value = "/tt/viewClassReportCard", method = RequestMethod.GET)
    public String viewClassReportCard(ModelMap map, HttpServletRequest request, @RequestParam("classId") String classId,   @RequestParam("currentSelection") String currentSelection  ) throws TTCustomException {

    	Locale loc = request.getLocale(); 
    	String lang = loc.getLanguage();

    	if (lang.equals("es")) {
    		loc = new Locale("es","AR");	
    	}
    	else {
    		loc = new Locale("en","US");	
    	}	
    	
    	ResourceBundle rb = null;
    	try {
    		rb = ResourceBundle.getBundle("MathSpring",loc);
    	}
    	catch (Exception e) {
//    		logger.error(e.getMessage());	
    	}

    	HttpSession session = request.getSession();
    	int sTeacherId = (int) session.getAttribute("teacherId");
    	if (currentSelection.equals("classReportCard")) {
			try {
	    		if (!DbClass.validateClassTeacher(connection.getConnection(),Integer.valueOf(classId),sTeacherId)) {
	    			if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    				tLogger.logEntryWorker(sTeacherId, 0, "logout", "Multiple teacher logins detected");
	    			}
	    	    	session.removeAttribute("tLogger");
	    	    	session.removeAttribute("teacherUsername");
	    	    	session.removeAttribute("teacherId");
	    			session.invalidate();
	    			String msg = rb.getString("multiple_teacher_logins_detected");
	                request.setAttribute("message",msg);
	    	        return "login/loginK12_teacher";
	    		}
			}
			catch(SQLException e) {
				logger.debug(e.getMessage());
				String msg = rb.getString("system_error") + " " + rb.getString("log_in_and_try_again");
	            request.setAttribute("message",msg);
		        return "login/loginK12_teacher";			
			}
			catch(java.lang.NullPointerException ne) {
				logger.debug(ne.getMessage());
				String msg = rb.getString("system_error") + " " + rb.getString("log_in_and_try_again");
	            request.setAttribute("message",msg);
		        return "login/loginK12_teacher";			
			}
    	}
    	else {   		
			int teacherId = (int) session.getAttribute("teacherId");
			if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
				tLogger.logEntryWorker(teacherId, 0, "logout", "Multiple teacher logins detected");
			}
	    	session.removeAttribute("tLogger");
	    	session.removeAttribute("teacherUsername");
	    	session.removeAttribute("teacherId");
			session.invalidate();
			String msg = rb.getString("system_error") + " " + rb.getString("log_in_and_try_again");
            request.setAttribute("message",msg);
	        return "login/loginK12_teacher";
    	}

    	ccService.setTeacherInfo(map,String.valueOf(sTeacherId),classId);
        map.addAttribute("createClassForm", new CreateClassForm());
        map.addAttribute("teacherId", String.valueOf(sTeacherId));
        map.addAttribute("teacherLoginType", (String) session.getAttribute("teacherLoginType"));
        session.setAttribute("classId",classId);
        return "teacherTools/classReportCard";
    }
    
    @RequestMapping(value = "/tt/reOrderProblemSets", method = RequestMethod.POST)
    public @ResponseBody  String  reOrderProblemSets(ModelMap map, HttpServletRequest request, @RequestParam(value = "problemSets[]") List<String> problemSets, @RequestParam(value = "classid") String classid) throws TTCustomException {
        List<Integer> sequenceNosToBeRemoved = new ArrayList<>();
        Map<Integer, Integer> insertSequences = new HashMap<>();
        for (String probsetEntries : problemSets) {
            String[] entries = probsetEntries.split("~~");
            insertSequences.put(Integer.valueOf(entries[0]), Integer.valueOf(entries[1]));
            sequenceNosToBeRemoved.add(Integer.valueOf(entries[2]));
        }
        ccService.reOrderProblemSets(Integer.valueOf(classid), sequenceNosToBeRemoved, insertSequences);
		HttpSession session = request.getSession();
		int teacherId = (int) session.getAttribute("teacherId");
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
			tLogger.logEntryWorker(teacherId, 0, classid,"reOrderTopics", "");
		}
        return "success";
    }

    @RequestMapping(value = "/tt/configureProblemSets", method = RequestMethod.POST)
    public @ResponseBody String activateProblemSets(ModelMap map, HttpServletRequest request, @RequestParam(value = "activateData[]") List<String> problemSets, @RequestParam(value = "classid") String classid,@RequestParam(value = "activateFlag") String activateFlag) throws TTCustomException {
        List<Integer> intProblemSets = problemSets.stream().map(Integer::parseInt).collect(Collectors.toList());
		HttpSession session = request.getSession();
		System.out.println(problemSets);
		String logMsg = "{ ";
		if (activateFlag.equals("deactivate")) {
			logMsg += "\"cmd\" : \"Deactivate\",";
			logMsg += "\"result\" : \"Remaining active topics on list\",";
	    	logMsg += "\"topicIds\" : \"" + problemSets.toString() + "\"";
		}
		else {
			logMsg += "\"cmd\" : \"Activate\",";
			logMsg += "\"result\" : \"Topics added to active list\",";
	    	logMsg += "\"topicIds\" : \"" + problemSets.toString() + "\"";
		}
		logMsg += " }";

		int teacherId = (int) session.getAttribute("teacherId");
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
			tLogger.logEntryWorker(teacherId, 0, classid, "reconfigureTopics", logMsg );
		}
        return ccService.activateDeactivateProblemSets(Integer.valueOf(classid), intProblemSets,activateFlag);
    }
    
    @RequestMapping(value = "/tt/continousContentApply", method = RequestMethod.POST)
    public @ResponseBody String continousContentApply(ModelMap map,  HttpServletRequest request, @RequestParam(value = "classesToApply[]") List<String> classIdList, @RequestParam(value = "classid") String classid,@RequestParam(value = "teacherId") String teacherId) throws TTCustomException {
        List<Integer> intClassIDList = classIdList.stream().map(Integer::parseInt).collect(Collectors.toList());

                
        String logMsg = "{ ";
        String idList = "[";

        logMsg += "\"result\" : \"CLasses updated\",";

        for (int i=0;i< intClassIDList.size();i++ ) {
        	if (i > 0) {
        		idList += ",";
        	}
        	idList += String.valueOf(intClassIDList.get(i));
        }
        idList = "]";
    	logMsg += "\"classes Updated\" : \"[" +idList + "]\"";
        logMsg += " }";
        
        HttpSession session = request.getSession();
		int teacherId2 = (int) session.getAttribute("teacherId");
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
			tLogger.logEntryWorker(teacherId2, 0, classid, "continousContentApply", logMsg);
		}
        return ccService.continousContentApply(intClassIDList,Integer.valueOf(classid),Integer.valueOf(teacherId));
    }

    @RequestMapping(value = "/tt/getProblemForProblemSets", method = RequestMethod.POST)
    public @ResponseBody  String viewProblemsForProblemSet(ModelMap map,  HttpServletRequest request, @RequestParam(value = "problemID") String problemId, @RequestParam(value = "classid") String classid) throws TTCustomException {

    	Locale loc = request.getLocale();
    	String lang = loc.getLanguage();

    	if (lang.equals("es")) {
    		loc = new Locale("es","AR");	
    	}
    	else {
    		loc = new Locale("en","US");	
    	}	
        try {
            ProblemsView pView = pvService.viewProblemSetsInGivenProblem(Integer.valueOf(problemId), Integer.valueOf(classid), lang);
            ObjectMapper objectMapp = new ObjectMapper();
            objectMapp.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        	System.out.println("getProblemForProblemSets = success");
            return objectMapp.writeValueAsString(pView);
        }catch (IOException e){
			logger.debug(e.getMessage());
            e.printStackTrace();
            
        }
    	System.out.println("getProblemForProblemSets = Failed");
        return "failed";
    }


    @RequestMapping(value = "/tt/saveChangesForProblemSet", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<String> saveChangsForproblemSets(@RequestParam(value = "problemIds[]") List<String> problemId,
                                                    @RequestParam(value = "classid") String classid, @RequestParam(value = "problemsetId") String problemsetId) throws TTCustomException {
          pvService.saveChangsForproblemSets(problemId,Integer.valueOf(classid),problemsetId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/tt/activatePrePostSurveys", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<String> activatePrePostSurveys(@RequestParam(value = "activatePrePostSurveys") String surveyIds,
                                                    @RequestParam(value = "classid") String classid) throws TTCustomException {
        String[] prePostToActivate = surveyIds.split(",");
        pvService.saveSurveySettingsForClass(prePostToActivate,Integer.valueOf(classid));
        return new ResponseEntity<String>(HttpStatus.OK);
    }


    @RequestMapping(value = "/tt/resetStudentdata", method = RequestMethod.POST)
    public @ResponseBody
    String resetStudentdata(@RequestParam(value = "studentId") String studentId, @RequestParam(value = "action") String action,  @RequestParam("lang") String lang) throws TTCustomException {
    	return pvService.resetStudentData(studentId,action,lang);
    }

    @RequestMapping(value = "/tt/deleteInactiveStudents", method = RequestMethod.POST)
    public @ResponseBody
    String deleteInactiveStudents(@RequestParam(value = "classId") String classId, @RequestParam(value = "action") String action,  @RequestParam("lang") String lang) throws TTCustomException {
    	return pvService.deleteInactiveStudents(classId,action,lang);
    }


    @RequestMapping(value = "/tt/resetStudentPassword", method = RequestMethod.POST)
    public @ResponseBody
    String resetStudentPassword(@RequestParam(value = "studentId") String studentId,@RequestParam(value = "userName") String userName, @RequestParam(value = "newPassWord") String newPassWord ) throws TTCustomException {
        return pvService.resetPassWordForStudent(studentId,userName,newPassWord);
    }

    @RequestMapping(value = "/tt/changeStudentClass", method = RequestMethod.POST)
    public @ResponseBody
    String changeStudentClass(@RequestParam(value = "studentId") String studentId, @RequestParam(value = "newClassId") String newClassId ) throws TTCustomException {
    	System.out.println("changeStudentClass");
    	return pvService.changeClassForStudent(studentId,newClassId);
    }
    
    
    @RequestMapping(value = "/tt/editStudentInfo", method = RequestMethod.POST)
    public @ResponseBody
    String editStudentInfo(@RequestParam(value = "studentId") String studentId,@RequestParam(value = "formData[]") String[] formData,  @RequestParam("lang") String lang) throws TTCustomException {
    	System.out.println("editStudentInfo");
        return pvService.editStudentInfo(new EditStudentInfoForm(Integer.valueOf(studentId.trim()),formData[1].trim(),formData[2].trim(),formData[0].trim()),lang);
    }


    @RequestMapping(value = "/tt/createMoreStudentIds", method = RequestMethod.POST)
    public @ResponseBody
    String createMoreStudentIds(ModelMap map,  HttpServletRequest request, @RequestParam(value = "formData[]") String[] formData,  @RequestParam("lang") String lang) throws TTCustomException {
    	System.out.println("createMoreStudentIds");
    	for (int i=0;i<formData.length;i++ ) {
        	System.out.println(formData[i].trim());    		
    	}
    	String message =  pvService.createAdditionalIdForClass(formData,lang);
        loginService.populateClassInfoForTeacher(map,Integer.valueOf(formData[3].trim()));
		HttpSession MySession = request.getSession();
		int teacherId = (int) MySession.getAttribute("teacherId");
       return message;
    }

    @RequestMapping(value = "/tt/isClassInUse", method = RequestMethod.POST)
    public @ResponseBody
    String isClassInUse(@RequestParam(value = "classId") String classId) {
    	return pvService.isClassInUse(classId);
    }

    
    @RequestMapping(value = "/tt/classLogTeacherEvent", method = RequestMethod.POST)
    public @ResponseBody
    String classLogTeacherEvent(@RequestParam(value = "teacherId") String teacherId,@RequestParam(value = "sessionId") String sessionId,@RequestParam(value = "classId") String classId,@RequestParam(value = "action") String action,@RequestParam(value = "activityName") String activityName) {
    	int intTeacherId = Integer.parseInt(teacherId);
    	int intSessionId =  Integer.parseInt(sessionId);
    	tLogger.logEntryWorker(intTeacherId, intSessionId, classId, action, activityName);
    	return "success";

    }

    @RequestMapping(value = "/tt/getStudentList", method = RequestMethod.POST)
    public @ResponseBody
    String getStudentList(@RequestParam(value = "classId") String classId) {
    	return pvService.getStudentList(classId);
    }

   
}
