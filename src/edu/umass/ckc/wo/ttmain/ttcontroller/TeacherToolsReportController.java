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

/**
 * Created by nsmenon on 5/19/2017.
 * 
 * Frank 	10-15-19	Issue #7 perStudentperProblemReport report
 * Frank 	11-25-19	Issue #13 add standards filter for per student per problem report
 * Frank 	12-10-19	Issue #21 add teacher logging by using the request object to get the TeacherLogger object
 * Frank 	02-24-20	Issue #21 convert to autowired implementation
 * Frank 	06-17-20	Issue #149
 */
@Controller
public class TeacherToolsReportController {

    @Autowired
    private TTReportService reportService;

    @Autowired
    private TeacherLogger tLogger;


    @RequestMapping(value = "/tt/getTeacherReports", method = RequestMethod.POST)
    public @ResponseBody String getTeacherReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("reportType") String reportType,  @RequestParam("lang") String lang,  @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
    	return reportService.generateTeacherReport(teacherId, classId, reportType, lang, filter);

    }

    @RequestMapping(value = "/tt/getCompleteMasteryProjectionForStudent", method = RequestMethod.POST)
    public @ResponseBody
    String getCompleteMasteryProjectionForStudent(ModelMap map,@RequestParam("classId") String classId, @RequestParam("chartType") String chartType, @RequestParam("studentId") String studentId, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "CompleteMasteryProjectionForStudent", classId+" "+studentId);
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        return reportService.getCompleteMasteryProjectionForStudent(classId,studentId,chartType);
    }

    @RequestMapping(value = "/tt/getMasterProjectionsForCurrentTopic", method = RequestMethod.POST)
    public @ResponseBody
    String getMasterProjectionsForCurrentTopic(ModelMap map,@RequestParam("classId") String classId, @RequestParam("topicID") String topicID, @RequestParam("studentId") String studentId, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "MasterProjectionsForCurrentTopic", "Topic: " + topicID);
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        return reportService.getMasterProjectionsForCurrentTopic(classId,studentId,topicID);
    }


    @RequestMapping(value = "/tt/getProblemDetailsPerCluster", method = RequestMethod.POST)
    public @ResponseBody
    String getProblemDetailsPerCluster(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("clusterId") String clusterId, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0,  classId, "Problems In Cluster", "Cluster: " + clusterId);
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        return reportService.generateReportForProblemsInCluster(teacherId, classId, clusterId);
    }

    @RequestMapping(value = "/tt/downLoadPerStudentReport", method = RequestMethod.GET)
       public ModelAndView downLoadPerStudentReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerStudentReport", "");
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        List<ClassStudents> classStudents =  reportService.generateClassReportPerStudent(teacherId, classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("levelOneData",classStudents );
        Map<String,String> studentIdMap = classStudents.stream().collect( Collectors.toMap(studMap -> studMap.getStudentId(), studMap -> studMap.getNoOfProblems()));
        map.addAttribute("levelTwoData",reportService.generateEfortMapValues(studentIdMap,classId));
        map.addAttribute("reportType", "perStudentReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerProblemSetReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerProblemSetReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerProblemSetReport", "");
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        Map<String, Object> dataPerProblemSet =  reportService.generateClassReportPerStudentPerProblemSet(teacherId,classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblemSet",dataPerProblemSet );
        map.addAttribute("reportType", "perProblemSetReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerStudentPerProblemReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerStudentPerProblemReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0,  classId, "downLoadPerStudentPerProblemReport", filter);
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        Map<String, Object> dataPerStudentPerProblem =  reportService.generateClassReportPerStudentPerProblem(teacherId,classId, filter);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForStudentPerProblem",dataPerStudentPerProblem );
        map.addAttribute("reportType", "perStudentperProblemReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerProblemReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerProblemReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerProblemReport", "");
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        Map<String, PerProblemReportBean> perProblemReport =  reportService.generatePerProblemReportForClass(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perProblemReport);
        map.addAttribute("reportType", "perProblemReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerClusterReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerClusterReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerClusterReport", "");
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        Map<String, PerClusterObjectBean> perClusterReport =  reportService.generatePerCommonCoreClusterReport(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perClusterReport);
        map.addAttribute("reportType", "perClusterReport");
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/downloadStudentEmotions", method = RequestMethod.GET)
    public ModelAndView downloadStudentEmotions(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downloadStudentEmotions", "");
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        Map<String, List<String[]>> perStdentReport =  reportService.generateEmotionsReportForDownload(teacherId,classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("reportType", "perStudentEmotion");
        map.addAttribute("dataForEmotionReport",perStdentReport);
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/printStudentTags", method = RequestMethod.GET)
    public ModelAndView printStudentTags(ModelMap map, @RequestParam("classId") String classId,@RequestParam("formdata") String formData, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0,  classId, "printStudentTags", "");
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        List<EditStudentInfoForm> studentInfoForTags =  reportService.printStudentTags(formData,classId);
        map.addAttribute("classId", classId);
        map.addAttribute("dataForProblem",studentInfoForTags);
        map.addAttribute("reportType", "studentInfoDownload");
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/downLoadPerSummSurReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerPerSummSurReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
    	try {
        	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, classId, "downLoadPerSummSurReport", "");
    	}
    	catch (Exception e) {
    		System.out.println("TeacherLogger error " + e.getMessage());
    	}
        Map<String, Map<Integer, StudentDetails>> perSummSurReport =  reportService.generateSurveyReport(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perSummSurReport);
        map.addAttribute("reportType", "perSummSurveyReport");
        return new ModelAndView("teachersReport", map);
    }
}
