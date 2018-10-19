package edu.umass.ckc.wo.admin;

import edu.umass.ckc.servlet.servbase.UserException;
import edu.umass.ckc.wo.beans.ClassInfo;
import edu.umass.ckc.wo.db.DbClass;
import edu.umass.ckc.wo.db.DbClassPedagogies;
import edu.umass.ckc.wo.db.DbTopics;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbUtil;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: marshall
 * Date: 9/7/11
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClassCloner {

    /**
     *  Duplicate the class that is given.   Everything about the two classes will be the same
     *  except the names and sections.
     *
     * @param classId  The id of the class to clone
     * @param newClassName  The name of the new class to create
     * @param newClassSection   The section of the new class
     * @return
     */
    public static int cloneClass (Connection conn, int classId, String newClassName, String newClassSection) throws Exception {
        if (newClassName.trim().equals("") || newClassSection.trim().equals(""))
            return -1;
        ClassInfo info = DbClass.getClass(conn,classId);
        int newClassId= DbClass.insertClass(conn,newClassName,info.getSchool(), Integer.toString(info.getSchoolYear()),
                        info.getTown(),newClassSection, Integer.toString(info.getTeachid()),
                        info.getPropGroupId(), info.getPretestPoolId(), info.getGrade(),info.getClassLanguageCode());
         DbClass.removeConfig(conn,newClassId);
        if (newClassId != -1) {
            // clone the lesson plan (if there is one)
            DbTopics.cloneLessonPlan(conn,classId,newClassId);
            // clone the class config
            DbClass.cloneConfig(conn,classId,newClassId);
            // clone class pedagogies
            DbClassPedagogies.clonePedagogies(conn,classId,newClassId);
            // clone class omitted problems
            DbTopics.cloneOmittedProblems(conn,classId,newClassId);
        }
        return newClassId;
    }

    public static void main(String[] args) {
        DbUtil.loadDbDriver();
        try {
            Connection conn = DbUtil.getAConnection("cadmium.cs.umass.edu");
            int id = DbUser.getStudent(conn,"whole_1","whole");
            ClassCloner.cloneClass(conn,415,"Clone of Class 415","section 2");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
