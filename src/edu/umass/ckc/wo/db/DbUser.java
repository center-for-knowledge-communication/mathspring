package edu.umass.ckc.wo.db;

import com.mysql.jdbc.Statement;
import edu.umass.ckc.wo.log.RequestActions;
import edu.umass.ckc.wo.login.PasswordAuthentication;
import edu.umass.ckc.wo.state.StudentState;
import edu.umass.ckc.wo.tutor.probSel.LessonModelParameters;
import edu.umass.ckc.wo.tutor.probSel.TopicModelParameters;
import edu.umass.ckc.wo.tutor.studmod.AffectStudentModel;
import edu.umass.ckc.wo.tutor.studmod.StudentModelMotivational;
import edu.umass.ckc.wo.util.SqlQuery;
import edu.umass.ckc.wo.tutormeta.TopicMastery;
import edu.umass.ckc.wo.beans.Topic;
import edu.umass.ckc.wo.tutor.Settings;
import edu.umass.ckc.wo.smgr.User;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


/**
 * Copyright (c) University of Massachusetts
 * Written by: David Marshall
 * Date: Feb 11, 2008
 * Time: 4:38:15 PM
 * 
 * Frank 01-20-2020 Issue #39 use classId as alternative password - test for class id
 * Frank 09-14-2020	issue #237 added pauseStudentUse coding
 * Frank 09-14-2020	issue #237 added 'exclude test users from pause'
 * Frank 09-29-2020	issue #237R3 added 'exclude test users from pause'
 * Frank 12-26-20	issue #329 use multi-lingual version of getAllTopics()
 * Frank 05-19-21   issue #473 cropt lname to 2 characters 
 */
public class DbUser {


    public static User getStudent(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select * from student where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String fn = rs.getString("fname");
                String ln = rs.getString("lname");
                String pw = rs.getString("password");
                String un = rs.getString("username");
                String email = rs.getString("email");
                int classId = rs.getInt("classId");
                return new User(fn, ln, un, email, pw, studId);
            }
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return null;
    }


    public static int getGuestUserCounter(Connection conn) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "update guestcount set count=LAST_INSERT_ID(count+1)";
            ps = conn.prepareStatement(q);
            ps.executeUpdate();
            ps.close();
            q = "select LAST_INSERT_ID() as counter";
            ps = conn.prepareStatement(q);
            rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count;
            }
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
        return 0;
    }

    public static int setStudentPedagogy(Connection conn, int studId, int pedId) throws SQLException {
        PreparedStatement ps = null;
        try {
            String q = "update student set pedagogyId=? where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, pedId);
            ps.setInt(2, studId);
            return ps.executeUpdate();
        } finally {

            if (ps != null)
                ps.close();
        }
    }

    /**
     * returns a strategyId.  -1 will be put in place if null is there.
     * @param conn
     * @param studId
     * @return
     * @throws SQLException
     */
    public static int getStudentStrategy(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select strategyId from student where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int stratId = rs.getInt(1);
                if (rs.wasNull())
                    return -1;
                else return stratId;
            }
            return -1;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    public static int getStudentPedagogy(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select pedagogyId from student where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int pedId = rs.getInt(1);
                if (rs.wasNull())
                    return -1;
                else return pedId;
            }
            return -1;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    public static int getStudentOverridePedagogy(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select overridePedagogy from userPedagogyParameters where studId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int pedId = rs.getInt(1);
                if (rs.wasNull())
                    return -1;
                else return pedId;
            }
            return -1;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();

        }
    }

    // This is for a one-time clean-up converting XML pedagogies to db ones.
    public static void insertStudentOverridePedagogy(Connection conn, int studId, int pedId, LessonModelParameters lparams) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        TopicModelParameters params = (TopicModelParameters) lparams;
        try {
            String q = "insert into userpedagogyparameters (studId, overridePedagogy, showIntro,maxtime,maxprobs,mode," +
                    "singletopicmode,mastery) " +
                    "values (?,?,?,?,?,'ExamplePractice',0,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.setInt(2, pedId);
            TopicModelParameters.frequency f = params.getTopicIntroFrequency();
            // need to set a string value in here. not a number.  Add new cols
            stmt.setBoolean(3, params.getTopicIntroFrequency() != TopicModelParameters.frequency.never);
            stmt.setLong(4, params.getMaxTimeMs());
            stmt.setInt(5, params.getMaxProbs());
            stmt.setDouble(6, params.getDesiredMastery());
            stmt.execute();

        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
    }


    public static boolean isAssistmentsUser(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select uid from assistmentsuser where studId=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next())
                return true;
            else return false;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }


    public static boolean isKeepUser(Connection conn, int studId) throws SQLException {
        boolean[] flags = getUserFlags(conn, studId);
        if (flags != null)
            return flags[0];
        else return true;
    }

    public static boolean isKeepData(Connection conn, int studId) throws SQLException {
        boolean[] flags = getUserFlags(conn, studId);
        if (flags != null)
            return flags[1];
        else return true;
    }

    public static boolean isUpdateStats(Connection conn, int studId) throws SQLException {
        boolean[] flags = getUserFlags(conn, studId);
        if (flags != null)
            return flags[2];
        else return true;
    }

    public static boolean isShowTestControls(Connection conn, int studId) throws SQLException {
        boolean[] flags = getUserFlags(conn, studId);
        if (flags != null)
            return flags[3];
        else return false;
    }


    public static boolean isTestUser(Connection conn, int studId) throws SQLException {
        boolean[] flags = getUserFlags(conn, studId);
        if (flags != null)
            return flags[4];
        else return false;
    }


    public static boolean[] getUserFlags(Connection conn, int studId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select keepUser,keepData,updateStats,showTestControls,trialUser,isGuest from student where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                boolean keepUser = rs.getBoolean(1);
                boolean keepData = rs.getBoolean(2);
                boolean updateStats = rs.getBoolean(3);
                boolean showTestControls = rs.getBoolean(4);
                boolean trialUser = rs.getBoolean(5);
                boolean isGuest = rs.getBoolean(6);
                return new boolean[]{keepUser, keepData, updateStats, showTestControls, trialUser, isGuest};
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();


        }
    }

    public static int getStudent(Connection conn, String userName) throws Exception {
        String q = "select id from Student where userName=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        else return -1;
    }


    public static int getStudent(Connection conn, String userName, String password) throws Exception {
        String q = "select id, password, classId from Student where userName=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String token = rs.getString("password");
            //boolean pwMatch = true;
            boolean pwMatch = PasswordAuthentication.getInstance().authenticate(password.toCharArray(), token);
            if (pwMatch) {
                return rs.getInt(1);
            }
            else {
                int classId = rs.getInt("classId");
                try {
                	int testClassId = Integer.parseInt(password);               	
                	if (testClassId == classId) {
                		System.out.println("Student logging in with class ID");
                		return rs.getInt(1);            		
                	}
                	else {
                		return -1;
                	}
                }
                catch (java.lang.NumberFormatException e) {
                	return -1;
                }
            }
        } 
        else {
        	return -1;
        }
    }


    public static int getStudentByMomsName(Connection conn, String un, String momsName) throws Exception {
        SqlQuery q = new SqlQuery();
        String s = "select id from Student where userName=? and momsName=?";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setString(1, un);
        ps.setString(2, momsName);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return rs.getInt("id");
        else
            return -1;
    }

    public static int getStudentClass(Connection conn, int studId) throws SQLException {
        SqlQuery q = new SqlQuery();
        String s = "select classId from Student where id=?";
        PreparedStatement ps = conn.prepareStatement(s);
        ps.setInt(1, studId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int classId = rs.getInt("classId");
            if (rs.wasNull())
                return -1;
            else return classId;
        } else
            return -1;
    }

    public static void updateStudentClass(Connection conn, int studId, int classId) throws SQLException {
        String q = "update Student set classId=? where id=?";
        PreparedStatement ps = conn.prepareStatement(q);
        ps.setInt(1, classId);
        ps.setInt(2, studId);
        ps.execute();
    }

    /**
     * Create a row in the Student table and return the id.  Return -1 if this duplicates an
     * existing user.
     */
    public static int createUser(Connection conn, String fname, String lname, String userName,
                                 String password, String email, boolean isGuest, boolean isTrialUser) throws Exception {

        int id = DbUser.getStudent(conn, userName, password);
        // if the user exists
        if (id != -1)
            return -1;
        String q;
        q = "insert into Student (fname,lname,userName,email,password,isGuest, trialUser) values (?,?,?,?,?,?,?)";
        PreparedStatement ps;
        ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, fname);
        if (lname.length() > 2) {
        	lname = lname.substring(0, 1);
        }
        ps.setString(2, lname);
        ps.setString(3, userName);
        ps.setString(4, email);
        String token = PasswordAuthentication.getInstance().hash(password.toCharArray());
        ps.setString(5, token);
        if (isGuest)
            ps.setInt(6, 1);
        else ps.setInt(6, 0);
        ps.setBoolean(7, isTrialUser);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return -1;

    }

    public static int createUser(Connection conn, String fname, String lname, String userName,
                                 String password, String email,
                                 String age, String gender, User.UserType userType) throws Exception {
        boolean[] flags = User.getUserTypeFlags(userType);
        boolean keepUser = flags[0];
        boolean keepData = flags[1];
        boolean updateStats = flags[2];
        boolean showTestControls = flags[3];
        int id = DbUser.getStudent(conn, userName, password);
        // if the user exists
        if (id != -1)
            return -1;
        String q;
        q = "insert into Student (fname,lname,userName,email,password,keepUser,keepData,updateStats,showTestControls, trialUser, isGuest,age,gender) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps;
        ps = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, fname);
        if (lname.length() > 2) {
        	lname = lname.substring(0, 1);
        }
        ps.setString(2, lname);
        ps.setString(3, userName);
        ps.setString(4, email);
        String token = PasswordAuthentication.getInstance().hash(password.toCharArray());
        ps.setString(5, token);
        ps.setInt(6, keepUser ? 1 : 0);
        ps.setInt(7, keepData ? 1 : 0);
        ps.setInt(8, updateStats ? 1 : 0);
        ps.setInt(9, showTestControls ? 1 : 0);
        boolean isTrialUser = User.isTrialUser(userType);
        ps.setInt(10, isTrialUser ? 1 : 0);
        ps.setInt(11, userType == User.UserType.guest ? 1 : 0);
        ps.setInt(12, age.length() > 0 ? Integer.parseInt(age) : 0);
        ps.setString(13, gender);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
            return rs.getInt(1);
        return -1;

    }

    public static int getUserIdFromSession(Connection conn, int sessId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select studId from session where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, sessId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int c = rs.getInt(1);
                return c;
            }
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return -1;
    }

    public static List<TopicMastery> getTopicMasteryLevels(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            List<Topic> topics = DbTopics.getAllTopics(conn,studId);
            // Note this fetches all the topics for which a student has levels (If a new topic is added a level for that is not included)
            String q = "select topicId, value, t.description, entered from studenttopicmastery m, problemgroup t where m.topicId=t.id and t.active=1 and studId=? order by m.topicId";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            List<TopicMastery> res = new ArrayList<TopicMastery>();
            while (rs.next()) {
                int topicId = rs.getInt(1);
                double v = rs.getDouble(2);
                String descr = rs.getString(3);
                boolean entered = rs.getBoolean(4);
                res.add(new TopicMastery(new Topic(topicId, descr), v, entered));
            }
            return res;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static List<String> getTopicMasteryLevelsOld(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select value from woproperty where objid=? order by position";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            List<String> res = new ArrayList<String>();
            while (rs.next()) {
                String v = rs.getString(1);
                res.add(v);
            }
            return res;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static void clearTopicMastery(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "delete from studenttopicmastery where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.executeUpdate();

        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int insertTopicMastery(Connection conn, int studId, TopicMastery m) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "insert into studenttopicmastery (studId, topicId, value, entered) values (?,?,?,?)";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.setInt(2, m.getTopic().getId());
            stmt.setDouble(3, m.getMastery());
            stmt.setBoolean(4, m.isEntered());
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == Settings.duplicateRowError || e.getErrorCode() == Settings.keyConstraintViolation)
                ;
            else throw e;
        } finally {

            if (stmt != null)
                stmt.close();
        }
        return -1;
    }

    public static void setTopicMasteryLevels(Connection conn, int studId, List<TopicMastery> levels) throws SQLException {
        // remove the rows for the student and then insert
        clearTopicMastery(conn, studId);
        for (TopicMastery m : levels) {
            insertTopicMastery(conn, studId, m);
        }

    }

    public static int updateTopicMasteryLevel(Connection conn, int studId, TopicMastery m) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "update studenttopicmastery set value=?, entered=1 where studid=? and topicid=?";
            stmt = conn.prepareStatement(q);
            stmt.setFloat(1, (float) m.getMastery());
            stmt.setInt(2, studId);
            stmt.setInt(3, m.getTopic().getId());
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static boolean isLoginPaused(Connection conn, int studId) throws SQLException {
    	
    	boolean result = false;
    	ResultSet rs = null;
        PreparedStatement stmt = null;
/*
        // First test global flag
        String globalPause = "0";
      	try {
      		globalPause = Settings.getString(conn, "pauseStudentUse");
      		if (globalPause.equals("1")) {
      			return true;
      		}
      	}
      	catch (Exception e) {
      		System.out.println(e.getMessage());
      	}        	
*/
      	// if global flag is false, check if teacher paused
        try {
            String q = "select t.pauseStudentUse as pause, t.id, c.id, s.classId, s.trialUser as trialUser from teacher t, class c, student s where s.id=? and s.classId = c.id and c.teacherId=t.id";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	int trialUser = rs.getInt("trialuser");
                int pause = rs.getInt("pause");
                if ((pause == 1) && (trialUser == 0)) {
                	result = true;
                }
            } else result = false;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
        return result;
    }
    
    public static User getTeacherEmail(Connection conn, int classId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select email,fname,lname,u.id from teacher u, class c where c.id=? and c.teacherId=u.id";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, classId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String email = rs.getString(1);
                String fname = rs.getString(2);
                String lname = rs.getString(3);
                int id = rs.getInt(4);
                return new User(fname, lname, null, email, "", id);

            } else return null;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int deleteChildRows(Connection conn, String idField, int id, String childTable) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "delete from " + childTable + " where " + idField + " =?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    /**
     * Determine if a student has been in the system before based on an entry in the EventLog table
     *
     * @param conn
     * @param studId
     * @return
     * @throws SQLException
     */
    public static boolean isFirstLogin(Connection conn, int studId, int curSessId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select * from eventlog where studId=? and sessNum!=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.setInt(2, curSessId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }
            return true;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }

    /**
     * Remove all traces of the student from the system
     *
     * @param conn
     * @param studId
     */
    public static int deleteStudent(Connection conn, int studId) throws SQLException {
        // must delete all child tables that relate to this through a foreign key.
        PreparedStatement stmt = null;
        boolean error = false;
        try {
            deleteSessions(conn, studId); // this happens in a transaction
            conn.setAutoCommit(false);
            deleteStudentData(conn, studId);  // get rid of the data about the student
            deleteUserProfile(conn, studId);
            deleteFromPedagogyGroup(conn, studId);


            String q = "delete from student where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failure during deletion of Student " + studId + " " + e.getMessage());
            error = true;
            conn.rollback();
            System.out.println("deleteStudent Rolled back");
            throw e;
        } finally {
            if (!error) {
                conn.commit();
            }
            conn.setAutoCommit(true);
            if (stmt != null)
                stmt.close();
        }

    }

    public static void deleteStudentData(Connection conn, int studId) throws SQLException {
        deleteChildRows(conn, "studId", studId, "preposttestproblems");
        deleteChildRows(conn, "studId", studId, "studenttopicmastery");
        deleteChildRows(conn, "studId", studId, "flankinguser");
        deleteChildRows(conn, "leftStudId", studId, "flankinguser");
        deleteChildRows(conn, "rightStudId", studId, "flankinguser");
        deleteChildRows(conn, "studId", studId, "eventlog");
        deleteChildRows(conn, "studId", studId, "adventureproblem");
        deleteChildRows(conn, "studId", studId, "episodicadventuredata");
        deleteChildRows(conn, "studId", studId, "studentproblemhistory");
        deleteChildRows(conn, "studId", studId, "affectstudentmodel");
        deleteChildRows(conn, "studId", studId, "basestudentmodel");
        deleteChildRows(conn, "studId", studId, "collaborationlog");
        deleteEngagementData(conn, studId);
        deletePencilPaperData(conn, studId);
        deletePrePostEvents(conn, studId);
        deleteSessions(conn, studId);
        deleteTopicMastery(conn, studId);
        new StudentState(conn, null).clearTutorHutState();
        deleteWoProps(conn, studId);

    }

    // This will erase all trace of the student having been in the tutor hut
    public static void deleteStudentPracticeHutData(Connection conn, int studId) throws SQLException {
        // delete events related to practice hut
        deletePracticeHutEvents(conn, studId);
        // delete woproperties related to practice hut
        resetStudentPracticeHut(conn, studId);
    }


    private static int deletePracticeHutEvents(Connection conn, int studId) throws SQLException {
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            // gets all activities that have to do with the practicehut (about the only one that doesn't is 'navigate'
            String actions = getHutActivityNames(RequestActions.TUTOR_HUT_ACTIVITIES);
            String q = "delete from eventlog where studid=? and action in " + actions;
            String q2 = "delete from studentproblemhistory where studid=?";
            stmt = conn.prepareStatement(q);
            stmt2 = conn.prepareStatement(q2);
            stmt.setInt(1, studId);
            stmt2.setInt(1, studId);
            stmt2.executeUpdate();
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
            if (stmt2 != null)
                stmt2.close();
        }
    }

    private static String getHutActivityNames(String[] tutorHutActivities) {
        StringBuilder sb = new StringBuilder("(");
        for (String a : tutorHutActivities) {
            sb.append("'" + a + "',");
        }
        // replace the trailing , with )
        sb.replace(sb.length() - 1, sb.length(), ")");
        return sb.toString();
    }


    // This will remove state information about the student in the practice hut so that s/he can use it again
    // while retaining event history of work in the practice hut.
    public static void resetStudentPracticeHut(Connection conn, int studId) throws SQLException {
        // delete woproperties related to practice hut
        StudentState state = new StudentState(conn, null);
        state.setObjid(studId);
        state.clearTutorHutState();
        AffectStudentModel sm = new AffectStudentModel(conn);
        sm.setObjid(studId);
        sm.clearTutorHutState();
        StudentModelMotivational sm2 = new StudentModelMotivational(conn);
        sm2.setObjid(studId);
        sm2.clearTutorHutState();
        // N.B.
        // The assumption below is that the topic mastery can be reset because it is only determined by actions
        // in the tutor hut.   If the topic mastery were to be set by activity in the pretest hut, then we
        // can't clear the topic mastery when we reset the tutor hut.
        deleteTopicMastery(conn, studId);
    }

    public static int deleteStudentPrePostEventData(Connection conn, int studId, boolean deletePre, boolean deletePost) throws SQLException {
        StudentState state = new StudentState(conn, null);
        // reset these lists so that the test can be given again
        if (deletePre) {
            state.getPrePostState().clearPreData(conn);
        }
        if (deletePost) {
            state.getPrePostState().clearPostData(conn);
        }
        PreparedStatement stmt = null;
        try {
            String q = "delete from preposteventlog where studId=? ";
            if (deletePre)
                q += " and activityName='pretestProblem'";
            else q += " and activityName!='pretestProblem'";
            if (deletePost)
                q += " and activityName='posttestProblem'";
            else q += " and activityName!='posttestProblem'";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private static int deleteWoProps(Connection conn, int studId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "delete from woproperty where objid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }

    }

    private static void deleteUserProfile(Connection conn, int studId) throws SQLException {
        // delete from userprofile and from userpropval
        PreparedStatement stmt = null;
        try {
            String q = "delete from userprofile where userId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.executeUpdate();
            q = "delete from userpropval where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private static int deleteTopicMastery(Connection conn, int studId) throws SQLException {
        // delete from studenttopicmastery
        PreparedStatement stmt = null;
        try {
            String q = "delete from studenttopicmastery where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private static int deleteSessions(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String q = "select id from session where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                int sessId = rs.getInt(1);
                DbSession.deleteSession(conn, sessId);
                count++;
            }
            return count;
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }

    }

    private static void deletePrePostEvents(Connection conn, int studId) throws SQLException {
        // delete from preposteventlog  and preposttestdata and pretestassignment
        PreparedStatement stmt = null;
        try {
            deleteStudentPrePostEventData(conn, studId, true, true);
            // seems like this table might no longer be in use.
            String q = "delete from preposttestdata where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.executeUpdate();
            q = "delete from pretestassignment where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private static int deletePencilPaperData(Connection conn, int studId) throws SQLException {
        //To change body of created methods use File | Settings | File Templates.
        PreparedStatement stmt = null;
        try {
            String q = "delete from pencilpaperdata where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private static int deleteMFRScore(Connection conn, int studId) throws SQLException {
        // delete from mfrtestdata for studId
        PreparedStatement stmt = null;
        try {
            String q = "delete from mfrtestdata where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }


    private static int deleteFromPedagogyGroup(Connection conn, int studId) throws SQLException {
        // delete interventionengagementdata
        PreparedStatement stmt = null;
        try {
            String q = "delete from pedagogygroup where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    private static int deleteEngagementData(Connection conn, int studId) throws SQLException {
        // delete interventionengagementdata
        PreparedStatement stmt = null;
        try {
            String q = "delete from interventionengagementdata where studid=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }


    /**
     * Supports modification of some fields about a student (presented in an editor in the admin pages)
     *
     * @param conn
     * @param studId
     * @param uname
     * @param fname
     * @param password
     * @param pedagogyId
     * @return
     * @throws SQLException
     */
    public static int alterStudent(Connection conn, int studId, String uname, String fname, String password, int pedagogyId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "update student set fname=?, username=?, password=?, pedagogyId=? where id=?";
            stmt = conn.prepareStatement(q);

            stmt.setString(1, fname);
            stmt.setString(2, uname);
            String token = PasswordAuthentication.getInstance().hash(password.toCharArray());
            stmt.setString(3, token);
            stmt.setInt(4, pedagogyId);
            stmt.setInt(5, studId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static int setUserNames(Connection conn, int studentId, String fname, String lini) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String q = "update student set fname=?, lname=?  where id=?";
            stmt = conn.prepareStatement(q);
            stmt.setString(1, fname);
            stmt.setString(2, lini);
            stmt.setInt(3, studentId);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    public static void setFlankingUsers(Connection conn, int studId, int leftStudId, int rightStudId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String q = "select studId,leftStudId, rightStudId from flankingUser where studId=?";
            ps = conn.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1, studId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("studId");
                int l = rs.getInt("leftStudId");
                if (leftStudId > 0)
                    rs.updateInt("leftStudId", leftStudId);
                else rs.updateNull("leftStudId");
                int r = rs.getInt("rightStudId");
                if (rightStudId > 0)
                    rs.updateInt("rightStudId", rightStudId);
                else rs.updateNull("rightStudId");
                rs.updateRow();
            } else {
                rs = null;
                PreparedStatement stmt = null;
                try {
                    q = "insert into flankingUser (studId,leftStudId,rightStudId) values (?,?,?)";
                    stmt = conn.prepareStatement(q);
                    stmt.setInt(1, studId);
                    if (leftStudId > 0)
                        stmt.setInt(2, leftStudId);
                    else stmt.setNull(2, Types.INTEGER);
                    if (rightStudId > 0)
                        stmt.setInt(3, rightStudId);
                    else stmt.setNull(3, Types.INTEGER);
                    stmt.execute();
                } finally {
                    if (rs != null)
                        rs.close();
                    if (stmt != null)
                        stmt.close();
                }
            }
        } finally {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }
    }

    public static int getStudentFromSession(Connection conn, int sessId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String q = "select studId from session where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, sessId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
        }
    }

    /**
     * Go through all users sessions and figure out how much time they've been logged into the tutor for and return that number in minutes.
     *
     * @param conn
     * @param studId
     * @return
     */
    public static int getLoggedInTimeInMinutes(Connection conn, int studId) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            long totalLoggedTime = 0;
            String q = "select beginTime, lastAccessTime from session where studId=?";
            stmt = conn.prepareStatement(q);
            stmt.setInt(1, studId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp bt = rs.getTimestamp(1);
                Timestamp lt = rs.getTimestamp(2);
                long sessLen = lt.getTime() - bt.getTime();
                totalLoggedTime += sessLen;
            }
            return (int) totalLoggedTime / 60000;  // converts from ms to min
        } finally {
            if (stmt != null)
                stmt.close();
            if (rs != null)
                rs.close();
        }
    }


    public static void main(String[] args) {
        try {
            Connection conn = DbUtil.getAConnection("rose.cs.umass.edu");
            PreparedStatement ps = null;
            ResultSet rs = null;
            PreparedStatement stmt = null;
            try {
                String q = "select id, password, oldpw from student";
                stmt = conn.prepareStatement(q,ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    int c = rs.getInt(1);
                    String pw = rs.getString("password");
                    String token = PasswordAuthentication.getInstance(0).hash(pw.toCharArray());
                    rs.updateString("password",token);
                    rs.updateString("oldpw",pw);
                    rs.updateRow();
                    System.out.println(token);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static int setStrategy(Connection conn, int studId, int strategyId) throws SQLException {
        PreparedStatement ps = null;
        try {
            String q = "update student set strategyId=? where id=?";
            ps = conn.prepareStatement(q);
            ps.setInt(1, strategyId);
            ps.setInt(2, studId);
            return ps.executeUpdate();
        } finally {
            if (ps != null)
                ps.close();
        }

    }
	    public static int isTrialUser(Connection conn, int studId) throws SQLException {
	    	int flag = 0;
	    	ResultSet rs = null;
	        PreparedStatement stmt = null;
	        try {
	            String q = "select trialUser from student where id=?";
	            stmt = conn.prepareStatement(q);
	            stmt.setInt(1, studId);
	            rs = stmt.executeQuery();
	            while (rs.next()) {
	                flag = rs.getInt("trialUser");
	                return flag;
	            }
	        } finally {
	            if (stmt != null)
	                stmt.close();
	            if (rs != null)
	                rs.close();
	        }
	        return flag;
	    }
	   
    

}
