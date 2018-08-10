package edu.umass.ckc.wo.ttmain.ttservice.classservice.impl;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.SATProb;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.login.PasswordAuthentication;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.ProblemsView;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTProblemsViewService;
import edu.umass.ckc.wo.ttmain.ttservice.util.TTUtil;
import edu.umass.ckc.wo.tutor.Settings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nsmenon on 4/14/2017.
 */
@Service
public class TTProblemsViewServiceImpl implements TTProblemsViewService {

    @Autowired
    private TTConfiguration connection;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger logger =   Logger.getLogger(TTProblemsViewServiceImpl.class);

    @Override
    public ProblemsView viewProblemSetsInGivenProblem(Integer problemId, Integer classId) throws TTCustomException {
        ProblemsView view= new ProblemsView();
        try {
            Topic problemSet = ProblemMgr.getTopic(problemId);
            List<Problem> problems = ProblemMgr.getWorkingProblems(problemId);
            List<SATProb> satProbs = new DbProblem().getTopicOmittedProblems(connection.getConnection(), classId, problems, problemId);
            view.setProblemLevelId(String.valueOf(problemSet.getId()));
            view.setTopicName(problemSet.getName());
            view.setTopicStandars(problemSet.getCcStandards());
            view.setTopicSummary(problemSet.getSummary());
            view.setUri(Settings.probPreviewerPath);
            view.setHtml5ProblemURI(Settings.html5ProblemURI);
            view.setProblems(satProbs);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.FAILED_TO_LOAD_PROBLEMS);
        }
        return view;
    }

    @Override
    public boolean saveChangsForproblemSets(List<String> problemIdIds, Integer classId, String problemSetId) throws TTCustomException {
        Integer problemsetId = Integer.valueOf(problemSetId.replace("problemLevelId",""));

        //Delete all entries for this class Id and Topic Id
        Map<String,Integer> deletepararms = new HashMap<String,Integer>();
        deletepararms.put("classId", classId);
        deletepararms.put("problemSetId", problemsetId);
        this.namedParameterJdbcTemplate.update(TTUtil.REMOVE_FROM_CLASSOMITTED_PROBLEMS,deletepararms);

        //Insert all entries from coming list
        if(!problemIdIds.isEmpty())
        for(String newOmits : problemIdIds){
            Map<String,Integer> insertParams = new HashMap<String,Integer>();
            insertParams.put("classId", classId);
            insertParams.put("topicId", problemsetId);
            insertParams.put("probId", Integer.valueOf(newOmits));
            this.namedParameterJdbcTemplate.update(TTUtil.INSERT_ON_CLASSOMITTED_PROBLEMS,insertParams);
        }

        return true;
    }

    @Override
    public String resetStudentData(String studentId, String action) throws TTCustomException {
        String message = "";
        try {
            switch (action) {
                case "4":
                    DbUser.deleteStudentData(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = "Student data successfully cleared.";
                    break;

                case "5":
                    DbUser.deleteStudentPracticeHutData(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = "Student successfully deleted.";
                    break;

                case "6":
                    DbUser.resetStudentPracticeHut(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = "Student data reset";
                    break;

                case "7":
                    DbUser.deleteStudentPrePostEventData(connection.getConnection(), Integer.valueOf(studentId.trim()), true, false);
                    message = "Cleard Pretest data";
                    break;

                case "8":
                    DbUser.deleteStudentPrePostEventData(connection.getConnection(), Integer.valueOf(studentId.trim()), false, true);
                    message = "Cleard Posttest data";
                    break;

                case "9":
                    DbUser.deleteStudent(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = "Student info removed from class";
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_OCCURRED_WHILE_UPDATING_STUDENT_DATA);
        }
        return message;
    }

    @Override
    public String resetPassWordForStudent(String studentId, String userName, String newPassWordTobeSet) throws TTCustomException {
        String token = newPassWordTobeSet;
        String newPassWord = PasswordAuthentication.getInstance().hash(token.toCharArray());
        Map<String, Object> updateParams = new HashMap<String, Object>();
        updateParams.put("resetPassword", newPassWord);
        updateParams.put("studentId", Integer.valueOf(studentId.trim()));
        this.namedParameterJdbcTemplate.update(TTUtil.UPDATE_PASSWORD_FOR_STUDENT, updateParams);
        return token;
    }

    @Override
    public String editStudentInfo(EditStudentInfoForm editStudentInfoForm) throws TTCustomException {
        Map<String,Object> updateParams = new HashMap<String,Object>();
        updateParams.put("fname", editStudentInfoForm.getStudentFname());
        updateParams.put("lname", editStudentInfoForm.getStudentLname());
        updateParams.put("uname", editStudentInfoForm.getStudentUsername());
        updateParams.put("studentId", editStudentInfoForm.getStudentId());
        this.namedParameterJdbcTemplate.update(TTUtil.UPDATE_STUDENT_INFO,updateParams);
        return "Changes saved successfully";
    }

    @Override
    public String createAdditionalIdForClass(String[] formValues) throws TTCustomException {
        try {
            ClassInfo info = DbClass.getClass(connection.getConnection(),Integer.valueOf(formValues[4]));
            DbClass.createStudentRoster(connection.getConnection(),info,formValues[0].trim(),formValues[1].trim(),Integer.valueOf(formValues[2]));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.USER_ALREADY_EXIST);
        }
        return "success";
    }

    @Override
    public boolean saveSurveySettingsForClass(String[] prePostToActivate, Integer classId) throws TTCustomException {
        Map<String, Integer> updateparams = new HashMap<String, Integer>();
        updateparams.put("classId", classId);
        if (prePostToActivate[0].equals("undefined") && !prePostToActivate[1].equals("undefined") ) {
            if(prePostToActivate[1].equals("0"))
                updateparams.put("showPostSurvey", 0);
            else
                updateparams.put("showPostSurvey", 1);
            updateparams.put("posttest", Integer.valueOf(prePostToActivate[1]));
            this.namedParameterJdbcTemplate.update(TTUtil.UPDATE_SURVEY_SETTING_FOR_CLASS_POST, updateparams);
        } else if (prePostToActivate[1].equals("undefined") && !prePostToActivate[0].equals("undefined")) {
            updateparams.put("pretest", Integer.valueOf(prePostToActivate[0]));
            this.namedParameterJdbcTemplate.update(TTUtil.UPDATE_SURVEY_SETTING_FOR_CLASS_PRE, updateparams);
        } else if (!prePostToActivate[0].equals("undefined") && !prePostToActivate[1].equals("undefined")) {
            updateparams.put("pretest", Integer.valueOf(prePostToActivate[0]));
            updateparams.put("posttest", Integer.valueOf(prePostToActivate[1]));
            if(prePostToActivate[1].equals("0"))
                updateparams.put("showPostSurvey", 0);
            else
                updateparams.put("showPostSurvey", 1);
            this.namedParameterJdbcTemplate.update(TTUtil.UPDATE_SURVEY_SETTING_FOR_CLASS_ALL, updateparams);
        }

        return true;
    }

}
