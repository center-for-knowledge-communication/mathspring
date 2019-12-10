package edu.umass.ckc.wo.ttmain.ttcontroller;

import edu.umass.ckc.wo.beans.StudentDetails;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTReportService;
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
import edu.umass.ckc.wo.log.TeacherLogger;

/**
 * Created by nsmenon on 5/19/2017.
 * 
 * Frank 	10-15-19	Issue #7 perStudentperProblemReport report
 * Frank 	11-25-19	Issue #13 add standards filter for per student per problem report
 * Frank 	12-10-19	Issue #21 add teacher logging by using the request object to get the TeacherLogger object
 */
@Controller
public class TeacherToolsReportController {

    @Autowired
    private TTReportService reportService;


    @RequestMapping(value = "/tt/getTeacherReports", method = RequestMethod.POST)
    public @ResponseBody String getTeacherReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("reportType") String reportType,  @RequestParam("lang") String lang,  @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
    	//  	System.out.println("Request = " + request.toString());
    	//  	System.out.println("Request = " + request.getSession().getAttribute("conn").toString());
    	  //  	int intId = (int) request.getSession().getAttribute("teacherId");
    	  //  	System.out.println("intId=" + intId);
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, reportType, classId);

    	System.out.println("TeacherToolsReportController filter = " + filter);
    	return reportService.generateTeacherReport(teacherId, classId, reportType, lang, filter);

    }

    @RequestMapping(value = "/tt/getCompleteMasteryProjectionForStudent", method = RequestMethod.POST)
    public @ResponseBody
    String getCompleteMasteryProjectionForStudent(ModelMap map,@RequestParam("classId") String classId, @RequestParam("chartType") String chartType, @RequestParam("studentId") String studentId, HttpServletRequest request) throws TTCustomException {
    	System.out.println("getCompleteMasteryProjectionForStudent");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "CompleteMasteryProjectionForStudent", classId+" "+studentId);
        return reportService.getCompleteMasteryProjectionForStudent(classId,studentId,chartType);
    }

    @RequestMapping(value = "/tt/getMasterProjectionsForCurrentTopic", method = RequestMethod.POST)
    public @ResponseBody
    String getMasterProjectionsForCurrentTopic(ModelMap map,@RequestParam("classId") String classId, @RequestParam("topicID") String topicID, @RequestParam("studentId") String studentId, HttpServletRequest request) throws TTCustomException {
    	System.out.println("getMasterProjectionsForCurrentTopic");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "MasterProjectionsForCurrentTopic", topicID);
        return reportService.getMasterProjectionsForCurrentTopic(classId,studentId,topicID);
    }


    @RequestMapping(value = "/tt/getProblemDetailsPerCluster", method = RequestMethod.POST)
    public @ResponseBody
    String getProblemDetailsPerCluster(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("clusterId") String clusterId, HttpServletRequest request) throws TTCustomException {
    	System.out.println("generateReportForProblemsInCluster");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "ProblemsInCluster", clusterId);
        return reportService.generateReportForProblemsInCluster(teacherId, classId, clusterId);
    }

    @RequestMapping(value = "/tt/downLoadPerStudentReport", method = RequestMethod.GET)
       public ModelAndView downLoadPerStudentReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) {
    	System.out.println("downLoadPerStudentReport");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "downLoadPerStudentReport", classId);
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
    	System.out.println("downLoadPerProblemSetReport");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "downLoadPerProblemSetReport", classId);
        Map<String, Object> dataPerProblemSet =  reportService.generateClassReportPerStudentPerProblemSet(teacherId,classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblemSet",dataPerProblemSet );
        map.addAttribute("reportType", "perProblemSetReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerStudentPerProblemReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerStudentPerProblemReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, @RequestParam("filter") String filter, HttpServletRequest request) throws TTCustomException {
    	System.out.println("downLoadPerStudentPerProblemReport");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "downLoadPerStudentPerProblemReport", classId);
        Map<String, Object> dataPerStudentPerProblem =  reportService.generateClassReportPerStudentPerProblem(teacherId,classId, filter);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForStudentPerProblem",dataPerStudentPerProblem );
        map.addAttribute("reportType", "perStudentperProblemReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerProblemReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerProblemReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
    	System.out.println("downLoadPerProblemReport");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "downLoadPerProblemReport", classId);
        Map<String, PerProblemReportBean> perProblemReport =  reportService.generatePerProblemReportForClass(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perProblemReport);
        map.addAttribute("reportType", "perProblemReportDownload");
        return new ModelAndView("teachersReport", map);
    }

    @RequestMapping(value = "/tt/downLoadPerClusterReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerClusterReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) {
    	System.out.println("downLoadPerClusterReport");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "downLoadPerClusterReport", classId);
        Map<String, PerClusterObjectBean> perClusterReport =  reportService.generatePerCommonCoreClusterReport(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perClusterReport);
        map.addAttribute("reportType", "perClusterReport");
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/downloadStudentEmotions", method = RequestMethod.GET)
    public ModelAndView downloadStudentEmotions(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
    	System.out.println("downloadStudentEmotions");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "downloadStudentEmotions", classId);
        Map<String, List<String[]>> perStdentReport =  reportService.generateEmotionsReportForDownload(teacherId,classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("reportType", "perStudentEmotion");
        map.addAttribute("dataForEmotionReport",perStdentReport);
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/printStudentTags", method = RequestMethod.GET)
    public ModelAndView printStudentTags(ModelMap map, @RequestParam("classId") String classId,@RequestParam("formdata") String formData, HttpServletRequest request) throws TTCustomException {
    	System.out.println("printStudentTags");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "printStudentTags", "");
        List<EditStudentInfoForm> studentInfoForTags =  reportService.printStudentTags(formData,classId);
        map.addAttribute("classId", classId);
        map.addAttribute("dataForProblem",studentInfoForTags);
        map.addAttribute("reportType", "studentInfoDownload");
        return new ModelAndView("teachersReport", map);

    }

    @RequestMapping(value = "/tt/downLoadPerSummSurReport", method = RequestMethod.GET)
    public ModelAndView downLoadPerPerSummSurReport(ModelMap map, @RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId, HttpServletRequest request) throws TTCustomException {
    	System.out.println("downLoadPerSummSurReport");
    	TeacherLogger tLogger = (TeacherLogger) request.getSession().getAttribute("tLogger");
    	tLogger.logEntryWorker((int) request.getSession().getAttribute("teacherId"), 0, "downLoadPerSummSurReport", "");
        Map<String, Map<Integer, StudentDetails>> perSummSurReport =  reportService.generateSurveyReport(classId);
        map.addAttribute("classId", classId);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("dataForProblem",perSummSurReport);
        map.addAttribute("reportType", "perSummSurveyReport");
        return new ModelAndView("teachersReport", map);
    }
}
