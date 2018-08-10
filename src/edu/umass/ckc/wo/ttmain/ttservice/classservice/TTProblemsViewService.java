package edu.umass.ckc.wo.ttmain.ttservice.classservice;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.ProblemsView;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * Created by nsmenon on 4/14/2017.
 */
public interface TTProblemsViewService {

    public ProblemsView viewProblemSetsInGivenProblem(Integer problemId, Integer classId) throws TTCustomException;

    public boolean saveChangsForproblemSets(List<String> problemIdIds, Integer classId, String problemSetId) throws TTCustomException;

    public String resetStudentData(String studentId, String action) throws TTCustomException;

    public String resetPassWordForStudent(String studentId, String userName, String newPassWord) throws TTCustomException;

    public String editStudentInfo(EditStudentInfoForm editStudentInfoForm) throws TTCustomException;

    public String createAdditionalIdForClass(String[] formValues) throws TTCustomException;

    public boolean saveSurveySettingsForClass(String[] prePostToActivate, Integer classId) throws TTCustomException;
}
