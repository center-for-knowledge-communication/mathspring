package edu.umass.ckc.wo.ttmain.ttservice.classservice.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.SATProb;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.content.Problem;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbProblem;
import edu.umass.ckc.wo.db.DbTopics;
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

import edu.umass.ckc.wo.smgr.User;

/**
 * Created by nsmenon on 4/14/2017.
 * 
 * Frank	02-16-20	Issue #48
 * Frank 	06-13-2020  issue #106 replace use of probstdmap
 * Frank	07-08-20	issue #134 & #156 added setClassActive flag handling and editClass method 
 * Frank	08-20-20	Issue #49 added method deleteInactiveStudents()
 * Frank	10-27-20	Issue #149R2 teacher logging in JSON format
 * Frank	01-03-21	Issue #329R2 Update topic elements with multi-ligual values 
 * Frank	01-16-21	Issue #368 Edit profile should not allow dups
 * Frank    03-15-21  	Issue #398 New feature to move student from one class to another
 * Frank    04-01-21  	Issue #418 getStudentList()
 * Frank    05-20-21  	Issue #473 crop lname and fix username update bug
 * Frank 	10-09-2021	issue #523 username prefix validation 
 * Frank	05-11-2022 	issue #632 add sorted standards to view
 * Frank 	02-04-23    Issue #723 - handle class clustering
 */
@Service
public class TTProblemsViewServiceImpl implements TTProblemsViewService {

    @Autowired
    private TTConfiguration connection;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger logger =   Logger.getLogger(TTProblemsViewServiceImpl.class);

	private ResourceBundle rb = null;

    @Override
    public ProblemsView viewProblemSetsInGivenProblem(Integer problemId, Integer classId, String lang) throws TTCustomException {
        ProblemsView view= new ProblemsView();
        DbProblem dbProb = new DbProblem();
        try {
            Topic topic = ProblemMgr.getTopic(problemId);
            List<Problem> problems = ProblemMgr.getWorkingProblems(problemId);
            dbProb.filterproblemsBasedOnLanguagePreference(connection.getConnection(), problems, classId);
            List<SATProb> satProbs = new DbProblem().getTopicOmittedProblems(connection.getConnection(), classId, problems, problemId);
            view.setProblemLevelId(String.valueOf(topic.getId()));
            
            view.setTopicName(topic.getMlName(lang));
            view.setTopicStandars(DbTopics.getRptTopicStandards(connection.getConnection(), topic.getId()));
            view.setTopicStandardsSorted(DbTopics.getRptTopicStandardsSorted(connection.getConnection(), topic.getId()));
            view.setTopicSummary(topic.getMlDescription(lang));
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
    public String resetStudentData(String studentId, String action, String lang) throws TTCustomException {
    	
    	System.out.println("resetStudentData - lang=" + lang);
		// Multi=lingual enhancement
    	Locale loc = new Locale("en","US");	
    	if (lang.substring(0,2).equals("es")) {
    		loc = new Locale("es","US");	
    	}
		rb = ResourceBundle.getBundle("MathSpring",loc);        
		String message = "";
        try {
            switch (action) {
                case "4":
                    DbUser.deleteStudentData(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = rb.getString("student_data_successfully_cleared");
                    break;

                case "5":
                    DbUser.deleteStudentPracticeHutData(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = rb.getString("student_successfully_deleted");
                    break;

                case "6":
                    DbUser.resetStudentPracticeHut(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = rb.getString("student_data_reset");
                    break;

                case "7":
                    DbUser.deleteStudentPrePostEventData(connection.getConnection(), Integer.valueOf(studentId.trim()), true, false);
                    message = rb.getString("cleared_pretest_data");
                    break;

                case "8":
                    DbUser.deleteStudentPrePostEventData(connection.getConnection(), Integer.valueOf(studentId.trim()), false, true);
                    message = rb.getString("cleared_posttest_data");
                    break;

                case "9":
                    DbUser.deleteStudent(connection.getConnection(), Integer.valueOf(studentId.trim()));
                    message = rb.getString("student_info_removed_from_class");
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_OCCURRED_WHILE_UPDATING_STUDENT_DATA);
        }
    	System.out.println("resetStudentData - message=" + message);
        return message;
    }

    @Override
    public String deleteInactiveStudents(String classId, String action, String lang) throws TTCustomException {
    	
    	System.out.println("deleteInactiveStudents - lang=" + lang);
		// Multi=lingual enhancement
    	Locale loc = new Locale("en","US");	
    	if (lang.substring(0,2).equals("es")) {
    		loc = new Locale("es","US");	
    	}		
    	rb = ResourceBundle.getBundle("MathSpring",loc);        
		String message = "";
        try {
            switch (action) {
                case "0":
                	message = rb.getString("deleted_student_ids") + ": ";
                	message += DbClass.deleteClassInactiveStudents(connection.getConnection(), Integer.valueOf(classId.trim()));
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_OCCURRED_WHILE_UPDATING_STUDENT_DATA);
        }
    	System.out.println("deleteInactiveStudents - message=" + message);
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
    public String changeClassForStudent(String studentId, String newClassId, String lang) throws TTCustomException {
		String msg = "***";
        try {
        	Map<String, Object> updateParams = new HashMap<String, Object>();
	        updateParams.put("studentId", Integer.valueOf(studentId.trim()));
	        updateParams.put("newClassId", Integer.valueOf(newClassId.trim()));
	        this.namedParameterJdbcTemplate.update(TTUtil.UPDATE_CLASS_FOR_STUDENT, updateParams);
	        msg = "{ ";
	    	msg += "\"studentid\" : \"" + studentId.trim() + "\",";
	    	msg += "\"newClassId\" : \"" + newClassId.trim() + "\",";
	    	msg += " }";
	    	return msg;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        	msg =  "*** " + rb.getString("system_error") + "(" + ErrorCodeMessageConstants.ERROR_OCCURRED_WHILE_UPDATING_STUDENT_DATA + ")" +  " ***";
        	return msg;

        }
    }   

    @Override
    public String editStudentInfo(EditStudentInfoForm editStudentInfoForm, String lang) throws TTCustomException {
    	
    	// Multi=lingual enhancement
    	Locale loc = new Locale("en","US");	
    	if (lang.substring(0,2).equals("es")) {
    		loc = new Locale("es","US");	
    	}		
    	rb = ResourceBundle.getBundle("MathSpring",loc);
		String msg =  "";
        try {
        	int id = 0;
        	if (!editStudentInfoForm.getStudentUsername().equals(editStudentInfoForm.getOrigStudentUsername())) {
            	id = DbUser.getStudent(connection.getConnection(), editStudentInfoForm.getStudentUsername());        		
        	}
        	if (id > 0) {
        		msg =  "*** " + rb.getString("username_in_use") +  " ***";
            	return msg;
        	}
        	else {
		        Map<String,Object> updateParams = new HashMap<String,Object>();
		        updateParams.put("fname", editStudentInfoForm.getStudentFname());
		        String lname = editStudentInfoForm.getStudentLname();
		        if (lname.length() > 2) {
		        	lname = lname.substring(0, 2);
		        }
		        updateParams.put("lname",lname);
		        updateParams.put("uname", editStudentInfoForm.getStudentUsername());
		        updateParams.put("studentId", editStudentInfoForm.getStudentId());
		        this.namedParameterJdbcTemplate.update(TTUtil.UPDATE_STUDENT_INFO,updateParams);
		        msg = "{ ";
		    	msg += "\"msg\" : \"" + rb.getString("changes_saved_successfully") + "\",";
		    	msg += "\"id\" : \"" + editStudentInfoForm.getStudentId() + "\",";
		    	msg += "\"fname\" : \"" + editStudentInfoForm.getStudentFname() + "\",";
		    	msg += "\"lname\" : \"" + editStudentInfoForm.getStudentLname() + "\",";
		    	msg += "\"username\" : \"" + editStudentInfoForm.getStudentUsername() + "\"";
		    	msg += " }";
	        	return msg;
		    }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
    		msg =  "*** " + rb.getString("system_error") + "(" + ErrorCodeMessageConstants.ERROR_OCCURRED_WHILE_UPDATING_STUDENT_DATA + ")" +  " ***";
        	return msg;

        }
    }

    @Override
    public String isStudentPrefixInUse(String[] formValues, String lang) throws TTCustomException {
    	
    	int result = 0;
    	// Multi=lingual enhancement
    	Locale loc = new Locale("en","US");	
    	if (lang.substring(0,2).equals("es")) {
    		loc = new Locale("es","US");	
    	}
		rb = ResourceBundle.getBundle("MathSpring",loc);        
        String msg = "";

        try {
            result = DbUser.isStudentPrefixInUse(connection.getConnection(),formValues);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.USER_ALREADY_EXIST);
            
        }
        if (result > 0) {
        	return "inuse";
	    }
	    else {
        	return "available";
	    }
    }
    
    @Override
    public String createAdditionalIdForClass(String[] formValues, String lang) throws TTCustomException {
    	
		// Multi=lingual enhancement
    	Locale loc = new Locale("en","US");	
    	if (lang.substring(0,2).equals("es")) {
    		loc = new Locale("es","US");	
    	}
		rb = ResourceBundle.getBundle("MathSpring",loc);        
        String msg = "";

        try {
            ClassInfo info = DbClass.getClass(connection.getConnection(),Integer.valueOf(formValues[4]));
            String prefix = formValues[0].trim() + "-";
            DbClass.createStudentRoster(connection.getConnection(),info,prefix,formValues[4].trim(),Integer.valueOf(formValues[2]));
            msg = "{ ";
        	msg += "\"student ids created\" : \"" + formValues[2] + "\"";
        	msg += " }";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.USER_ALREADY_EXIST);
            
        }
        return msg;
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

    @Override
    public String isClassInUse(String classId) {
    	
    	boolean result = true;
  		
        try {
        	result = DbClass.isClassInUse(connection.getConnection(), Integer.valueOf(classId.trim()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        if (result) {
        	return "Y";
        }
        else {
        	return "N";
        }
    }

    public String getStudentList(String classId) {
    	
    	String result = "";
  		List<User> theList = null;
  		
  		
        try {
      		

      		theList = DbClass.getClassStudents(connection.getConnection(), Integer.valueOf(classId.trim()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        int count = 0;
        ListIterator iterator = theList.listIterator();
        if (iterator.hasNext()) {        	
	        while (iterator.hasNext()) {
	        	User u = (User) iterator.next();
	        	if (count > 0) {
	        		result += ",";
	        	}
	        	else {
	        		count++;	        		
	        	}
	        	result += u.getUname() + "~" + u.getLname() + "~" + u.getFname() + "~" + String.valueOf(u.getId());    	
	        }
	        
        }
        else {
        	result = "No Data found";
        }
        return result;
    }    
}
