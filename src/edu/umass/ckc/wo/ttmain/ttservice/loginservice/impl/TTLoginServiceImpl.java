package edu.umass.ckc.wo.ttmain.ttservice.loginservice.impl;

import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.beans.Classes;
import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbPedagogy;
import edu.umass.ckc.wo.db.DbTeacher;
import edu.umass.ckc.wo.smgr.User;
import edu.umass.ckc.wo.ttmain.ttconfiguration.TTConfiguration;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.ErrorCodeMessageConstants;
import edu.umass.ckc.wo.ttmain.ttconfiguration.errorCodes.TTCustomException;
import edu.umass.ckc.wo.ttmain.ttmodel.CreateClassForm;
import edu.umass.ckc.wo.ttmain.ttmodel.EditStudentInfoForm;
import edu.umass.ckc.wo.ttmain.ttservice.classservice.impl.TTCreateClassAssistServiceImpl;
import edu.umass.ckc.wo.ttmain.ttservice.loginservice.TTLoginService;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.tutor.probSel.BaseExampleSelector;
import edu.umass.ckc.wo.tutor.vid.BaseVideoSelector;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Neeraj on 3/25/2017.
 */
@Service
public class TTLoginServiceImpl implements TTLoginService {
    private static Logger logger = Logger.getLogger(TTLoginServiceImpl.class);

    @Autowired
    private TTConfiguration connection;

    @Override
    public int loginAssist(String uname, String password) throws TTCustomException {
        try {
            int teacherId = DbTeacher.getTeacherId(connection.getConnection(), uname, password);
            if (teacherId == -1)
                return -1;
            else
                return teacherId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_OCCURED_ON_AUTHENTICATE);
        }

    }

    @Override
    public String populateClassInfoForTeacher(ModelMap model, int teacherId) throws TTCustomException {
        try {

            if (!ProblemMgr.isLoaded()) {
                ProblemMgr.loadProbs(connection.getConnection());
                Settings.lessonMap = DbPedagogy.buildAllLessons(connection.getConnection());
                Settings.loginMap = DbPedagogy.buildAllLoginSequences(connection.getConnection());
                Settings.pedagogyGroups = DbPedagogy.buildAllPedagogies(connection.getConnection(), null);
            }
            ClassInfo[] classes = DbClass.getClasses(connection.getConnection(), teacherId);
            List<ClassInfo> classInfoList = Arrays.asList(classes);
            Collections.reverse(classInfoList);
            Classes classbean = new Classes(classInfoList.toArray(new ClassInfo[classInfoList.size()]));
            String teacherName = DbTeacher.getTeacherName(connection.getConnection(), teacherId);
            model.addAttribute("teacherName", teacherName);
            model.addAttribute("teacherId", Integer.toString(teacherId));
            model.addAttribute("createClassForm", new CreateClassForm());
            if (classes.length > 0) {
                int classId = classInfoList.get(0).getClassid();
                ClassInfo classInfo = DbClass.getClass(connection.getConnection(), classId);
                List<User> students = DbClass.getClassStudents(connection.getConnection(), classId);
                model.addAttribute("students", students);
                model.addAttribute("classbean", classbean);
                model.addAttribute("classInfo", classInfo);
                model.addAttribute("noClass", false);
                return "teacherTools/teacherToolsMain";
            } else {
                model.addAttribute("noClass", true);
                return "teacherTools/teacherToolsMain";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new TTCustomException(ErrorCodeMessageConstants.ERROR_OCCURED_ON_LOADING_TEACHER_DATA);
        }
    }


}
