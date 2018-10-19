package edu.umass.ckc.wo.ttmain.ttservice.classservice;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

/**
 * Created by Neeraj on 3/26/2017.
 */

public interface TTCreateClassAssistService {

    public void setTeacherInfo(ModelMap map, String teacherId, String classId);

    public Integer createNewClass(CreateClassForm createForm,String tid) throws TTCustomException;

    public Integer cloneExistingClass(Integer classId,CreateClassForm createForm) throws TTCustomException;

    public ClassInfo addDefaultPedagogy(Integer classId, CreateClassForm createForm) throws TTCustomException;

    public void createStudentRoster(Integer classId,ClassInfo info,CreateClassForm createForm) throws TTCustomException;

    public void changeDefaultProblemSets(ModelMap map,Integer classId) throws TTCustomException;

    public boolean reOrderProblemSets(Integer classId, List<Integer> problemSetsToReorder,Map<Integer,Integer> sequenceNosToBeAdded) throws TTCustomException;

    public String activateDeactivateProblemSets(Integer classId, List<Integer> problemSetsToReorder,String activateFlag) throws TTCustomException;

    public boolean restSurveySettings(Integer classId, CreateClassForm createForm) throws TTCustomException;
}
