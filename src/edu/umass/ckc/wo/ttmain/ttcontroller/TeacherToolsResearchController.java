package edu.umass.ckc.wo.ttmain.ttcontroller;


import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;

import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTCreateClassAssistService;
import edu.umass.ckc.wo.ttmain.ttservice.miscservice.TTMiscService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TeacherLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Frank on 08/05/2021.
 * 

 */
@Controller
public class TeacherToolsResearchController {

	
    @Autowired
    private TTConfiguration connection;

    @Autowired
    private TTMiscService miscService;

    @Autowired
    private TTCreateClassAssistService ccService;

    @Autowired
    private TeacherLogger tLogger;


    @RequestMapping(value = "/tt/researchServices", method = RequestMethod.POST)
    public @ResponseBody String getCohortTeachersClasses(ModelMap map, @RequestParam("cohortId") String cohortId, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("reportType") String reportType,  @RequestParam("lang") String lang,  @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {

    	if (reportType.equals("getCohortTeachersClasses")) {
    		return miscService.getCohortTeachersClasses(cohortId, teacherId, classId, reportType, lang, filter);
    	}
    	else {
    		return "";
    	}

    }

    @RequestMapping(value = "/tt/getCohorts", method = RequestMethod.POST)
    public @ResponseBody String getCohorts(ModelMap map, @RequestParam("reportType") String reportType,  @RequestParam("lang") String lang, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {

    	return miscService.getCohorts(reportType, lang, filter);
    }

    @RequestMapping(value = "/tt/getCohortReport", method = RequestMethod.POST)
    public @ResponseBody String getCohortReport(ModelMap map, @RequestParam("cohortId") String cohortId, @RequestParam("reportType") String reportType,  @RequestParam("lang") String lang, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {


    	return miscService.getCohortReport(cohortId, reportType, lang, filter);
    }

    @RequestMapping(value = "/tt/getResearcherHelp", method = RequestMethod.POST)
    public @ResponseBody String getResearcherHelp(ModelMap map, @RequestParam("helpTopic") String helpTopic,  @RequestParam("lang") String lang, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {


    	return miscService.getResearcherHelp(helpTopic, lang, filter);
    }

    @RequestMapping(value = "/tt/cohortAdmin", method = RequestMethod.POST)
    public @ResponseBody String cohortAdmin(ModelMap map, @RequestParam("cohortId") String cohortId, @RequestParam("command") String command,  @RequestParam("lang") String lang, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {


    	return miscService.cohortAdmin(cohortId, command, lang, filter);
    }

    @RequestMapping(value = "/tt/msAdmin", method = RequestMethod.POST)
    public @ResponseBody String msAdmin(ModelMap map, @RequestParam("command") String command,  @RequestParam("lang") String lang, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {


    	return miscService.msAdmin(command, lang, filter);
    }

    
    @RequestMapping(value = "/tt/researcherViewClassReportCard", method = RequestMethod.GET)
    public String researcherViewClassReportCard(ModelMap map, HttpServletRequest request, @RequestParam("teacherId") String teacherId,  @RequestParam("classId") String classId  ) throws TTCustomException {

	   	 Locale loc = request.getLocale(); 
	   	 String lang = loc.getLanguage();
	   	 String country = loc.getCountry();
	
	   	 System.out.println("locale set to:" + lang + "-" + country );	
	
	   	 if (!lang.equals("es")) {
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
    	session.setAttribute("teacherId",Integer.valueOf(teacherId));
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("teacherLoginType", (String) session.getAttribute("teacherLoginType"));
    	ccService.setTeacherInfo(map,teacherId,classId);
        map.addAttribute("createClassForm", new CreateClassForm()); 
        session.setAttribute("classId",classId);
        return "teacherTools/researcherClassReportCard";
    }

    
    @RequestMapping(value = "/tt/researcherMSAdmin", method = RequestMethod.GET)
    public String researcherMSAdmin(ModelMap map, HttpServletRequest request  ) throws TTCustomException {

	   	 Locale loc = request.getLocale(); 
	   	 String lang = loc.getLanguage();
	   	 String country = loc.getCountry();
	
	   	 System.out.println("locale set to:" + lang + "-" + country );	
	
	   	 if (!lang.equals("es")) {
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
        return "teacherTools/researcherMSAdmin";
    }
    
    
}
