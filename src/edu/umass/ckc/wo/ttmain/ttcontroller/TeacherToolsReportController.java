package edu.umass.ckc.wo.ttmain.ttcontroller;

import edu.umass.ckc.wo.beans.StudentDetails;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTReportService;
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
 * Created by nsmenon on 5/19/2017.
 * 
 * Frank 	10-15-19	Issue #7 perStudentperProblemReport report
 * Frank 	11-25-19	Issue #13 add standards filter for per student per problem report
 * Frank 	12-10-19	Issue #21 add teacher logging by using the request object to get the TeacherLogger object
 * Frank 	02-24-20	Issue #21 convert to autowired implementation
 * Frank 	06-17-20	Issue #149
 * Frank	07-28-20	issue #74 use session for teacher id and hack for teacher report 
 * Frank	08-15-20	Issue #148 added time period (days) filter for perStudentPerProblemSet report
 * Frank	11-12-20    issue #276 suppress logging if logged in as Master
 * Frank	11-23-20	Issue #148R3 add lastXdays filter to perCluster Report
 * Frank	05-11-21	Issue #463 add report filters

 */
@Controller
public class TeacherToolsReportController {

    @Autowired
    private TTReportService reportService;

    @Autowired
    private TeacherLogger tLogger;


    @RequestMapping(value = "/tt/getTeacherReports", method = RequestMethod.POST)
    public @ResponseBody String getTeacherReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("reportType") String reportType,  @RequestParam("lang") String lang,  @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {

    	HttpSession session = request.getSession();
    	int intTeacherId = (int) session.getAttribute("teacherId");
    	String teacherLoginType = (String) session.getAttribute("teacherLoginType");
    	String sTeacherId = String.valueOf(intTeacherId);
    	String sClassId = "";
    	
    	// Hack - using classId to hold 'Target teacher ID' for teacher activities reports so use URL param not session variable
    	if ( (!reportType.equals("teacherList")) && (!reportType.equals("perTeacherReport")) ) {
    		sClassId = (String) session.getAttribute("classId");
    	}
    	else {
    		sClassId = classId;
    	}
    	// end Hack
    	
    	return reportService.generateTeacherReport(sTeacherId, sClassId, reportType, lang, filter, teacherLoginType);

    }

    @RequestMapping(value = "/tt/getCompleteMasteryProjectionForStudent", method = RequestMethod.POST)
    public @ResponseBody
    String getCompleteMasteryProjectionForStudent(ModelMap map,@RequestParam("classId") String classId, @RequestParam("chartType") String chartType, @RequestParam("studentId") String studentId, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    	try {
	           	String logMsg = "{  \"studentId\" : \"" + studentId + "\" }";     
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "CompleteMasteryProjectionForStudent", logMsg);
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
        return reportService.getCompleteMasteryProjectionForStudent(classId,studentId,chartType);
    }

    @RequestMapping(value = "/tt/getMasterProjectionsForCurrentTopic", method = RequestMethod.POST)
    public @ResponseBody
    String getMasterProjectionsForCurrentTopic(ModelMap map,@RequestParam("classId") String classId, @RequestParam("topicID") String topicID, @RequestParam("filter") String filter, @RequestParam("studentId") String studentId, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    	try {
	    		String filters[] = filter.split("~");
	           	String logMsg = "{  \"topicId\" : \"" +  topicID + "\", \"nbrofdays\" : \"" +  filters[1].trim() + "\" }";
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "MasterProjectionsForCurrentTopic", logMsg);
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
    	return reportService.getMasterProjectionsForCurrentTopic(classId,studentId,topicID,filter);
    }


    @RequestMapping(value = "/tt/getProblemDetailsPerCluster", method = RequestMethod.POST)
    public @ResponseBody
    String getProblemDetailsPerCluster(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("clusterId") String clusterId, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
        return reportService.generateReportForProblemsInCluster(teacherId, classId, clusterId, filter, (String) session.getAttribute("teacherLoginType"));
    }

    @RequestMapping(value = "/tt/downLoadPerStudentReport", method = RequestMethod.GET)
       public ModelAndView downLoadPerStudentReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("filter") String filter, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    	try {
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerStudentReport", "");
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
        List<ClassStudents> classStudents =  reportService.generateClassReportPerStudent(teacherId, classId, filter);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("levelOneData",classStudents );
        Map<String,String> studentIdMap = classStudents.stream().collect( Collectors.toMap(studMap -> studMap.getStudentId(), studMap -> studMap.getNoOfProblems()));
        map.addAttribute("levelTwoData",reportService.generateEfortMapValues(studentIdMap,classId,filter));
        map.addAttribute("reportType", "perStudentReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerProblemSetReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerProblemSetReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
        Map<String, Object> dataPerProblemSet =  reportService.generateClassReportPerStudentPerProblemSet(teacherId,classId, filter, (String) session.getAttribute("teacherLoginType"));
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblemSet",dataPerProblemSet );
        map.addAttribute("reportType", "perProblemSetReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerStudentPerProblemReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerStudentPerProblemReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
        Map<String, Object> dataPerStudentPerProblem =  reportService.generateClassReportPerStudentPerProblem(teacherId,classId, filter, (String) session.getAttribute("teacherLoginType"));
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForStudentPerProblem",dataPerStudentPerProblem );
        map.addAttribute("reportType", "perStudentperProblemReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerProblemReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerProblemReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    	try {
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerProblemReport", "");
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
    	}
        Map<String, PerProblemReportBean> perProblemReport =  reportService.generatePerProblemReportForClass(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perProblemReport);
        map.addAttribute("reportType", "perProblemReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerClusterReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerClusterReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("filter") String filter,  HttpServletRequest request) {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    	try {
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerClusterReport", "");
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
		
        Map<String, PerClusterObjectBean> perClusterReport =  reportService.generatePerCommonCoreClusterReport(teacherId,classId,filter,(String) session.getAttribute("teacherLoginType"));
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perClusterReport);
        map.addAttribute("reportType", "perClusterReport");
        
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/downLoadStudentEmotions", method = RequestMethod.GET)
    public ModelAndView downLoadStudentEmotions(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    	try {
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadStudentEmotions", "");
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
        Map<String, List<String[]>> perStdentReport =  reportService.generateEmotionsReportForDownload(teacherId,classId, filter);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("reportType", "perStudentEmotion");
        map.addAttribute("dataForEmotionReport",perStdentReport);
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/printStudentTags", method = RequestMethod.GET)
    public ModelAndView printStudentTags(ModelMap map, @RequestParam("classId") String classId,@RequestParam("formdata") String formData, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
			try {
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0,  classId, "printStudentTags", "");
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
        List<EditStudentInfoForm> studentInfoForTags =  reportService.printStudentTags(formData,classId);
        map.addAttribute("classId", classId);
        map.addAttribute("dataForProblem",studentInfoForTags);
        map.addAttribute("reportType", "studentInfoDownload");
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/downLoadPerSummSurReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerPerSummSurReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
		HttpSession session = request.getSession();
		if ("Normal".equals((String) session.getAttribute("teacherLoginType"))) {
	    	try {
	        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerSummSurReport", "");
	    	}
	    	catch (Exception e) {
	    		System.out.println("TeacherLogger error " + e.getMessage());
	    	}
		}
        Map<String, Map<Integer, StudentDetails>> perSummSurReport =  reportService.generateSurveyReport(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perSummSurReport);
        map.addAttribute("reportType", "perSummSurveyReport");
        return new ModelAndView("teachersReport", map);
    }
}
