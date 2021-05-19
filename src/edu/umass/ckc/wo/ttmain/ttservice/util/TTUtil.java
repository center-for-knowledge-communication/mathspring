package edu.umass.ckc.wo.ttmain.ttservice.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.CCStandard;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbProblem;

/**
 * Created by Neeraj on 4/5/2017.
 * 
 * Frank 	10-15-19	Issue #7 perStudentperProblemReport report
 * Frank 	01-14-20	Issue #45 & #21 add sql query for log report
 * Frank    03-02-2020  Added teacherlist query string 
 * Frank 	06-13-2020 	issue #106 replace use of probstdmap
 * Frank 	06-17-20	Issue #149
 * Frank	08-15-20	Issue #148 added time period (days) filter for perStudentPerProblemSet report
 * Frank	10-01-20	Issue #254R2 restrict inactive topic list columns to selected 'grade', 'above' and 'below' selections
 * Frank	11-12-20	Issue #299 Class Landing Page Report queries
 * Frank	11-23-20	Issue #148R3 add lastXdays filter to perCluster Report
 * Frank    11-28-20	issue #318 Sort by Student Fname - landingReportOne
 * Frank	12-26-20	Issue #329 changed topic query to refer to problemgroup_description_multi_language table
 * Frank    03-15-21  	Issue #398 New feature to move student from one class to another - added getter for teacherClassList
 * Frank 	03-22-21  	Issue #391 change date selection to use BETWEEN for date range
 * Frank	05-11-21	Issue #463 add between dates clause to some queries for filtering
 */
public class TTUtil {
    private static TTUtil util = new TTUtil();

    /**
     * SQL Queries to Reorder Problem Sets as well as deactivate problem sets. The Passed problemset ID's are first removed and then inserted back as per their new Sequence position. For deactivate, I am clearing the plan and inserting only the ones that are left unchecked
     */
    public static final String DELETE_CLASS_PLAN = "DELETE FROM classlessonplan where seqPos IN (:seqPos) and classId=(:classId)";
    public static final String INSERT_ON_CLASS_PLAN = "INSERT INTO classlessonplan (classId, seqPos, probGroupId, isDefault) values (:classId, :seqPos, :probGroupId, :isDefault)";
    public static final String GET_CLASS_LESSON_PLAN = "select seqPos,probGroupId,isDefault from classlessonplan where classId=(:classId)";
    public static final String GET_CLASS_OMITTED_LIST = "select probId,topicId from classomittedproblems where classId=(:classId)";
    public static final String ACTIVATE_ON_CLASS_PLAN = "SELECT MAX(seqPos)+1 from classLessonPlan where classId=?";
    public static final String REMOVE_FROM_CLASSOMITTED_PROBLEMS =  "DELETE FROM ClassOmittedProblems where classId=(:classId) and topicId=(:problemSetId)";
    public static final String INSERT_ON_CLASSOMITTED_PROBLEMS = "INSERT INTO ClassOmittedProblems (classId,topicId,probId) values (:classId, :topicId, :probId)";
    public static final String UPDATE_PASSWORD_FOR_STUDENT = "UPDATE STUDENT SET password=(:resetPassword) where id=(:studentId)";
    public static final String UPDATE_CLASS_FOR_STUDENT = "UPDATE STUDENT SET classId=(:newClassId) where id=(:studentId)";
    public static final String UPDATE_STUDENT_INFO = "UPDATE STUDENT SET fname=(:fname),lname=(:lname),userName=(:uname) where id=(:studentId)";
    public static final String GET_STUDENTS_INFO_FOR_CLASS = "select s.id,s.fname,s.lname,s.userName,c.name from student s,class c where s.classId=c.id and s.classId=(:classId)";
    public static final String VALIDATE_STUDENT_PASSWORD_TO_DOWNLOAD = "select s.password from student s,class c where s.classId=c.id and s.classId=(:classId) order by s.id limit 1";
    public static final String PASSWORD_TOKEN = "M8$tek@12";

    /** SQL Queries for Class Config **/
    public static final String UPDATE_MAX_TIMEIN_TOPIC_FOR_CLASS = "UPDATE classconfig SET maxTimeInTopic=(:maxTimeInTopic) where classId=(:classId)";
    
    /** SQL Queries for Survey Settings **/
    public static final String UPDATE_SURVEY_SETTING_FOR_CLASS_ALL = "UPDATE classconfig SET pretest=(:pretest), posttest=(:posttest), showPostSurvey=(:showPostSurvey) where classId=(:classId)";
    public static final String UPDATE_SURVEY_SETTING_FOR_CLASS_PRE = "UPDATE classconfig SET pretest=(:pretest) where classId=(:classId)";
    public static final String UPDATE_SURVEY_SETTING_FOR_CLASS_POST = "UPDATE classconfig SET posttest=(:posttest), showPostSurvey=(:showPostSurvey) where classId=(:classId)";

    /** SQL Queries For Reports **/
    public static final String PER_STUDENT_QUERY_FIRST ="Select studId AS studentId,concat(s.fname,' ',s.lname) As studentName, s.userName As userName,count(problemId) AS noOfProblems from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=(:classId) and sh.mode != 'demo' and sh.problemBeginTime BETWEEN (:tsFromDate) AND (:tsToDate)  GROUP BY studId order by studId ; ";
    public static final String PER_STUDENT_QUERY_SECOND ="select sh.id,sh.problemId, pg.description,sh.problemEndTime,pr.name,pr.nickname, pr.statementHTML,pr.screenShotURL,sh.isSolved,sh.numMistakes,sh.numHints,sh.numAttemptsToSolve,sh.effort,sh.videoSeen,sh.exampleSeen,pr.standardID, round(od.diff_level,2) as diff_level,sh.mastery,sh.topicId,pg.description,sh.problemEndTime from studentproblemhistory sh, problem pr, problemgroup pg,overallprobdifficulty od where sh.studId in ( select id from student where classId=(:classId)) and sh.studId=(:studId) and sh.mode != 'demo' and sh.problemId = pr.id and sh.problemId = od.problemId and sh.topicId=pg.id and sh.problemBeginTime BETWEEN (:tsFromDate) AND (:tsToDate) order by sh.problemEndTime desc;";
    public static final String PER_STUDENT_PER_PROBLEM = "select s.id as studentId, concat(s.fname,' ',s.lname) As studentName, s.username as username, sh.studId, sh.problemId as problemId,sh.effort as effort,sh.problemBeginTime as problemBeginTime, p.name as description, p.nickname as nickname from student s JOIN studentproblemhistory sh ON sh.studId = s.id JOIN problem as p ON sh.problemId = p.id where s.classId=(:classId) and p.standardID like (:filter) and problemBeginTime BETWEEN (:tsFromDate) AND (:tsToDate) order by s.username, problemBeginTime asc;";
    
    public static final String PER_TOPIC_QUERY_FIRST = "select studId AS studentId,concat(s.fname,' ',s.lname) As studentName, s.userName As userName,sh.topicId,json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (:classId))))) as description,CAST(MAX(sh.mastery) AS DECIMAL(16,2)) AS mastery, sh.problemBeginTime as problemBeginTime from student s,studentproblemhistory sh , problemgroup_description_multi_language pgl where s.id=sh.studId and s.classId=(:classId) and  sh.topicId = pgl.pg_pg_grp_id and sh.mode != 'demo' and sh.problemBeginTime  BETWEEN (:tsFromDate) AND (:tsToDate) group by sh.topicId,studId order by studId";
    public static final String PER_TOPIC_QUERY_SECOND = "select sh.topicId,sh.problemId,CAST(sh.mastery AS DECIMAL(16,2)) AS mastery,sh.effort,sh.problemBeginTime as problemBeginTime,sh.problemEndTime,pr.name,pr.nickname, pr.statementHTML,pr.screenShotURL, sh.problemBeginTime as problemBeginTime from studentproblemhistory sh,problem pr where sh.studId in ( select id from student where classId=(:classId)) and sh.studId=(:studId) and sh.topicId=(:topicId) and sh.mode != 'demo' and sh.problemId = pr.id  and sh.problemBeginTime BETWEEN (:tsFromDate) AND (:tsToDate) order by sh.topicId,problemEndTime asc";

    public static final String PER_TOPIC_QUERY_COMPLETE_MAX = "select sh.topicId,CAST(MAX(sh.mastery) AS DECIMAL(16,2)) AS mastery from student s,studentproblemhistory sh where s.classId=(:classId) and studId=(:studId) and sh.mode != 'demo' group by sh.topicId";
    public static final String PER_TOPIC_QUERY_COMPLETE_AVG = "select sh.topicId,CAST(AVG(sh.mastery) AS DECIMAL(16,2)) AS mastery from student s,studentproblemhistory sh where s.classId=(:classId) and studId=(:studId) and sh.mode != 'demo' group by sh.topicId";
    public static final String PER_TOPIC_QUERY_COMPLETE_LATEST = "select sh.topicId, CAST(sh.mastery AS DECIMAL(16,2)) AS mastery from studentproblemhistory sh where sh.id IN (SELECT MAX(shs.id) from studentproblemhistory shs where studId=(:studId) and shs.mode != 'demo' group by shs.topicId)";

    public static final String PER_PROBLEM_QUERY_FIRST = "select distinct(e.problemId)as problemID,pr.name,pr.standardID, pr.standardCategoryName,pr.screenShotURL,std.description from eventlog e,problem pr,standard std, student student where student.trialUser=0 and student.classId=(:classId) and probElapsed<600000 and e.action in ('Attempt', 'BeginProblem','EndProblem', 'Hint') and student.id = e.studId and e.problemId = pr.id and pr.standardID=std.id order by student.id, e.id";
    public static final String PER_PROBLEM_QUERY_SECOND = "select e.* from eventlog e, student where student.trialUser=0 and student.classId=(:classId) and  e.problemId=(:problemId) and probElapsed<600000 and e.action in ('Attempt', 'BeginProblem','EndProblem', 'Hint') and student.id = e.studId order by student.id, e.id";
    public static final String PER_PROBLEM_QUERY_THIRD = "select sh.effort from studentproblemhistory sh where sh.studid in (select id from student where classId=(:classId)) and sh.problemId =(:problemId) and sh.effort != 'null' and sh.effort != '' ";
    public static final String PER_PROBLEM_QUERY_FOURTH = "select h.* from studentproblemhistory h, student where student.trialUser=0 and student.classId=(:classId) and  h.problemId=(:problemId) and h.mode != 'demo' and h.effort != 'null' and h.effort != '' and student.id = h.studId order by student.id, h.id";
    public static final String PER_PROBLEM_QUERY_FIFTH = "select count(distinct h.studId) as noOfStudents from studentproblemhistory h, student where student.trialUser=0 and student.classId=(:classId) and  h.problemId=(:problemId) and h.mode != 'demo' and student.id = h.studId;";

    public static final String PER_STANDARD_QUERY_FIRST = "select distinct(std.clusterId),cc.categoryCode,cc.clusterCCName,cc.displayName,count(distinct(h.problemId)) as noOfProblemsInCluster ,SUM((h.numHints)) as totalHintsViewedPerCluster, problemBeginTime from studentproblemhistory h, standard std, problem p, cluster cc where studid in (select id from student where classId=(:classId)) and std.clusterID = cc.id and h.mode != 'demo' and std.id=p.standardID and p.id=h.problemId and h.problemBeginTime BETWEEN (:tsFromDate) AND (:tsToDate) group by std.clusterID";
    public static final String PER_STANDARD_QUERY_SECOND = "select std.clusterId,count(distinct(h.problemId)) as noOfProblems from studentproblemhistory h, standard std, problem p where studid in (select id from student where classId =(:classId)) and mode='practice' and std.id=p.standardID and p.id=h.problemId and h.numAttemptsToSolve = 1 group by std.clusterID";
    public static final String PER_STANDARD_QUERY_THIRD = "select distinct(h.problemId),pr.name,pr.standardID, pr.standardCategoryName,pr.screenShotURL,std.description  from studentproblemhistory h, standard std, problem pr where studid in (select id from student where classId=(:classId)) and std.clusterID=(:clusterID) and mode='practice' and std.id=pr.standardID and h.problemId = pr.id and h.problemBeginTime  BETWEEN (:tsFromDate) AND (:tsToDate)";
    public static final String PER_STANDARD_QUERY_FOURTH = "SELECT * FROM (select std.clusterId,count(h.effort) as totalSOFLogged, problemBeginTime from studentproblemhistory h, standard std, problem p where studid in (select id from student where classId =(:classId)) and mode='practice' and std.id=p.standardID and p.id=h.problemId and  h.effort = 'SOF' and h.effort != 'null' group by std.clusterID) as A join ( select std.clusterId,count(h.effort) as totoaleffortlogged, problemBeginTime from studentproblemhistory h, standard std, problem pr where studid in (select id from student where classId =(:classId)) and mode='practice' and std.id=pr.standardID and pr.id=h.problemId and h.problemBeginTime BETWEEN (:tsFromDate) AND (:tsToDate) and  h.effort != 'null' group by std.clusterID) as B on A.clusterId=B.clusterId";

    public static final String LANDING_REPORT_STUDENTS_QUERY ="Select studId AS studentId,concat(s.fname,' ',s.lname) As studentName, s.userName As userName, sh.problemBeginTime, count(problemId) AS noOfProblems from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=(:classId) and sh.mode != 'demo' and sh.problemBeginTime > (:ts) GROUP BY studId order by studentName;";
    public static final String LANDING_REPORT_EVENTS_QUERY ="select distinct s.id AS studentId, s.classid, e.clickTime as clickTime, e.action as action, e.sessNum as sessNum, e.probElapsed as probElapsed, e.elapsedTime as elapsedTime from student as s, class as c, eventlog as e where s.classid = (:classId) and e.studId = s.id and e.clickTime > (:ts) order by e.studId, e.time ASC;";

    public static final String LANDING_REPORT2_STUDENTS_QUERY ="Select studId AS studentId,concat(s.fname,' ',s.lname) As studentName, s.userName As userName, sh.problemBeginTime, count(problemId) AS noOfProblems from student s,studentproblemhistory sh where s.id=sh.studId and s.classId=(:classId) and sh.mode != 'demo' and sh.problemBeginTime BETWEEN (:tsFromDate) AND (:tsToDate) GROUP BY studId order by studentName;";
    public static final String LANDING_REPORT2_EVENTS_QUERY ="select distinct s.id AS studentId, s.classid, e.clickTime as clickTime, e.action as action, e.sessNum as sessNum, e.probElapsed as probElapsed, e.elapsedTime as elapsedTime from student as s, class as c, eventlog as e where s.classid = (:classId) and e.studId = s.id and e.clickTime BETWEEN (:tsFromDate) AND (:tsToDate) order by e.studId, e.time ASC;";
    
    public static final String EMOTION_REPORT = "select e.userInput from eventlog e where studId =(:studId) and action='InputResponse' and userInput != 'null' and userInput not like '%howDoYouFeel%' and userInput not like '%-1%'";
    public static final String EMOTION_REPORT_DOWNLOAD = "select e.studId,e.userInput,s.userName,e.problemId,e.time,pr.name,pr.nickname, pr.standardID, round(od.diff_level,2)as diff_level,e.curTopicId,pg.description from eventlog e, problem pr,overallprobdifficulty od, student s, problemgroup pg   where studId =(:studId) and action='InputResponse' and  userInput != 'null' and userInput not like '%howDoYouFeel%' and e.problemId = od.problemId and e.problemId = pr.id  and e.curTopicId=pg.id and s.id=e.studId and userInput not like '%-1%' and e.time BETWEEN (:tsFromDate) AND (:tsToDate) " ;

    public static final String JSON_PROBLEM_SET_DETAILS = "select probGroupId,json_unquote(json_extract(pgl.pg_lanuage_description, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (:classId))))) as description,\r\n" + 
    		"json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (:classId))))) as summary,seqPos\r\n" + 
    		"from classlessonplan,problemgroup_description_multi_language pgl\r\n" + 
    		"where probGroupId=pgl.pg_pg_grp_id and classId=(:classId) order by seqPos;";
    
    public static final String JSON_PROBLEM_SET_INACTIVE_DETAILS = "select id, \r\n" + 
    		"json_unquote(json_extract(pgl.pg_lanuage_description, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (:classId))))) as description,\r\n" + 
    		"json_unquote(json_extract(pgl.pg_language_name, (select concat('$.',language_code) from ms_language where language_name = (select class_language from class where id= (:classId))))) as summary\r\n" + 
    		"from problemgroup,problemgroup_description_multi_language pgl where active=1 and id=pgl.pg_pg_grp_id and id not in (:activeIds);";
    
    /**
     * Query for summary report
     */
    public static final String SUM_SUR_REPORT ="Select ppt.name as surveyName, ppt.isActive as isSurveyActive,s.id AS studentId,concat(s.fname,' ',s.lname) As studentName, s.userName," + 
    		"pptd.studentAnswer, s.pedagogyId, s.gender, s.age, pptd.probId, pptd.probName, pptd.isCorrect, pptd.studentAnswer, pptd.testType," + 
    		"pptd.seqNum, pptd.skipped, ppptm.testId, ppptm.position as questionPosition, ppp.name as questionName, ppp.description, ppp.ansType," + 
    		"ppp.answer, ppp.problemSet, ppp.aChoice, ppp.bChoice, ppp.cChoice, ppp.dChoice, ppp.eChoice " + 
    		"from student s, preposttestdata pptd, preposttest ppt, prepostproblemtestmap ppptm, prepostproblem ppp " + 
    		"where pptd.studid=s.id and ppptm.testId = ppt.id and pptd.probId = ppptm.probId and ppp.id = ppptm.probId and classid=(:classId) order by ppptm.testId, pptd.studid";
  
    
    public static final String TEACHER_LOG_QUERY_FIRST ="select teacherId AS teacherId,concat(t.fname,' ',t.lname) As teacherName, t.userName As userName,action As action, classId as classId, activityName as activityName, time as timestamp from teacher t ,teacherlog tlog where t.id=tlog.teacherId and t.id=(:targetId) order by time DESC;";
    public static final String TEACHER_LIST_QUERY_FIRST ="select distinct teacherlog.teacherId, teacher.userName from teacherlog join teacher where teacher.ID = teacherlog.teacherId order by teacher.userName;";
    public static final String TEACHER_CLASSLIST_QUERY ="select t.ID as teacherId, c.teacher as teacherName, c.id as classId, c.name as className from teacher t, class c where t.ID = (:teacherId) and t.ID = c.teacherId;";

    
    public static final String COUNT_STUDENTS_USING_CLASS = "select count(distinct h.studId), s.userName, s.classId from studentproblemhistory h, student s where h.studId = s.id and s.classId = ?";

    /* A private Constructor prevents any other
    * class from instantiating.
    */
    private TTUtil(){

    }
    /* Static 'instance' method */
    public static TTUtil getInstance( ) {
        return util;
    }

    private ClassInfo cc = null;

    public void setNumProblemsForProblemSet(DbProblem probMgr,Integer classId ,Connection conn,List<Topic> problemSets) throws SQLException {
        for (Topic t : problemSets) {
            List<Problem> problems = ProblemMgr.getWorkingProblems(t.getId());
            probMgr.filterproblemsBasedOnLanguagePreference(conn, problems, classId);
            List<String> ids = probMgr.getClassOmittedTopicProblemIds(conn, classId, t.getId());
            Map<String,Integer> gradewiseProblemMap = new HashMap<String,Integer>();
            int availabProblem = 0;
            if (problems != null && !problems.isEmpty()) {
                for (Problem p : problems) {
                    if (!ids.contains("" + p.getId())) {
                        availabProblem++;
                        if(p.getStandards().isEmpty())
                        {
                            CCStandard standard = new CCStandard("0.0.0.0","No Standard Associated With Problem","No Category","0","0.0.0.0");
                            p.getStandards().add(standard);
                        }
                        if(gradewiseProblemMap.containsKey(p.getStandards().get(0).getGrade())){
                            int numProb = gradewiseProblemMap.get(p.getStandards().get(0).getGrade());
                            numProb++;
                            gradewiseProblemMap.put(p.getStandards().get(0).getGrade(),numProb);
                        }else{
                            gradewiseProblemMap.put(p.getStandards().get(0).getGrade(),1);
                        }
                    }
                }

            }
            t.setNumProbs(availabProblem);
            t.setGradewiseProblemDistribution(gradewiseProblemMap);
        }
    }
    
 
    public void resetSequenceNosForTheClass( List<Topic> classActiveTopics,int classId, NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        int resetSeq = 1;
        for(Topic classTopic : classActiveTopics) {
            Map<String, Integer> insertParams = new HashMap<String, Integer>();
            insertParams.put("classId", classId);
            insertParams.put("seqPos", resetSeq);
            insertParams.put("probGroupId", classTopic.getId());
            insertParams.put("isDefault", 0);
            namedParameterJdbcTemplate.update(TTUtil.INSERT_ON_CLASS_PLAN, insertParams);
            resetSeq++;
        }

    }

    public Map<String,Integer> getMaxGradesforClassPerHashMap( Map<String,Integer> gradewisePerProblemMap, Map<String,Integer> headerGradeMap){
        for(Map.Entry<String,Integer> gradeEntry : gradewisePerProblemMap.entrySet())
            headerGradeMap.putIfAbsent(gradeEntry.getKey(),gradeEntry.getValue());
        return headerGradeMap;
    }

	public List<Topic> updateTopicNameAndDescription(List<Topic> activeproblemSet, Integer classId,
			Connection connection, NamedParameterJdbcTemplate namedParameterJdbcTemplate, boolean isActiveProblemSet) {
		MapSqlParameterSource sqlSurce = new MapSqlParameterSource();
		sqlSurce.addValue("classId", classId);
		List<Topic> activeList = new ArrayList<>();
		if (isActiveProblemSet) {
			List<Topic> activeListTopics = namedParameterJdbcTemplate.query(TTUtil.JSON_PROBLEM_SET_DETAILS, sqlSurce,
					(ResultSet mappedrow) -> {
						while (mappedrow.next()) {
							Topic tp = new Topic(mappedrow.getInt("probGroupId"), mappedrow.getString("summary"),
									mappedrow.getString("description"));
							tp.setSeqPos(mappedrow.getInt("seqPos"));
							tp.setOldSeqPos(tp.getSeqPos());
							 String topicDescription = mappedrow.getString("description");
			                    if("".equals(topicDescription) || topicDescription == null)
			                    	topicDescription = "The problemset does not have a description in Spanish";
			                tp.setSummary(topicDescription);
			                // We get the set of CCStandards for this topic from the ProblemMgr
			                Set<CCStandard> stds = ProblemMgr.getTopicStandards(tp.getId());
			                tp.setCcStandards(stds);
							activeList.add(tp);
						}
						return activeList;
					});
			return activeListTopics;

		} else {
			try {
				cc = DbClass.getClass(connection, classId);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			List<Integer> activeProblemSetIds = activeproblemSet.stream().map(x -> x.getId())
					.collect(Collectors.toList());
			List<Topic> inactiveTopicList = new ArrayList<>();
			if(activeProblemSetIds != null && !activeProblemSetIds.isEmpty()) {
			sqlSurce.addValue("activeIds", activeProblemSetIds);
			List<Topic> inActiveTopics = namedParameterJdbcTemplate.query(TTUtil.JSON_PROBLEM_SET_INACTIVE_DETAILS,
					sqlSurce, (ResultSet mappedrow) -> {
						while (mappedrow.next()) {
							Topic tp = new Topic(mappedrow.getInt("id"), mappedrow.getString("summary"),
									mappedrow.getString("description"));
							 String topicDescription = mappedrow.getString("description");
			                    if("".equals(topicDescription) || topicDescription == null)
			                    	topicDescription = "The problemset does not have a description in Spanish";
			                tp.setSummary(topicDescription);
			                // We get the set of CCStandards for this topic from the ProblemMgr
			                Set<CCStandard> stds = ProblemMgr.getTopicStandards(tp.getId());
			                tp.setCcStandards(stds);
			                try {
			                	if (hasStandardWithinBounds(tp.getCcStandards().iterator(),cc.getGrade(),cc.getSimpleLowDiff(),cc.getSimpleHighDiff())) {
			                		inactiveTopicList.add(tp);
			                		System.out.println("Added " + tp.getName());
			                	}
			                	else {
			                		System.out.println("Not added " + tp.getName());
			                	}
			                }
			                catch (Exception tpe) {
			                	System.out.println(tpe.getMessage());
			                }
						}
						return inactiveTopicList;
					});
			return inActiveTopics;
			}
			return inactiveTopicList;
		}
	}
	  private boolean hasStandardWithinBounds(Iterator ccstds, String grade, String lowDiff, String highDiff) {

	        while (ccstds.hasNext()) {
	            CCStandard c = (CCStandard) ccstds.next();
	            if (withinDifficultyRange(c,grade,lowDiff,highDiff))
	                return true;
	        }
	        return false;
	    }

	    private int gradeToNum (String grade) {
	        try {
	            int i = Integer.parseInt(grade);
	            return i;
	        } catch (NumberFormatException e) {
	            if (grade.toUpperCase().equals("K"))
	                return 0;
	            else if (grade.toUpperCase().equals("H"))
	                return 9;
	            else if (grade.toUpperCase().equals("ADULT")) // a value given by teacher tools simple class config.
	                return 9;
	            return 9;
	        }

	    }

	    // Given something like above3,above2,above1, above0
	    private int getLevelAbove (String highDiff) {
	        if (highDiff.startsWith("above")) {
	            return Integer.parseInt(highDiff.substring(5));
	        }
	        else return 0;
	    }

	    // Given something like below3,below2,below1, below0
	    private int getLevelBelow (String lowDiff) {
	        if (lowDiff.startsWith("below")) {
	            return Integer.parseInt(lowDiff.substring(5));
	        }
	        else return 0;
	    }

	  
	    private boolean withinDifficultyRange (CCStandard std, String grade, String lowDiff, String highDiff) {
	        String ccstdGrade = std.getGrade();
	        int stdGrade = gradeToNum(ccstdGrade);
	        int myGrade = gradeToNum(grade);
	        int lowDecr = getLevelBelow(lowDiff);
	        int highIncr = getLevelAbove(highDiff);
	        return (stdGrade >= (myGrade-lowDecr)) && (stdGrade <= (myGrade+highIncr));
	    }
}
