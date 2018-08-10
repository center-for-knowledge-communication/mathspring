package edu.umass.ckc.wo.content;

import edu.umass.ckc.wo.cache.ProblemMgr;
import edu.umass.ckc.wo.db.DbCC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: marshall
 * Date: 8/1/14
 * Time: 12:58 PM
 * Must be called after the CCContentMgr loads content.
 */
public class LessonMgr {
    private static LessonMgr instance = null;

    private LessonMgr () {
    }

    public static LessonMgr getInstance() {
        if (instance == null)
            instance = new LessonMgr();
        return instance;
    }


    // THis method is called when the tutor loads.   It is simply used to make sure of the integrity of the
    // content.   If anything is bad, we want an exception so we know it at load time rather than when we are
    // running with a student in a class that uses the CCPedagogicalModel and tries to instantiate it and then it
    // fails.
    public static void getAllLessons (Connection conn) throws Exception {
        List<Lesson> allLessons = DbCC.getAllLessons(conn);
        // the lessons now have to have CurricUnits added to them and have their omit lists added
        for (Lesson l : allLessons) {
            List<CurricUnit> cus = DbCC.getLessonCurricUnits(conn,l.getId());
            for (CurricUnit cu : cus) {
                connectToCCStructure(cu);
                l.insertCU(cu, cu.getPosition());
            }
            List<LessonOmit> omits = DbCC.getLessonOmits(conn,l.getId());
        }
    }

    /**
     * A class's lessons are loaded on each request that needs them.   THis allows removal of content from lessons that
     * will be picked up right away
     * @param conn
     */
    public List<Lesson> getClassLessons (Connection conn, int classId) throws Exception {
        List<Lesson> classLessons = DbCC.getLessons(conn,classId);
        // the lessons now have to have CurricUnits added to them and have their omit lists added
        for (Lesson l : classLessons) {
            List<CurricUnit> cus = DbCC.getLessonCurricUnits(conn,l.getId());
            for (CurricUnit cu : cus) {
                connectToCCStructure(cu);
                l.insertCU(cu, cu.getPosition());
            }
            List<LessonOmit> omits = DbCC.getLessonOmits(conn,l.getId());
            l.setOmits(omits);
        }
        return classLessons;
    }

    // THe CU either points at a Cluster, Standard, or a Problem
    private static void connectToCCStructure(CurricUnit cu) throws SQLException {
        if (cu.getClustId() != -1) {
            cu.setCluster(CCContentMgr.getInstance().getCluster(cu.getClustId()));
        }
        else if (cu.getStdId() != null) {
            cu.setStandard(CCContentMgr.getInstance().getStandard(cu.getStdId()));
        }
        else {
            Problem p = ProblemMgr.getProblem(cu.getProbId());
            cu.setProblem(p);
        }

    }




}
