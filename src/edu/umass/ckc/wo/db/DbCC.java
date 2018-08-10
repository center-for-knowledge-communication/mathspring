package edu.umass.ckc.wo.db;

import edu.umass.ckc.wo.content.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Common Core tables
 * User: marshall
 * Date: 8/1/14
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbCC {

    public static List<CCCluster> getClusters(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            List<CCCluster> clusters = new ArrayList<CCCluster>();
            String q = "select id,displayName,clusterABCD,clusterCCName,categoryCode,c_grade,category from cluster order by id";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String displayName = rs.getString("displayName");
                String clusterABCD = rs.getString("clusterABCD");
                String clusterCCName = rs.getString("clusterCCName");
                String categoryCode = rs.getString("categoryCode");
                String c_grade = rs.getString("c_grade");
                String category = rs.getString("category");
                CCCluster c = new CCCluster(id,displayName,clusterABCD,clusterCCName,categoryCode,c_grade,category);
                // Clusters are being selected in order by ID and inserted in the list in order
                clusters.add(c);

            }
            return clusters;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }

    }


    public static List<CCStandard> getStandards(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            List<CCStandard> standards = new ArrayList<CCStandard>();
            String q = "select id,description,grade,category,clusterName,clusterId,idABC,adminFlag,authorFlag from standard";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String descr = rs.getString("description");
                String grade = rs.getString("grade");
                String cat = rs.getString("category");
                String clustName = rs.getString("clusterName");
                int clustId = rs.getInt("clusterId");
                String idABC = rs.getString("idABC");
                String adminFlag = rs.getString("adminFlag");
                String authFlag = rs.getString("authorFlag");
                CCStandard standard = new CCStandard(id,descr,grade,cat,clustName,clustId,idABC,adminFlag,
                        authFlag, null);  // temporarily the standard has no cluster obj to point to.

                standards.add(standard);
            }
            return standards;

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }

    }

    /*
     * get the problem ids of problems that are part of a standard
     */
    public static List<Integer> getStandardProblemIds(Connection conn, String stdId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select probId,stdId from probstdmap where stdId=?";
            ps = conn.prepareStatement(q);
            ps.setString(1, stdId);
            rs = ps.executeQuery();
            List<Integer> probIds = new ArrayList<Integer>();
            while (rs.next()) {
                int pid = rs.getInt("probId");
                probIds.add(pid);
            }
            return probIds;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


    public static CurricUnit getCurriculumUnit (Connection conn, int cuId) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id, probId, clusterId, stdId, lessonid,position from curriculumunit where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,cuId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int id=rs.getInt("id");
                boolean isStd=false,isClust=false, isProb=false;
                int probId = rs.getInt("probId");
                isProb = !rs.wasNull();
                int clustId = rs.getInt("clusterId");
                isClust = !rs.wasNull();
                String stdId = rs.getString("stdId");
                isStd = !rs.wasNull();
                int lessonId = rs.getInt("lessonId");
                int position=rs.getInt("position");
                CurricUnit cu;
                cu = new CurricUnit(id,stdId,clustId,probId,lessonId,position);
                return cu;
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    /**
     * Returns a list of CurricUnits that have info from the table columns but not with pointers to the
     * standard,cluster, or problem objects.
     * @param conn
     * @return
     * @throws SQLException
     */
    public static List<CurricUnit> getCurriculumUnits (Connection conn) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id, clusterId, stdId, probId,lessonid,position from curriculumunit where id=?";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            List<CurricUnit> cus = new ArrayList<CurricUnit>();
            while (rs.next()) {
                int id=rs.getInt("id");
                boolean isStd=false,isClust=false, isProb=false;
                int clustId = rs.getInt("clusterId");
                isClust = !rs.wasNull();
                String stdId = rs.getString("stdId");
                isStd = !rs.wasNull();
                int probId = rs.getInt("probId");
                isProb = !rs.wasNull();
                String objId = rs.getString("cu_refoObjectId");
                int lessonId = rs.getInt("lessonId");
                int position=rs.getInt("position");
                CurricUnit cu;
                cu = new CurricUnit(id,stdId,clustId,probId,lessonId,position);
                cus.add(cu);
            }
            return cus;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    /**
     * REturn the CUs that go with a lesson
     * @param conn
     * @param lessonId
     * @return
     * @throws SQLException
     */
    public static List<CurricUnit> getLessonCurricUnits (Connection conn, int lessonId ) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id, clusterId, stdId, probId, position from curriculumunit where lessonid=? order by position";
            ps = conn.prepareStatement(q);
            ps.setInt(1,lessonId);
            rs = ps.executeQuery();
            List<CurricUnit> cus = new ArrayList<CurricUnit>();
            while (rs.next()) {
                int id=rs.getInt("id");
                boolean isStd=false,isClust=false,isProb=false;
                int clustId = rs.getInt("clusterId");
                isClust = !rs.wasNull();
                int probId = rs.getInt("probId");
                isProb = !rs.wasNull();
                String stdId = rs.getString("stdId");
                isStd = !rs.wasNull();
                int position=rs.getInt("position");
                CurricUnit cu;
                cu = new CurricUnit(id,stdId,clustId,probId,lessonId,position);
                cus.add(cu);
            }
            return cus;
        }
        finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }



    public static List<Lesson> getAllLessons (Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id,name,notes,assigndate,duedate from ccorelesson";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            List<Lesson> lessons = new ArrayList<Lesson>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String notes = rs.getString("notes");
                Timestamp due = rs.getTimestamp("dueDate");
                Timestamp assigned = rs.getTimestamp("assigndate");
                Lesson l = new Lesson(id,name,notes, due, assigned);
                lessons.add(l);
            }
            return lessons;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    public static List<Lesson> getLessons (Connection conn, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id,name,notes,assigndate,duedate from ccorelesson where classid=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            rs = ps.executeQuery();
            List<Lesson> lessons = new ArrayList<Lesson>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String notes = rs.getString("notes");
                Timestamp due = rs.getTimestamp("dueDate");
                Timestamp assigned = rs.getTimestamp("assigndate");
                Lesson l = new Lesson(id,name,notes, due, assigned);
                lessons.add(l);
            }
            return lessons;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    public static List<LessonOmit> getLessonOmits (Connection conn, int lessonId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select stdId,probId,parentStd,parentCluster from cclessonOmittedObject where lessonId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,lessonId);
            rs = ps.executeQuery();
            List<LessonOmit> omits = new ArrayList<LessonOmit>();
            while (rs.next()) {
                String stdId = rs.getString("stdId");
                boolean omitStd = !rs.wasNull();
                int probId = rs.getInt("probId");
                String parentStd = rs.getString("parentStd");
                int parentCluster = rs.getInt("parentCluster");
                LessonOmit o =
                        new LessonOmit(stdId,probId,parentCluster,parentStd, omitStd ? LessonOmit.type.standard : LessonOmit.type.problem);
                omits.add(o);
            }
            return omits;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


    public static List<ClassLesson> getClassLessons(Connection conn, int classId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select id,masterLessonId,duedate,assigndate from classlesson where classid=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1,classId);
            rs = ps.executeQuery();
            List<ClassLesson> lessons = new ArrayList<ClassLesson>();
            while (rs.next()) {
                int id=rs.getInt("id");
                int masterLessonId = rs.getInt("masterLessonsId");
                Timestamp due = rs.getTimestamp("duedate");
                Timestamp assigned = rs.getTimestamp("assigndate");
                ClassLesson cl = new ClassLesson(id,masterLessonId,due,assigned);
                List<LessonOmit> omits = getLessonOmits(conn,id);
                cl.setOmits(omits);
                lessons.add(cl);
            }
            return lessons;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


}
