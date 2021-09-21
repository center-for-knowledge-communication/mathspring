package edu.umass.ckc.wo.ttmain.ttcontroller;

import edu.umass.ckc.wo.beans.StudentDetails;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTReportService;
import edu.umass.ckc.wo.ttmain.ttservice.miscservice.TTMiscService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TeacherLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Frank on 08/05/2021.
 * 

 */
@Controller
public class TeacherToolsResearchController {

    @Autowired
    private TTMiscService miscService;
   
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

    @RequestMapping(value = "/tt/cohortAdmin", method = RequestMethod.POST)
    public @ResponseBody String cohortAdmin(ModelMap map, @RequestParam("cohortId") String cohortId, @RequestParam("command") String command,  @RequestParam("lang") String lang, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {


    	return miscService.cohortAdmin(cohortId, command, lang, filter);
    }

}
