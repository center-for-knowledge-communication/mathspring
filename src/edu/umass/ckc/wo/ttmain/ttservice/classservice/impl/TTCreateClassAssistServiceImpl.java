package edu.umass.ckc.wo.ttmain.ttservice.classservice.impl;

import edu.umass.ckc.wo.admin.ClassCloner;
import edu.umass.ckc.wo.admin.ClassContentSelector;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.db.*;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassLessonOnPlanBean;
import edu.umass.ckc.wo.ttmain.ttmodel.ClassOmmittedProblemsBean;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import edu.umass.ckc.wo.ttmain.ttmodel.PerClusterObjectBean;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTCreateClassAssistService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TTUtil;
import edu.umass.ckc.wo.tutor.Settings;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neeraj on 3/26/2017.
 * Frank	02-16-2020	Issue #48
 * Frank	07-08-20	issue #134 & #156 added editClass method
 * Frank	10-02-20	issue #267 detect when grade(s) selected has changed
 * Frank	10-30-20	Issue #293 added call to setAdvancedCofig()
 * Kartik	11-02-20	issue #292 test users to be created on class creation
 * Frank    11-28-20	issue #318 Sort Student - getClassStudentsByName(...)
 * Frank	02--7-22	issue #600 removed code to adjustment maxTime after changing Topic selections
 */

@Service
public class TTCreateClassAssistServiceImpl implements TTCreateClassAssistService {
    @Autowired
    private TTConfiguration connection;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger logger =   Logger.getLogger(TTCreateClassAssistServiceImpl.class);

    @Override
    public void setTeacherInfo(ModelMap map, String teacherId, String classId) {
        try {
        String teacherName = DbTeacher.getTeacherName(connection.getConnection(), Integer.valueOf(teacherId));
        ClassInfo classInfo = DbClass.getClass(connection.getConnection(),Integer.valueOf(classId));
        List<User> students = DbClass.getClassStudentsByName(connection.getConnection(), Integer.valueOf(classId));
        String[] prepostIds = DbPrePost.getActivatedSurveyIdsForClass(connection.getConnection(), Integer.valueOf(classId));
        ClassInfo[] classes = DbClass.getClasses(connection.getConnection(), Integer.valueOf(teacherId));
        Map<Integer,String> activeSurveys = DbPrePost.getActiveSurveyList(connection.getConnection());
        map.addAttribute("students",students );
        map.addAttribute("teacherName", teacherName);
        map.addAttribute("teacherId", teacherId);
        map.addAttribute("classInfo", classInfo);
        map.addAttribute("activeSurveys",activeSurveys );
        map.addAttribute("prepostIds",prepostIds[0]+"~~"+prepostIds[1] );
        map.addAttribute("webContentpath", Settings.webContentPath);
        map.addAttribute("classList", classes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer createNewClass(CreateClassForm createForm, String tid) throws TTCustomException {
        try {
            // Make sure to check initial fields of create class are validated before proceeding ahead
            int defaultPropGroup = DbClass.getPropGroupWithName(connection.getConnection(), "default");
            int newid = DbClass.insertClass(connection.getConnection(), createForm.getClassName(), createForm.getSchoolName(), createForm.getSchoolYear(), createForm.getTown(), createForm.getGradeSection(), tid,
                    defaultPropGroup, 0, createForm.getClassGrade(),createForm.getClassLanguage());
            if (newid != -1) {
                DbTopics.insertLessonPlanWithDefaultTopicSequence(connection.getConnection(), newid);
                ClassInfo info = DbClass.getClass(connection.getConnection(), newid);
                info.setSimpleConfigDefaults();
            } else {
                throw new TTCustomException(ErrorCodeMessageConstants.CLASS_ALREADY_EXIST);
            }
            return newid;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.CLASS_ALREADY_EXIST);
        }

    }

    @Override
    public boolean editClass(CreateClassForm createForm, String tid, int classId) throws TTCustomException {
        boolean update = false;
        try {
        	
            ClassInfo ciPrev = DbClass.getClass(connection.getConnection(), classId);
        	
            int defaultPropGroup = DbClass.getPropGroupWithName(connection.getConnection(), "default");
            DbClass.editClass(connection.getConnection(),
            		classId,
            		createForm.getClassName(), 
            		createForm.getSchoolName(), 
            		createForm.getSchoolYear(), 
            		createForm.getTown(), 
            		createForm.getGradeSection(), 
                    createForm.getClassGrade(),
                    createForm.getClassLanguage());
            
    		DbClass.setAdvancedConfig(connection.getConnection(), classId, createForm.getMaxProb(), createForm.getMinProb(), createForm.getMaxTime(), createForm.getMinTime());
            // Update if any of these were changes
            if ((!createForm.getClassGrade().equals(ciPrev.getGrade()))) {
            	update = true;
            }
            if ((!createForm.getHighEndDiff().equals(ciPrev.getSimpleHighDiff()))) {
            	update = true;
            }
            if ((!createForm.getLowEndDiff().equals(ciPrev.getSimpleLowDiff()))) {
            	update = true;
            }
           
            if (update) {
                DbClass.editClassConfig(connection.getConnection(),
                		classId, createForm.getClassGrade(), createForm.getHighEndDiff(), createForm.getLowEndDiff());            	
                DbTopics.insertLessonPlanWithDefaultTopicSequence(connection.getConnection(), classId);
            }
            //ClassInfo info = DbClass.getClass(connection.getConnection(), classId);
            //info.setSimpleConfigDefaults();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
//            throw new TTCustomException(ErrorCodeMessageConstants.CLASS_ALREADY_EXIST);
        }
        return update;
    }
    
    
    @Override
    public Integer cloneExistingClass(Integer classId, CreateClassForm createForm) throws TTCustomException {
        try {
        int newClassId = ClassCloner.cloneClass(connection.getConnection(),classId,createForm.getClassName(),createForm.getGradeSection());
        return newClassId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_WHILE_CLONNING_EXISTING_CLASS);
        }
    }

    @Override
    public ClassInfo addDefaultPedagogy(Integer classId, CreateClassForm createForm, String action) throws TTCustomException {
        try {
        	if (action.equals("create")) {
        		DbClass.setSimpleConfig(connection.getConnection(), classId, createForm.getSimpleLC(), createForm.getSimpleCollab(), createForm.getProbRate(), createForm.getLowEndDiff(), createForm.getHighEndDiff());
        	}
    		DbClass.setAdvancedConfig(connection.getConnection(), classId, createForm.getMaxProb(), createForm.getMinProb(), createForm.getMaxTime(), createForm.getMinTime());
        	ClassInfo info = DbClass.getClass(connection.getConnection(), classId);
            info.setDefaultClass(true);
            new ClassContentSelector(connection.getConnection()).selectContent(info);
            return info;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.DEFAULT_PEDAGOGY_SETTING_ERROR);
        }

    }

	@Override
	public void createStudentRoster(Integer classId, ClassInfo info, CreateClassForm createForm)
			throws TTCustomException {
		try {
			String password = createForm.getPasswordToken();
			password = classId.toString();

			String userPrefix = createForm.getUserPrefix();
			DbClass.createStudentRoster(connection.getConnection(), info, userPrefix, password,
					createForm.getNoOfStudentAccountsForClass());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new TTCustomException(ErrorCodeMessageConstants.USER_ALREADY_EXIST);
		}

	}

    @Override
    public void createTestUsers(Integer classId, ClassInfo info, int userCount) throws TTCustomException {
        try {
            DbClass.createTestUsers(connection.getConnection(),info,classId.toString(),userCount);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.USER_ALREADY_EXIST);
        }

    }

    @Override
    public void changeDefaultProblemSets(ModelMap map, Integer classId) throws TTCustomException {
        try {
            DbProblem probMgr = new DbProblem();

            List<Topic> activeproblemSet = DbTopics.getClassActiveTopics(connection.getConnection(), classId);
            List<Topic> activeproblemSetModified = TTUtil.getInstance().updateTopicNameAndDescription(activeproblemSet,classId,connection.getConnection(),namedParameterJdbcTemplate, true);
            
            DbTopics.clearClassLessonPlan(connection.getConnection(), classId);

            TTUtil.getInstance().resetSequenceNosForTheClass(activeproblemSetModified, classId, namedParameterJdbcTemplate);
            TTUtil.getInstance().setNumProblemsForProblemSet(probMgr, classId, connection.getConnection(), activeproblemSetModified);

            //DbTopics.getClassInactiveTopics(connection.getConnection(), activeproblemSet);
            List<Topic> inactiveproblemSets = TTUtil.getInstance().updateTopicNameAndDescription(activeproblemSetModified,classId,connection.getConnection(),namedParameterJdbcTemplate, false);
		            
            TTUtil.getInstance().setNumProblemsForProblemSet(probMgr, classId, connection.getConnection(), inactiveproblemSets);


            Map<String, Integer> gradewiseProblemMapActive = new HashMap<String, Integer>();
            Map<String, Integer> gradewiseProblemMapInActive = new HashMap<String, Integer>();

            for (Topic probSet : activeproblemSetModified)
                gradewiseProblemMapActive = TTUtil.getInstance().getMaxGradesforClassPerHashMap(probSet.getGradewiseProblemDistribution(), gradewiseProblemMapActive);


            for (Topic probSet : inactiveproblemSets)
                gradewiseProblemMapInActive = TTUtil.getInstance().getMaxGradesforClassPerHashMap(probSet.getGradewiseProblemDistribution(), gradewiseProblemMapInActive);

            map.addAttribute("inActiveproblemSetHeaders", gradewiseProblemMapInActive);
            map.addAttribute("activeproblemSet", activeproblemSetModified);
            map.addAttribute("inactiveproblemSets", inactiveproblemSets);
            map.addAttribute("activeproblemSetHeaders", gradewiseProblemMapActive);

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_CONFIGURING_PROBLEMSETS);
        }
    }

	@Override
    public boolean reOrderProblemSets(Integer classId, List<Integer> sequencesNosToBeRemoved,Map<Integer,Integer> sequenceNosToBeAdded) throws TTCustomException {
        try {

        //Delete Records Given Sequence Nos
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("seqPos", sequencesNosToBeRemoved);
        params.put("classId", classId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(params);
        namedParameterJdbcTemplate.update(TTUtil.DELETE_CLASS_PLAN, namedParameters);

        //Now update The New Sequence Nos
            for(Map.Entry<Integer,Integer> problemSets :  sequenceNosToBeAdded.entrySet()){
                Map<String,Integer> insertParams = new HashMap<String,Integer>();
                insertParams.put("classId", classId);
                insertParams.put("seqPos", problemSets.getValue());
                insertParams.put("probGroupId", problemSets.getKey());
                insertParams.put("isDefault", 0);
                namedParameterJdbcTemplate.update(TTUtil.INSERT_ON_CLASS_PLAN, insertParams);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_WHILE_REORDERING_PROBLEMSETS);
        }
    }

    @Override
    public String activateDeactivateProblemSets(Integer classId, List<Integer> problemSetsToReorder, String activateFlag) throws TTCustomException {
    	Map<String, Integer> insertParams = null;
    	try {    		
            if ("deactivate".equals(activateFlag)) {
                //Deactivate ProblemSets
                List<Topic> topics = DbTopics.getClassActiveTopics(connection.getConnection(), classId);
                List<Topic> activeproblemSetModified = TTUtil.getInstance().updateTopicNameAndDescription(topics,classId,connection.getConnection(),namedParameterJdbcTemplate, true);
                DbTopics.removeClassActiveTopics(connection.getConnection(), classId);
                int seqPos = 1;
                for (Topic problemSet : activeproblemSetModified) {
                    if (problemSetsToReorder.contains(problemSet.getId())) {
                        insertParams = new HashMap<String, Integer>();
                        insertParams.put("classId", classId);
                        insertParams.put("seqPos", seqPos++);
                        insertParams.put("probGroupId", problemSet.getId());
                        insertParams.put("isDefault", 0);
                        namedParameterJdbcTemplate.update(TTUtil.INSERT_ON_CLASS_PLAN, insertParams);
                    }
                }

            } else {
                //Activate ProblemSets
                int SequenceEntryIndex = 1;
                PreparedStatement getLatestSequencePosition = connection.getConnection().prepareStatement(TTUtil.ACTIVATE_ON_CLASS_PLAN);
                getLatestSequencePosition.setInt(1, classId);
                ResultSet sequenceNoToBeadded = getLatestSequencePosition.executeQuery();
                while (sequenceNoToBeadded.next())
                    SequenceEntryIndex = Math.max(1, sequenceNoToBeadded.getInt(1));
                for (Integer probSetsForReorder : problemSetsToReorder) {
                    insertParams = new HashMap<String, Integer>();
                    insertParams.put("classId", classId);
                    insertParams.put("seqPos", SequenceEntryIndex++);
                    insertParams.put("probGroupId", probSetsForReorder);
                    insertParams.put("isDefault", 0);
                    namedParameterJdbcTemplate.update(TTUtil.INSERT_ON_CLASS_PLAN, insertParams);
                }
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_WHILE_ACTIVATE_DEACTIVATE_PROBLEMSETS);
        }

    }

    @Override
    public boolean restSurveySettings(Integer classId, CreateClassForm createForm) throws TTCustomException {
        try {
            DbClass.setClassConfigShowPostSurvey(connection.getConnection(), classId, createForm.isShowPostSurvey());
            DbClass.setClassConfigShowPreSurvey(connection.getConnection(), classId, createForm.isShowPreSurvey());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_WHILE_RESETTING_SURVEY_SETTINGS);
        }

    }

	@Override
	public String continousContentApply(List<Integer> classIDtoApply, Integer srcClass, Integer teacherId)
			throws TTCustomException {
		if (!classIDtoApply.isEmpty()) {
			SqlParameterSource namedParameters = new MapSqlParameterSource("classId", srcClass);
			List<ClassLessonOnPlanBean> lpbList = new ArrayList<>();
			List<ClassLessonOnPlanBean> classLessonPlanList = namedParameterJdbcTemplate
					.query(TTUtil.GET_CLASS_LESSON_PLAN, namedParameters, (ResultSet mappedrow) -> {
						while (mappedrow.next()) {
							int seqPos = mappedrow.getInt("seqPos");
							int probGroupId = mappedrow.getInt("probGroupId");
							int isDefault = mappedrow.getInt("isDefault");
							ClassLessonOnPlanBean lpb = new ClassLessonOnPlanBean(seqPos, probGroupId, isDefault);
							lpbList.add(lpb);
						}
						return lpbList;
					});

			List<ClassOmmittedProblemsBean> copList = new ArrayList<>();
			List<ClassOmmittedProblemsBean> classOmittedList = namedParameterJdbcTemplate
					.query(TTUtil.GET_CLASS_OMITTED_LIST, namedParameters, (ResultSet mappedrow) -> {
						while (mappedrow.next()) {
							int probId = mappedrow.getInt("probId");
							int topicId = mappedrow.getInt("topicId");
							ClassOmmittedProblemsBean cop = new ClassOmmittedProblemsBean(probId, topicId);
							copList.add(cop);
						}
						return copList;
					});

			classIDtoApply.forEach(classid -> {
				try {
					DbTopics.clearClassLessonPlan(connection.getConnection(), classid);
					new DbProblem().clearClassOmittedProblems(connection.getConnection(), classid);
					for (ClassLessonOnPlanBean clb : classLessonPlanList) {
						Map<String, Integer> insertParams = new HashMap<String, Integer>();
						insertParams.put("classId", classid);
						insertParams.put("seqPos", clb.getSeqPos());
						insertParams.put("probGroupId", clb.getProbGroupId());
						insertParams.put("isDefault", clb.getIsDefault());
						namedParameterJdbcTemplate.update(TTUtil.INSERT_ON_CLASS_PLAN, insertParams);
					}
					for (ClassOmmittedProblemsBean cop : classOmittedList) {
						Map<String, Integer> insertParams = new HashMap<String, Integer>();
						insertParams.put("classId", classid);
						insertParams.put("probId", cop.getProbId());
						insertParams.put("topicId", cop.getTopicId());
						namedParameterJdbcTemplate.update(TTUtil.INSERT_ON_CLASSOMITTED_PROBLEMS, insertParams);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			});

		}
		return "success";
	}
	
    @Override
    public String setClassActiveFlag(Integer teacherId, Integer classId, String activeFlag) {
        try {
        	if (activeFlag.equals("-1")) {
        		DbClass.deleteClass(connection.getConnection(), classId);
        	}
        	else {
                DbClass.setIsActiveFlag(connection.getConnection(), classId, activeFlag);
        	}
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return "success";
    }


}
