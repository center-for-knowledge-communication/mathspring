
package edu.umass.ckc.wo.ttmain.ttservice.classservice;

import edu.umass.ckc.wo.beans.StudentDetails;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassStudents;
import edu.umass.ckc.wo.ttmain.ttmodel.TeacherLogEntry;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttmodel.PerProblemReportBean;
import org.w3c.dom.Document;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by nsmenon on 5/19/2017.
 * 
 * Frank 	10-15-19	Issue #7 perStudentperProblemReport report
 * Frank 	11-25-19	Issue #13 add standards filter for per student per problem report
 * Frank	12-21-19	Issue #21 this file is being re-released with issue 21 to correct EOL characters which were inadvertently changed to unix style
 *						  The entire file should be replaced during 'pull request & comparison' process.
 * Frank	02-16-2019  Issue #45 Teacher Log report
 */

public interface TTReportService {
    public String generateTeacherReport(String teacherId, String classId, String reportType, String lang, String filter) throws TTCustomException;

    public Map<String,List<String[]>> generateEmotionsReportForDownload(String teacherId, String classId) throws TTCustomException;

    public Map<String,PerClusterObjectBean> generatePerCommonCoreClusterReport(String classId);

    Map<String, List<Document>> generateEmotionMapValues(Map<String, String> studentIds) throws TTCustomException;

    public Map<String,Map<String, List<String>>> generateEfortMapValues(Map<String, String> studentIds, String classId);

    public List<ClassStudents> generateClassReportPerStudent(String teacherId, String classId);

    public Map<String,Object> generateClassReportPerStudentPerProblemSet(String teacherId, String classId) throws TTCustomException;

    public Map<String,Object> generateClassReportPerStudentPerProblem(String teacherId, String classId, String filter) throws TTCustomException;

    public String getMasterProjectionsForCurrentTopic(String classId, String studentId, String topicID) throws TTCustomException;

    public String getCompleteMasteryProjectionForStudent(String classId, String studentId, String chartType) throws TTCustomException;

    public String generateReportForProblemsInCluster(String teacherId, String classId, String clusterId) throws TTCustomException;

    public Map<String, PerProblemReportBean> generatePerProblemReportForClass(String classId) throws TTCustomException;

    public List<EditStudentInfoForm> printStudentTags(String studentPassword, String classId) throws TTCustomException;
    
    public Map<String, Map<Integer,StudentDetails>> generateSurveyReport(String classId) throws TTCustomException;
    
    public List<TeacherLogEntry> generateTeacherLogReport(String targetId);
}
