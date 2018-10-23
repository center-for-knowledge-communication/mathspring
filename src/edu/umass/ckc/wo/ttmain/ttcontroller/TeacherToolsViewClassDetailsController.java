package edu.umass.ckc.wo.ttmain.ttcontroller;

import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttmodel.ProblemsView;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTCreateClassAssistService;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.TTProblemsViewService;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import org.apache.commons.collections.map.HashedMap;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Neeraj on 3/31/2017.
 */

@Controller
public class TeacherToolsViewClassDetailsController {

    @Autowired
    private TTLoginService loginService;

    @Autowired
    private TTCreateClassAssistService ccService;

    @Autowired
    private TTProblemsViewService pvService;

    @RequestMapping(value = "/tt/viewClassDetails", method = RequestMethod.GET)
    public String viewClassDetails(ModelMap map,@RequestParam("teacherId") String teacherId, @RequestParam("classId") String classId ) throws TTCustomException {
        ccService.setTeacherInfo(map,teacherId,classId);
        ccService.changeDefaultProblemSets(map,Integer.valueOf(classId));
        map.addAttribute("createClassForm", new CreateClassForm());
        return "teacherTools/classDetails";
    }


    @RequestMapping(value = "/tt/reOrderProblemSets", method = RequestMethod.POST)
    public @ResponseBody  String  reOrderProblemSets(ModelMap map, @RequestParam(value = "problemSets[]") List<String> problemSets, @RequestParam(value = "classid") String classid) throws TTCustomException {
        List<Integer> sequenceNosToBeRemoved = new ArrayList<>();
        Map<Integer, Integer> insertSequences = new HashMap<>();
        for (String probsetEntries : problemSets) {
            String[] entries = probsetEntries.split("~~");
            insertSequences.put(Integer.valueOf(entries[0]), Integer.valueOf(entries[1]));
            sequenceNosToBeRemoved.add(Integer.valueOf(entries[2]));
        }
        ccService.reOrderProblemSets(Integer.valueOf(classid), sequenceNosToBeRemoved, insertSequences);
        return "success";
    }

    @RequestMapping(value = "/tt/configureProblemSets", method = RequestMethod.POST)
    public @ResponseBody String activateProblemSets(ModelMap map, @RequestParam(value = "activateData[]") List<String> problemSets, @RequestParam(value = "classid") String classid,@RequestParam(value = "activateFlag") String activateFlag) throws TTCustomException {
        List<Integer> intProblemSets = problemSets.stream().map(Integer::parseInt).collect(Collectors.toList());
        return ccService.activateDeactivateProblemSets(Integer.valueOf(classid), intProblemSets,activateFlag);
    }

    @RequestMapping(value = "/tt/getProblemForProblemSets", method = RequestMethod.POST)
    public @ResponseBody  String viewProblemsForProblemSet(ModelMap map, @RequestParam(value = "problemID") String problemId, @RequestParam(value = "classid") String classid) throws TTCustomException {
        try {
            ProblemsView pView = pvService.viewProblemSetsInGivenProblem(Integer.valueOf(problemId), Integer.valueOf(classid));
            ObjectMapper objectMapp = new ObjectMapper();
            objectMapp.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
            return objectMapp.writeValueAsString(pView);
        }catch (IOException e){
            e.printStackTrace();
        }
        return "failed";
    }


    @RequestMapping(value = "/tt/saveChangesForProblemSet", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<String> saveChangsForproblemSets(@RequestParam(value = "problemIds[]") List<String> problemId,
                                                    @RequestParam(value = "classid") String classid, @RequestParam(value = "problemsetId") String problemsetId) throws TTCustomException {
          pvService.saveChangsForproblemSets(problemId,Integer.valueOf(classid),problemsetId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/tt/activatePrePostSurveys", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<String> activatePrePostSurveys(@RequestParam(value = "activatePrePostSurveys") String surveyIds,
                                                    @RequestParam(value = "classid") String classid) throws TTCustomException {
        String[] prePostToActivate = surveyIds.split(",");
        pvService.saveSurveySettingsForClass(prePostToActivate,Integer.valueOf(classid));
        return new ResponseEntity<String>(HttpStatus.OK);
    }


    @RequestMapping(value = "/tt/resetStudentdata", method = RequestMethod.POST)
    public @ResponseBody
    String resetStudentdata(@RequestParam(value = "studentId") String studentId, @RequestParam(value = "action") String action) throws TTCustomException {
        return pvService.resetStudentData(studentId,action);
    }


    @RequestMapping(value = "/tt/resetStudentPassword", method = RequestMethod.POST)
    public @ResponseBody
    String resetStudentPassword(@RequestParam(value = "studentId") String studentId,@RequestParam(value = "userName") String userName, @RequestParam(value = "newPassWord") String newPassWord ) throws TTCustomException {
        return pvService.resetPassWordForStudent(studentId,userName,newPassWord);
    }


    @RequestMapping(value = "/tt/editStudentInfo", method = RequestMethod.POST)
    public @ResponseBody
    String editStudentInfo(@RequestParam(value = "studentId") String studentId,@RequestParam(value = "formData[]") String[] formData) throws TTCustomException {
        return pvService.editStudentInfo(new EditStudentInfoForm(Integer.valueOf(studentId.trim()),formData[1].trim(),formData[2].trim(),formData[0].trim()));
    }


    @RequestMapping(value = "/tt/createMoreStudentIds", method = RequestMethod.POST)
    public @ResponseBody
    String createMoreStudentIds(ModelMap map,@RequestParam(value = "formData[]") String[] formData) throws TTCustomException {
        String message =  pvService.createAdditionalIdForClass(formData);
        loginService.populateClassInfoForTeacher(map,Integer.valueOf(formData[3].trim()));
        return message;
    }

}
