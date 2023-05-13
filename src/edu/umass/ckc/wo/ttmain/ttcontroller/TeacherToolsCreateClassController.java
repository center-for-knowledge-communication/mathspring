package edu.umass.ckc.wo.ttmain.ttcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTCreateClassAssistService;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TeacherLogger;
import edu.umass.ckc.wo.ttmain.ttservice.loggerservice.TTLoggerService;

/**
 * Created by Neeraj on 3/26/2017.
 * 
 * Frank	07-08-20	issue #134 & #156 added isActive flag handling and editClass method
 * Frank	08-08-20	issue #51 return to home page after class creation
 * Frank	10-02-20	issue #254R2 change to return to home page after modifying class info
 * Frank	10-02-20	issue #267 detect when grade(s) selected has changed
 * Frank	10-27-20	issue #149R2 teacher logging changes
 * Kartik	11-02-20	issue #292 test users to be created on class creation
 * Frank	11-12-20    issue #276 suppress logging if logged in as Master
 * Frank 	10-09-2021	issue #528 Research Tool
 * Frank 	02-04-23    Issue #723 - Added class clustering 
 * Frank	05-13-23	Issue #763 make LCs selectable by class
 */


@Controller
public class TeacherToolsCreateClassController {

    @Autowired
    private TTLoginService loginService;

    @Autowired
    private TTCreateClassAssistService createClassAssistService;

    @Autowired
    private TeacherLogger tLogger;

    @RequestMapping(value = "/tt/ttCreateClass", method = RequestMethod.POST)
    public String createNewClass( HttpServletRequest request, @RequestParam("teacherId") String teacherId, @ModelAttribute("createClassForm") CreateClassForm classForm, ModelMap model) throws TTCustomException {

		HttpSession session = request.getSession();
		String teacherLoginType = (String) session.getAttribute("teacherLoginType");

		if (classForm.getHasClusters().equals("Y") ) {
			classForm.setHasClusters("1");
		}
		else { 
			classForm.setHasClusters("0");
		}
        //Basic Class Setup
        int newClassId = createClassAssistService.createNewClass(classForm, teacherId);
        //Set Default Pedagogy
        ClassInfo newClassInfo = createClassAssistService.addDefaultPedagogy(newClassId, classForm, "create");
        //Add Student Roster and Finish setup
        
        if (!("".equals(classForm.getUserPrefix())) && classForm.getUserPrefix() != null
                && !("".equals(classForm.getPasswordToken())) && classForm.getPasswordToken() != null
                && classForm.getNoOfStudentAccountsForClass() > 0)
            createClassAssistService.createStudentRoster(newClassId, newClassInfo, classForm);
        
        // Creating Test Users for class
        int testUserCount = 2;
        createClassAssistService.createTestUsers(newClassId,newClassInfo, testUserCount);
        
        createClassAssistService.changeDefaultProblemSets(model, newClassId);
        
        if (classForm.getHasClusters().equals("1")) {
        	createClassAssistService.addNewMasterClass(newClassId);
        }
        //Control Back to DashBoard with new Class visible
        loginService.populateClassInfoForTeacher(model, Integer.valueOf(teacherId), teacherLoginType);
        model.addAttribute("createClassForm", new CreateClassForm());
        
    	int intTeacherId = Integer.valueOf(teacherId);
    	String strNewClassId = String.valueOf(newClassId);

		if ("Normal".equals(teacherLoginType)) {    	
			tLogger.logEntryWorker(intTeacherId, 0, strNewClassId, "createClass", "");
		}
		
        return "teacherTools/teacherToolsMain";

    }

    @RequestMapping(value = "/tt/ttEditClass", method = RequestMethod.POST)
    public String editClass( HttpServletRequest request, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @ModelAttribute("createClassForm") CreateClassForm classForm, ModelMap model) throws TTCustomException {

		HttpSession session = request.getSession();
		String teacherLoginType = (String) session.getAttribute("teacherLoginType");
		
    	int intClassId = Integer.valueOf(classId.trim());

        //Basic Class Setup
        boolean update = createClassAssistService.editClass(classForm, teacherId, intClassId);

        if (update) {
        //Set Default Pedagogy
        	ClassInfo newClassInfo = createClassAssistService.addDefaultPedagogy(intClassId, classForm, "edit");
        }
        createClassAssistService.changeDefaultProblemSets(model, intClassId);
        //Control Back to DashBoard with new Class visible
        loginService.populateClassInfoForTeacher(model, Integer.valueOf(teacherId),teacherLoginType);
        model.addAttribute("createClassForm", new CreateClassForm());

    	int intTeacherId = Integer.valueOf(teacherId);
		if ("Normal".equals(teacherLoginType)) {    	
			tLogger.logEntryWorker(intTeacherId, 0, classId, "editClass", "");
		}
 		return "teacherTools/teacherToolsMain";

    }

    
    
    @RequestMapping(value = "/tt/ttCloneClass", method = RequestMethod.POST)
    public String cloneExistingClass(@RequestParam("classId") String classId, @RequestParam("teacherId") String teacherId, @ModelAttribute("createClassForm") CreateClassForm classForm, ModelMap model) throws TTCustomException {
        //Clone Existing Class
    	
    	
    	
        //Set Default Pedagogy
        //ClassInfo newClassInfo = createClassAssistService.addDefaultPedagogy(Integer.valueOf(classId) , classForm, "create");
        int result = createClassAssistService.cloneExistingClass(Integer.valueOf(classId.trim()), classForm);

        if (result == 0) {
        	return "*** clone error";
        }
        else {
//        createClassAssistService.cloneExistingClass(Integer.valueOf(classId.trim()), classForm);
        	return loginService.populateClassInfoForTeacher(model, Integer.valueOf(teacherId), "Normal");
        }
    }


    @RequestMapping(value = "/tt/ttResetSurvey", method = RequestMethod.POST)
    public String resetSurveySettings(HttpServletRequest request, @RequestParam("classId") String classId, @RequestParam("teacherId") String teacherId, @ModelAttribute("createClassForm") CreateClassForm classForm, ModelMap model) throws TTCustomException {
        //Clone Existing Class
         createClassAssistService.restSurveySettings(Integer.valueOf(classId.trim()), classForm);
     	int intTeacherId = Integer.valueOf(teacherId);
		HttpSession session = request.getSession();
		String teacherLoginType = (String) session.getAttribute("teacherLoginType");
		if ("Normal".equals(teacherLoginType)) {    	
			tLogger.logEntryWorker(intTeacherId, 0, classId, "resetSurvey", "");
		}
        return loginService.populateClassInfoForTeacher(model, Integer.valueOf(teacherId),teacherLoginType);

    }

    @RequestMapping(value = "/tt/setClassActiveFlag", method = RequestMethod.GET)
    String setClassActiveFlag(HttpServletRequest request, @RequestParam(value = "teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("activeFlag") String activeFlag, ModelMap model) throws TTCustomException {
		HttpSession session = request.getSession();
		String teacherLoginType = (String) session.getAttribute("teacherLoginType");
		
  		createClassAssistService.setClassActiveFlag(Integer.valueOf(teacherId), Integer.valueOf(classId), activeFlag);
    	int intTeacherId = Integer.valueOf(teacherId);
    	String cmd = "deactivate";
    	if (activeFlag.equals("1") ) {
    		cmd = "activate";
    	}
    	String logMsg = "{ \"cmd\" : \"" + cmd + "\" }";
    			
		if ("Normal".equals(teacherLoginType)) {    	
			tLogger.logEntryWorker(intTeacherId, 0, classId, "setClassActiveStatus", logMsg);
		}
        return loginService.populateClassInfoForTeacher(model, Integer.valueOf(teacherId),teacherLoginType);
       
    }

}
