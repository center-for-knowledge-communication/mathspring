package edu.umass.ckc.wo.ttmain.ttservice.classservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.ProblemsView;
import org.springframework.ui.ModelMap;

import java.util.List;


/**
 * Created by nsmenon on 4/14/2017.
 * Frank 04-01-21 Add getStudentList
 */
public interface TTProblemsViewService {

    public ProblemsView viewProblemSetsInGivenProblem(Integer problemId, Integer classId, String lang) throws TTCustomException;

    public boolean saveChangsForproblemSets(List<String> problemIdIds, Integer classId, String problemSetId) throws TTCustomException;

    public String resetStudentData(String studentId, String action, String lang) throws TTCustomException;

    public String resetPassWordForStudent(String studentId, String userName, String newPassWord) throws TTCustomException;

    public String changeClassForStudent(String studentId, String newClassId) throws TTCustomException;

    public String editStudentInfo(EditStudentInfoForm editStudentInfoForm, String lang) throws TTCustomException;

    public String deleteInactiveStudents(String classId, String action, String lang) throws TTCustomException;

    public String isStudentPrefixInUse(String[] formValues, String lang) throws TTCustomException;

    public String createAdditionalIdForClass(String[] formValues, String lang) throws TTCustomException;

    public boolean saveSurveySettingsForClass(String[] prePostToActivate, Integer classId) throws TTCustomException;
    
    public String isClassInUse(String classId);
    
    public String getStudentList(String classId);
}
